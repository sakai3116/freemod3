package com.FREEMOD.freemod.main.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ClientCameraHandler {
    private static boolean isDroneMode = false;
    private static ArmorStand cameraDummy = null;
    private static Entity originalCameraEntity = null;

    public static void toggleDroneMode() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (!isDroneMode) {
            // --- ドローンモード開始 ---
            isDroneMode = true;
            originalCameraEntity = mc.getCameraEntity();

            // カメラの身代わりとなるダミーエンティティを生成 (見えない防具立てなど)
            cameraDummy = new ArmorStand(mc.level, mc.player.getX(), mc.player.getY() + 1.5, mc.player.getZ());
            cameraDummy.setNoGravity(true);
            cameraDummy.setInvisible(true); // 透明にする

            if (mc.level != null) {
                mc.level.addFreshEntity(cameraDummy);
            }

            // 視点をダミーに変更
            mc.setCameraEntity(cameraDummy);
            mc.player.displayClientMessage(new TextComponent("ドローン視点：起動"),true);

        } else {
            // --- ドローンモード終了 ---
            isDroneMode = false;

            // 視点を元のプレイヤーに戻す
            if (originalCameraEntity != null) {
                mc.setCameraEntity(originalCameraEntity);
            }

            // ダミーを消去
            if (cameraDummy != null) {
                cameraDummy.discard();
                cameraDummy = null;
            }

            mc.player.displayClientMessage(new TextComponent("ドローン視点：起動"),true);
        }
    }

    // クライアントの毎フレームの更新（Tickイベントなど）で、キー入力を感知して cameraDummy を動かすロジックをここに追記します
    public static void moveCamera(double x, double y, double z) {
        if (isDroneMode && cameraDummy != null) {
            cameraDummy.setPos(cameraDummy.getX() + x, cameraDummy.getY() + y, cameraDummy.getZ() + z);
        }
    }
}