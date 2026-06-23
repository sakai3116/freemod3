package com.FREEMOD.freemod.main.handler;

import com.FREEMOD.freemod.main.FreeMod;
import com.FREEMOD.freemod.main.config.ModKeyBindings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FreeMod.MOD_ID, value = Dist.CLIENT)
public class ClientForgeFrameHandler {

    // ClientForgeTickHandler の中身はこれだけにしてください
//    @SubscribeEvent
//    public static void onClientTick(TickEvent.ClientTickEvent event) {
//        if (event.phase == TickEvent.Phase.END) {
//            ClientCameraHandler.onClientTick();
//        }
//    }

    // カメラの描画
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientCameraHandler.onRenderTick(event.renderTickTime);

            // 💡 ここを追記！
            // ドローンモード中に、登録したキー(デフォルトはX)が押されたか判定
            if (ClientCameraHandler.isDroneMode() && ModKeyBindings.droneExitKey.consumeClick()) {
                // 強制的にドローンモードを終了（トグル）させる
                ClientCameraHandler.toggleDroneMode();
            }
        }
    }
    // 手の非表示
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event){
        if (ClientCameraHandler.isDroneMode()){
            event.setCanceled(true);
        }
    }
}