package com.FREEMOD.freemod.main.handler;

import com.FREEMOD.freemod.main.config.ModKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = com.FREEMOD.freemod.main.FreeMod.MOD_ID, value = Dist.CLIENT)
public class ClientForgeFrameHandler {

    /**
     * 💡 対策1: ドローンモード中、プレイヤーが勝手に歩き出すのを防ぐ
     */
    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if (ClientCameraHandler.isDroneMode()) {
            if (event.getInput() != null) {
                event.getInput().forwardImpulse = 0.0F;
                event.getInput().leftImpulse = 0.0F;
                event.getInput().up = false;
                event.getInput().down = false;
                event.getInput().left = false;
                event.getInput().right = false;
                event.getInput().jumping = false;
                event.getInput().shiftKeyDown = false;
            }
        }
    }

    /**
     * 💡 対策2: 毎フレームの描画処理（三人称視点のロックと移動ロジックの呼び出し）
     */
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options == null) return;

        if (ClientCameraHandler.isDroneMode()) {
            // 💡 視点を「三人称後方（F5キーの状態）」に強制ロック
            if (mc.options.getCameraType() != net.minecraft.client.CameraType.THIRD_PERSON_BACK) {
                mc.options.setCameraType(net.minecraft.client.CameraType.THIRD_PERSON_BACK);
            }

            // ClientCameraHandler側の移動・同期ロジックを実行する
            ClientCameraHandler.onRenderTick(event.renderTickTime);
        }
    }

    // 手の非表示
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event){
        if (ClientCameraHandler.isDroneMode()){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();

        // インベントリやメニューを開いていない（ゲーム画面を操作している）ときだけ反応させる
        if (mc.screen == null && mc.player != null) {
            // 登録したキー（右CTRL）が押されたかを判定
            if (ModKeyBindings.droneExitKey.consumeClick()) {
                // ドローンモード中であれば、トグル（終了）させる
                if (ClientCameraHandler.isDroneMode()) {
                    ClientCameraHandler.toggleDroneMode();
                }
            }
        }
    }
}