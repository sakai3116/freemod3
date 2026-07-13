//package com.FREEMOD.freemod.main.handler;
//
//import com.example.cameramod.item.CameraControllerItem;
//import net.minecraft.client.Minecraft;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.ViewportEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(value = Dist.CLIENT)
//public class ClientViewHandler {
//
//    @SubscribeEvent
//    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
//        if (CameraControllerItem.isViewing() && CameraControllerItem.getActiveCameraPos() != null) {
//            // カメラの向き（角度）を固定、またはマウス操作に応じて動かしたい場合はここを調整します
//            // event.setPitch(0); // 上下の角度
//            // event.setYaw(0);   // 左右の角度
//        }
//    }
//
//    @SubscribeEvent
//    public static void onComputeCameraPosition(ViewportEvent.ComputeCameraPosition event) {
//        if (CameraControllerItem.isViewing() && CameraControllerItem.getActiveCameraPos() != null) {
//            var pos = CameraControllerItem.getActiveCameraPos();
//            // 視点の位置を、リンクされたカメラブロックの中心（+0.5）に強制固定する
//            event.getPosition().set(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
//        }
//    }
//}