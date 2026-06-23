package com.FREEMOD.freemod.main.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

public class ClientCameraHandler {
    private static boolean isDroneMode = false;
    private static Entity cameraDummy = null;
    private static Entity originalCameraEntity = null;

    private static float originalPlayerYaw = 0.0F;
    private static float originalPlayerPitch = 0.0F;

    // 💡 ドローンの現在位置を保持する変数
    private static double droneX = 0.0D;
    private static double droneY = 0.0D;
    private static double droneZ = 0.0D;

    // ドローンの移動速度（好みに合わせて調整してください）
    private static final double MOVE_SPEED = 0.12D;

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
            originalPlayerYaw = mc.player.getYRot();
            originalPlayerPitch = mc.player.getXRot();

            // 初期位置をプレイヤーの頭上20マスに設定
            droneX = mc.player.getX();
            droneY = mc.player.getY() + 20.0D;
            droneZ = mc.player.getZ();

            cameraDummy = EntityType.MARKER.create(mc.level);
            if (cameraDummy != null) {
                cameraDummy.setPos(droneX, droneY, droneZ);
                cameraDummy.noPhysics = true;

                cameraDummy.setYRot(originalPlayerYaw);
                cameraDummy.setXRot(originalPlayerPitch);
                cameraDummy.xo = droneX;
                cameraDummy.yo = droneY;
                cameraDummy.zo = droneZ;
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

    /**
     * 毎フレーム（Renderバス）で呼び出される同期＆移動ロジック
     */
    public static void onRenderTick(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (!isDroneMode || cameraDummy == null || mc.player == null) return;

        // 1. マウス操作によるプレイヤーの回転を取得
        float currentYaw = mc.player.getYRot();
        float currentPitch = mc.player.getXRot();

        // 2. 💡 キー入力によるドローンの移動計算
        Options options = mc.options;
        double moveForward = 0.0D;
        double moveStrafe = 0.0D;
        double moveVertical = 0.0D;

        if (options.keyUp.isDown()) moveForward += 1.0D;    // Wキー
        if (options.keyDown.isDown()) moveForward -= 1.0D;  // Sキー
        if (options.keyLeft.isDown()) moveStrafe += 1.0D;   // Aキー
        if (options.keyRight.isDown()) moveStrafe -= 1.0D;  // Dキー
        if (options.keyJump.isDown()) moveVertical += 1.0D;   // スペースキー（上昇）
        if (options.keyShift.isDown()) moveVertical -= 1.0D;  // シフトキー（下降）

        // カメラが向いている方角（Yaw）を基準に、WASDの移動方向を3次元ベクトルに変換
        if (moveForward != 0 || moveStrafe != 0 || moveVertical != 0) {
            float yawRad = currentYaw * ((float)Math.PI / 180F);
            double sinYaw = Mth.sin(yawRad);
            double cosYaw = Mth.cos(yawRad);

            // 前後・左右の移動ベクトル計算
            double fX = -sinYaw * moveForward;
            double fZ = cosYaw * moveForward;
            double sX = cosYaw * moveStrafe;
            double sZ = sinYaw * moveStrafe;

            // 座標変数（droneX, Y, Z）を更新
            droneX += (fX + sX) * MOVE_SPEED;
            droneY += moveVertical * MOVE_SPEED;
            droneZ += (fZ + sZ) * MOVE_SPEED;
        }

        // 3. カメラ（ダミー）の座標と角度を更新
        // 前フレームの座標（Oがつく変数）に「移動前の座標」を入れることで、
        // 移動時もカクつかず滑らか（ヌルヌル）に並進移動させます
        cameraDummy.xo = cameraDummy.getX();
        cameraDummy.yo = cameraDummy.getY();
        cameraDummy.zo = cameraDummy.getZ();

        cameraDummy.setPos(droneX, droneY, droneZ);

        cameraDummy.setYRot(currentYaw);
        cameraDummy.setXRot(currentPitch);
        cameraDummy.yRotO = mc.player.yRotO;
        cameraDummy.xRotO = mc.player.xRotO;

        // 4. 下にいるプレイヤー本体の同期（その場で描画を固定、移動・回転入力を打ち消す）
        mc.player.setDeltaMovement(0, 0, 0); // ドローン操作中にプレイヤーが勝手に動かないように物理移動をゼロに
        mc.player.yBodyRot = originalPlayerYaw;
        mc.player.yBodyRotO = originalPlayerYaw;
        mc.player.yHeadRot = originalPlayerYaw;
        mc.player.yHeadRotO = originalPlayerYaw;
    }
}