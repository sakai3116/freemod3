package com.FREEMOD.freemod.main.handler;

import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.item.CameraControllerItem;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FreeMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientCameraViewHandler {

    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {

        if (!CameraControllerItem.isViewing()) return;

        CameraEntity camera =
                CameraControllerItem.getActiveCamera();

        if (camera == null) return;

        event.setYaw(camera.getYRot());
        event.setPitch(camera.getXRot());

    }

    /**
     * アップデート：カメラ視聴中に「通常の右クリック」を入力したら安全に解除する処理
     */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();

        // カメラ視聴中かつ、プレイヤーがコントローラーを持っている状態で
        if (CameraControllerItem.isViewing() && mc.player != null) {

            // 通常の右クリック（アイテム使用キー）がカチッと押されたら解除
            if (mc.options.keyUse.isDown() && !mc.player.isShiftKeyDown()) {

                // 視聴状態をリセット
                CameraControllerItem.setViewing(false);
                CameraControllerItem.setActiveCamera(null);

                // 視点をプレイヤー本体に戻す
                mc.setCameraEntity(mc.player);
                mc.player.displayClientMessage(new net.minecraft.network.chat.TextComponent("カメラ接続を解除し、プレイヤー視点に戻りました。"), true);

                // チャタリング防止のクールダウン
                mc.player.getCooldowns().addCooldown(mc.player.getMainHandItem().getItem(), 15);
            }
        }
    }
}