package com.FREEMOD.freemod.main.handler;

import com.FREEMOD.freemod.entity.DroneEntity;
import com.FREEMOD.freemod.register.EntityRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.CameraType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.ChatFormatting;
import net.minecraft.world.phys.Vec3;

public class ClientCameraHandler {
    private static boolean isDroneMode = false;
    private static DroneEntity droneBodyEntity = null;
    private static Entity cameraDummy = null;
    private static Entity originalCameraEntity = null;

    private static float originalPlayerYaw = 0.0F;
    private static float originalPlayerPitch = 0.0F;
    private static double droneX = 0.0D;
    private static double droneY = 0.0D;
    private static double droneZ = 0.0D;
    private static final double MOVE_SPEED = 0.12D;
    private static final double MAX_DISTANCE = 100.0D;
    private static final long MAX_FLIGHT_TIME_MS = 20000L;
    private static long flightStartTime = 0L;

    private static final double CAMERA_BACK_OFFSET = 3.5D;
    private static final double CAMERA_UP_OFFSET = 1.2D;

    public static boolean isDroneMode() {
        return isDroneMode;
    }

    public static void toggleDroneMode() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (!isDroneMode) {
            // --- ドローンモード開始 ---
            isDroneMode = true;
            flightStartTime = System.currentTimeMillis();
            originalCameraEntity = mc.getCameraEntity();
            originalPlayerYaw = mc.player.getYRot();
            originalPlayerPitch = mc.player.getXRot();

            droneX = mc.player.getX();
            droneY = mc.player.getY() + 2.0D; // プレイヤーの少し上(2マス)からスタート
            droneZ = mc.player.getZ();

            Entity entity = EntityRegister.DRONE.get().create(mc.level);
            if (entity instanceof DroneEntity) {
                droneBodyEntity = (DroneEntity) entity;
                droneBodyEntity.setPos(droneX, droneY, droneZ);
                droneBodyEntity.noPhysics = true;
                droneBodyEntity.setYRot(originalPlayerYaw);
                droneBodyEntity.setXRot(originalPlayerPitch);
                mc.level.putNonPlayerEntity(droneBodyEntity.getId(), droneBodyEntity);
            }

            cameraDummy = EntityType.MARKER.create(mc.level);
            if (cameraDummy != null) {
                // 💡 組み込み：起動した瞬間のカメラの位置を、最初からドローンの「真後ろ」に計算して配置
                float yawRad = originalPlayerYaw * ((float)Math.PI / 180F);
                float pitchRad = originalPlayerPitch * ((float)Math.PI / 180F);

                double initCamX = droneX - (Mth.sin(yawRad) * Mth.cos(pitchRad) * CAMERA_BACK_OFFSET);
                double initCamZ = droneZ + (Mth.cos(yawRad) * Mth.cos(pitchRad) * CAMERA_BACK_OFFSET);
                double initCamY = droneY - (Mth.sin(pitchRad) * CAMERA_BACK_OFFSET) + CAMERA_UP_OFFSET;

                cameraDummy.setPos(initCamX, initCamY, initCamZ);
                cameraDummy.noPhysics = true;

                // 💡 組み込み：カメラの向きを180度反転させて、最初からドローンの背中を捉える
                cameraDummy.setYRot(originalPlayerYaw + 180.0F);
                cameraDummy.setXRot(originalPlayerPitch);

                mc.setCameraEntity(cameraDummy);
            }

            mc.player.displayClientMessage(new TextComponent("ドローン視点：起動 (制限時間: 20秒 / 距離: 100m)"), true);
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
                // 💡 物理移動ロックを解除（動けないバグの修正）
                mc.player.setDeltaMovement(Vec3.ZERO);
            }

            if (mc.options != null) {
                mc.options.setCameraType(CameraType.FIRST_PERSON);
            }

            if (droneBodyEntity != null) {
                droneBodyEntity.discard();
                droneBodyEntity = null;
            }

            if (cameraDummy != null) {
                cameraDummy.discard();
                cameraDummy = null;
            }
            mc.player.displayClientMessage(new TextComponent("ドローン視点：終了"), true);
        }
    }

    public static void onRenderTick(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (!isDroneMode || cameraDummy == null || droneBodyEntity == null || mc.player == null) return;

        // 1. 時間制限チェック
        long elapsedMs = System.currentTimeMillis() - flightStartTime;
        if (elapsedMs >= MAX_FLIGHT_TIME_MS) {
            mc.player.displayClientMessage(new TextComponent("バッテリー切れ：ドローンが強制シャットダウンしました").withStyle(ChatFormatting.RED), false);
            toggleDroneMode();
            return;
        }

        // 2. 距離制限チェック
        double distanceSq = mc.player.distanceToSqr(droneX, droneY, droneZ);
        if (distanceSq > MAX_DISTANCE * MAX_DISTANCE) {
            mc.player.displayClientMessage(new TextComponent("通信途絶：最大通信距離（100m）を超えました").withStyle(ChatFormatting.RED), false);
            toggleDroneMode();
            return;
        }

        // 3. アクションバー表示
        double currentDistance = Math.sqrt(distanceSq);
        long remainingSeconds = (MAX_FLIGHT_TIME_MS - elapsedMs) / 1000L;
        String statusText = String.format("【ドローン】 距離: %.1fm / 100m | 残り時間: %d秒", currentDistance, remainingSeconds);
        TextComponent message = new TextComponent(statusText);
        message.withStyle(remainingSeconds <= 5 ? ChatFormatting.GOLD : ChatFormatting.AQUA);
        mc.player.displayClientMessage(message, true);

        // 4. 回転の取得
        float currentYaw = mc.player.getYRot();
        float currentPitch = mc.player.getXRot();

        // 5. WASD移動計算
        Options options = mc.options;
        double moveForward = 0.0D;
        double moveStrafe = 0.0D;
        double moveVertical = 0.0D;

        if (options.keyUp.isDown()) moveForward += 1.0D;
        if (options.keyDown.isDown()) moveForward -= 1.0D;
        if (options.keyLeft.isDown()) moveStrafe += 1.0D;
        if (options.keyRight.isDown()) moveStrafe -= 1.0D;
        if (options.keyJump.isDown()) moveVertical += 1.0D;
        if (options.keyShift.isDown()) moveVertical -= 1.0D;

        if (moveForward != 0 || moveStrafe != 0 || moveVertical != 0) {
            float yawRad = currentYaw * ((float)Math.PI / 180F);
            double sinYaw = Mth.sin(yawRad);
            double cosYaw = Mth.cos(yawRad);

            double fX = sinYaw * moveForward;
            double fZ = -cosYaw * moveForward;
            double sX = -cosYaw * moveStrafe;
            double sZ = -sinYaw * moveStrafe;

            droneX += (fX + sX) * MOVE_SPEED;
            droneY += moveVertical * MOVE_SPEED;
            droneZ += (fZ + sZ) * MOVE_SPEED;
        }

        // 6. ドローン本体の座標同期
        droneBodyEntity.xo = droneBodyEntity.getX();
        droneBodyEntity.yo = droneBodyEntity.getY();
        droneBodyEntity.zo = droneBodyEntity.getZ();
        droneBodyEntity.setPos(droneX, droneY, droneZ);

        droneBodyEntity.setYRot(currentYaw);
        droneBodyEntity.setXRot(currentPitch);
        droneBodyEntity.yRotO = mc.player.yRotO;
        droneBodyEntity.xRotO = mc.player.xRotO;

        // 7. カメラ（視点）の追従計算
        float yawRad = currentYaw * ((float)Math.PI / 180F);
        float pitchRad = currentPitch * ((float)Math.PI / 180F);

        double camX = droneX - (Mth.sin(yawRad) * Mth.cos(pitchRad) * CAMERA_BACK_OFFSET);
        double camZ = droneZ + (Mth.cos(yawRad) * Mth.cos(pitchRad) * CAMERA_BACK_OFFSET);
        double camY = droneY - (Mth.sin(pitchRad) * CAMERA_BACK_OFFSET) + CAMERA_UP_OFFSET;

        cameraDummy.xo = cameraDummy.getX();
        cameraDummy.yo = cameraDummy.getY();
        cameraDummy.zo = cameraDummy.getZ();
        cameraDummy.setPos(camX, camY, camZ);

        cameraDummy.setYRot(currentYaw + 180.0F);
        cameraDummy.setXRot(currentPitch);
        cameraDummy.yRotO = mc.player.yRotO + 180.0F;
        cameraDummy.xRotO = mc.player.xRotO;

        // 8. 💡 モード中のみプレイヤー本体の物理ベクトルを完全に静止させる
        if (isDroneMode) {
            mc.player.setDeltaMovement(0, 0, 0);
            mc.player.yBodyRot = originalPlayerYaw;
            mc.player.yBodyRotO = originalPlayerYaw;
            mc.player.yHeadRot = originalPlayerYaw;
            mc.player.yHeadRotO = originalPlayerYaw;
        }
    }
}