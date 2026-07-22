package com.FREEMOD.freemod.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class CameraEntity extends Entity {

    // 【★追加】クライアントとサーバー間で lockYaw を同期するためのデータキー
    private static final EntityDataAccessor<Float> DATA_LOCK_YAW =
            SynchedEntityData.defineId(CameraEntity.class, EntityDataSerializers.FLOAT);

    private float cameraYawOffset = 0;
    private float cameraPitchOffset = 0;
    private double lastMouseX;
    private double lastMouseY;
    private boolean firstTick = true;

    public CameraEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
        // 【★追加】同期データの初期化
        this.entityData.define(DATA_LOCK_YAW, 0.0F);
    }

    @Override
    public void moveTo(double x, double y, double z, float yaw, float pitch) {
        super.moveTo(x, y, z, yaw, pitch);
        this.setLockYaw(yaw);
    }

    // 【★追加】lockYaw のセッター/ゲッター（SynchedEntityData を経由）
    public void setLockYaw(float yaw) {
        this.entityData.set(DATA_LOCK_YAW, yaw);
    }

    public float getLockYaw() {
        return this.entityData.get(DATA_LOCK_YAW);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("LockYaw", getLockYaw());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        float yaw = tag.getFloat("LockYaw");
        setLockYaw(yaw);
        setYRot(yaw);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide()) return;

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;
        if (mc.getCameraEntity() != this) return;

        double mouseX = mc.mouseHandler.xpos();
        double mouseY = mc.mouseHandler.ypos();

        if (firstTick) {
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            firstTick = false;

            // 覗いた瞬間にオフセットをゼロにし、プレイヤーの首をカメラの向き(getLockYaw)に同期
            this.cameraYawOffset = 0.0F;
            this.cameraPitchOffset = 0.0F;
            mc.player.setYRot(getLockYaw());
            mc.player.setXRot(0.0F);
            return;
        }

        double deltaX = mouseX - lastMouseX;
        double deltaY = mouseY - lastMouseY;

        lastMouseX = mouseX;
        lastMouseY = mouseY;

        float sensitivity = (float)mc.options.sensitivity * 0.18F;

        cameraYawOffset += deltaX * sensitivity;
        cameraPitchOffset += deltaY * sensitivity;

        // 左右は自由回転（360度 wrap）
        cameraYawOffset = Mth.wrapDegrees(cameraYawOffset);

        // 上下のチルト角度制限（上: -45F, 下: 75F）
        cameraPitchOffset = Mth.clamp(cameraPitchOffset, -45.0F, 75.0F);

        float finalYaw = Mth.wrapDegrees(getLockYaw() + cameraYawOffset);

        setYRot(finalYaw);
        yRotO = finalYaw;

        setXRot(cameraPitchOffset);
        xRotO = cameraPitchOffset;
    }

    public void resetFirstTick() {
        this.firstTick = true;
    }

    public float getYawOffset() {
        return cameraYawOffset;
    }

    public float getPitchOffset() {
        return cameraPitchOffset;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isInvisible() {
        return true;
    }
}