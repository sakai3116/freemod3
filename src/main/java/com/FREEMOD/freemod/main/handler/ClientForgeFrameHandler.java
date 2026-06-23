package com.FREEMOD.freemod.main.handler;

import com.FREEMOD.freemod.main.FreeMod;
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
        // Renderの終了タイミングで同期を行う
        if (event.phase == TickEvent.Phase.END) {
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
}