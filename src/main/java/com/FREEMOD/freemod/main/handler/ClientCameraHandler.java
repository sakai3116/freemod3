package com.FREEMOD.freemod.main.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.TextComponent;

public class ClientCameraHandler {
    private static boolean isDroneMode = false;
    private static Entity cameraDummy = null;
    private static Entity originalCameraEntity = null;

    // 起動時のプレイヤーの向きを記憶する変数
    private static float originalPlayerYaw = 0.0F;
    private static float originalPlayerPitch = 0.0F;

    public static boolean isDroneMode() {
        return isDroneMode;
    }

    public static void toggleDroneMode() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (!isDroneMode) {
            // --- ドローンモード開始 ---
            isDroneMode = true;
            originalCameraEntity = mc.getCameraEntity();

            // 起動時のプレイヤーの「本来の向き」を記憶
            originalPlayerYaw = mc.player.getYRot();
            originalPlayerPitch = mc.player.getXRot();

            cameraDummy = EntityType.MARKER.create(mc.level);
            if (cameraDummy != null) {
                cameraDummy.setPos(mc.player.getX(), mc.player.getY() + 20.0, mc.player.getZ());
                cameraDummy.noPhysics = true;

                // ダミーの初期角度をプレイヤーに合わせる
                cameraDummy.setYRot(originalPlayerYaw);
                cameraDummy.setXRot(originalPlayerPitch);
                cameraDummy.yRotO = originalPlayerYaw;
                cameraDummy.xRotO = originalPlayerPitch;

                mc.setCameraEntity(cameraDummy);
            }
            mc.player.displayClientMessage(new TextComponent("ドローン視点：起動"), true);

        } else {
            // --- ドローンモード終了 ---
            isDroneMode = false;

            if (originalCameraEntity != null) {
                mc.setCameraEntity(originalCameraEntity);
            }

            // 終了時、プレイヤーの向きを「起動した時の向き」に強制リセット
            if (mc.player != null) {
                mc.player.setYRot(originalPlayerYaw);
                mc.player.setXRot(originalPlayerPitch);
                mc.player.yRotO = originalPlayerYaw;
                mc.player.xRotO = originalPlayerPitch;
            }

            if (cameraDummy != null) {
                cameraDummy.discard();
                cameraDummy = null;
            }
            mc.player.displayClientMessage(new TextComponent("ドローン視点：終了"), true);
        }
    }

    public static void onClientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (!isDroneMode || cameraDummy == null || mc.player == null) return;

        // 💡 ここが最大のポイント
        // マウス操作によって動いた「プレイヤーの現在の視点」を、そのままカメラ（ダミー）の角度にコピーする
        float currentYaw = mc.player.getYRot();
        float currentPitch = mc.player.getXRot();

        cameraDummy.setYRot(currentYaw);
        cameraDummy.setXRot(currentPitch);
        cameraDummy.yRotO = mc.player.yRotO;
        cameraDummy.xRotO = mc.player.xRotO;

        // カメラ位置の完全固定
        cameraDummy.setPos(mc.player.getX(), mc.player.getY() + 20.0, mc.player.getZ());
        cameraDummy.xo = cameraDummy.getX();
        cameraDummy.yo = cameraDummy.getY();
        cameraDummy.zo = cameraDummy.getZ();

        // 💡 ドローン中に、下のプレイヤーの「体（モデル）」がキョロキョロ回転して見えないように、
        // レンダリング（描画）用の体の向きだけを固定する（yBodyRot, yHeadRot）
        mc.player.yBodyRot = originalPlayerYaw;
        mc.player.yBodyRotO = originalPlayerYaw;
        mc.player.yHeadRot = originalPlayerYaw;
        mc.player.yHeadRotO = originalPlayerYaw;
    }
}