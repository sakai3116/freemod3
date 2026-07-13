package com.FREEMOD.freemod.main.handler;

import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.item.CameraControllerItem;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FreeMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientCameraViewHandler {

    /**
     * 1.18.2 対応：カメラ視聴中にプレイヤーのマウス入力をカメラエンティティへ同期し、首振りを可能にする処理
     */
    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        Minecraft mc = Minecraft.getInstance();

        // カメラコントローラーで視聴中かつ、アクティブなカメラが存在する場合のみ実行
        if (CameraControllerItem.isViewing() && CameraControllerItem.getActiveCamera() != null) {
            Player player = mc.player;
            CameraEntity camera = CameraControllerItem.getActiveCamera();

            if (player != null && camera != null) {
                // プレイヤーのマウス操作による角度（Yaw / Pitch）をカメラエンティティにリアルタイム同期
                camera.setXRot(player.getXRot());
                camera.setYRot(player.getYRot());
                camera.xRotO = player.xRotO;
                camera.yRotO = player.yRotO;

                // 1.18.2のカメラ角度上書き処理
                event.setPitch(player.getXRot());
                event.setYaw(player.getYRot());
            }
        }
    }
}