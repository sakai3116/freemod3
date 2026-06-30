package com.FREEMOD.freemod.main.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.CameraType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.ChatFormatting;

public class ClientCameraHandler {
    private static boolean isDroneMode = false;
    private static Entity cameraDummy = null;
    private static Entity originalCameraEntity = null;

    private static float originalPlayerYaw = 0.0F;
    private static float originalPlayerPitch = 0.0F;

    private static double droneX = 0.0D;
    private static double droneY = 0.0D;
    private static double droneZ = 0.0D;

    private static final double MOVE_SPEED = 0.12D;

    // 💡 制限用の定数を変更
    private static final double MAX_DISTANCE = 100.0D; // 💡 200から100ブロックに制限を縮小
    private static final long MAX_FLIGHT_TIME_MS = 20000L; // 飛行可能時間（20秒）
    private static long flightStartTime = 0L;

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
            }

            if (mc.options != null) {
                mc.options.setCameraType(CameraType.FIRST_PERSON);
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

        // 1. 時間制限（バッテリー）のチェック
        long elapsedMs = System.currentTimeMillis() - flightStartTime;
        if (elapsedMs >= MAX_FLIGHT_TIME_MS) {
            mc.player.displayClientMessage(new TextComponent("バッテリー切れ：ドローンが強制シャットダウンしました").withStyle(ChatFormatting.RED), false);
            toggleDroneMode();
            return;
        }

        // 2. 距離制限（電波届く範囲）のチェック
        double distanceSq = mc.player.distanceToSqr(droneX, droneY, droneZ);
        if (distanceSq > MAX_DISTANCE * MAX_DISTANCE) {
            mc.player.displayClientMessage(new TextComponent("通信途絶：最大通信距離（100m）を超えました").withStyle(ChatFormatting.RED), false);
            toggleDroneMode();
            return;
        }

        // 💡 3. 【新機能】現在の距離と残り時間を画面下（アクションバー）に常時表示
        double currentDistance = Math.sqrt(distanceSq); // 平方根を計算して実際のメートル（マス数）にする
        long remainingSeconds = (MAX_FLIGHT_TIME_MS - elapsedMs) / 1000L;

        // 表示する文字の作成 (例: "【ドローンステータス】 距離: 45.2m / 100m | 残り時間: 15秒")
        String statusText = String.format("【ドローン】 距離: %.1fm / 100m | 残り時間: %d秒", currentDistance, remainingSeconds);
        TextComponent message = new TextComponent(statusText);

        // 残り時間が少なくなったら文字を金色(警告色)にする
        if (remainingSeconds <= 5) {
            message.withStyle(ChatFormatting.GOLD);
        } else {
            message.withStyle(ChatFormatting.AQUA);
        }
        // 第二引数を「true」にすることで、チャット欄ではなく画面下のアクションバーに固定表示させます
        mc.player.displayClientMessage(message, true);


        // 4. マウス操作によるプレイヤーの回転を取得
        float currentYaw = mc.player.getYRot();
        float currentPitch = mc.player.getXRot();

        // 5. キー入力によるドローンの移動計算
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

        // 6. カメラ（ダミー）の座標と角度を更新
        cameraDummy.xo = cameraDummy.getX();
        cameraDummy.yo = cameraDummy.getY();
        cameraDummy.zo = cameraDummy.getZ();

        cameraDummy.setPos(droneX, droneY, droneZ);

        cameraDummy.setYRot(currentYaw + 180.0F);
        cameraDummy.setXRot(currentPitch);
        cameraDummy.yRotO = mc.player.yRotO + 180.0F;
        cameraDummy.xRotO = mc.player.xRotO;

        // 7. 下にいるプレイヤー本体の同期
        mc.player.setDeltaMovement(0, 0, 0);
        mc.player.yBodyRot = originalPlayerYaw;
        mc.player.yBodyRotO = originalPlayerYaw;
        mc.player.yHeadRot = originalPlayerYaw;
        mc.player.yHeadRotO = originalPlayerYaw;
    }
}