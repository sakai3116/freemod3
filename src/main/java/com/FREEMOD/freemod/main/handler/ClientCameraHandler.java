package com.FREEMOD.freemod.main.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.TextComponent;

public class ClientCameraHandler {
    private static boolean isDroneMode = false;
    private static Entity cameraDummy = null;
    private static Entity originalCameraEntity = null;

    private static float originalPlayerYaw = 0.0F;
    private static float originalPlayerPitch = 0.0F;

    public static boolean isDroneMode() {
        return isDroneMode;
    }

    public static void toggleDroneMode() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (!isDroneMode) {
            isDroneMode = true;
            originalCameraEntity = mc.getCameraEntity();
            originalPlayerYaw = mc.player.getYRot();
            originalPlayerPitch = mc.player.getXRot();

            cameraDummy = EntityType.MARKER.create(mc.level);
            if (cameraDummy != null) {
                cameraDummy.setPos(mc.player.getX(), mc.player.getY() + 20.0, mc.player.getZ());
                cameraDummy.noPhysics = true;

                // 初期の向きを設定
                cameraDummy.setYRot(originalPlayerYaw);
                cameraDummy.setXRot(originalPlayerPitch);
                // xo, yo, zo, yRotO, xRotO (Oが付く補間用変数) も同期
                cameraDummy.yo = cameraDummy.getY();
                cameraDummy.yRotO = originalPlayerYaw;
                cameraDummy.xRotO = originalPlayerPitch;

                mc.setCameraEntity(cameraDummy);
            }
            mc.player.displayClientMessage(new TextComponent("ドローン視点：起動"), true);

        } else {
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
     * 💡 毎フレーム（Renderバス）で呼び出される同期ロジック
     */
    public static void onRenderTick(float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (!isDroneMode || cameraDummy == null || mc.player == null) return;

        // 💡 Render のタイミングで、プレイヤーの回転をカメラにコピー
        // O(前フレームのデータ)もコピーすることで、補間のブレを完全に消す
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

        // プレイヤーの体のモデル描画を固定
        mc.player.yBodyRot = originalPlayerYaw;
        mc.player.yBodyRotO = originalPlayerYaw;
        mc.player.yHeadRot = originalPlayerYaw;
        mc.player.yHeadRotO = originalPlayerYaw;
    }
}