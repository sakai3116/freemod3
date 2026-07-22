package com.FREEMOD.freemod.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.network.NetworkHooks;

public class CameraEntity extends Entity {

    private float lockYaw;
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
    public void moveTo(double x,double y,double z,float yaw,float pitch){
        super.moveTo(x,y,z,yaw,pitch);
        this.lockYaw = yaw;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag){
        tag.putFloat("LockYaw",lockYaw);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag){
        lockYaw = tag.getFloat("LockYaw");
        setYRot(lockYaw);
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

            // === 【重要】覗いた瞬間にオフセットをゼロにし、プレイヤーの首をカメラの向き(lockYaw)に強制同期 ===
            this.cameraYawOffset = 0.0F;
            this.cameraPitchOffset = 0.0F;
            mc.player.setYRot(this.lockYaw);
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

        // 左右の旋回角度（必要に応じてコメントアウトを解除して制限できます）
        // cameraYawOffset = Mth.clamp(cameraYawOffset, -45F, 45F);

        // 左右は自由回転（360度 wrap）
        cameraYawOffset = Mth.wrapDegrees(cameraYawOffset);

        // 【修正】上下のチルト角度制限（上: -45F, 下: 75F）
        cameraPitchOffset = Mth.clamp(cameraPitchOffset, -45F, 75F);

        cameraYawOffset = Mth.wrapDegrees(cameraYawOffset);

        cameraPitchOffset = Mth.clamp(cameraPitchOffset, -85F, 85F);

        float finalYaw = Mth.wrapDegrees(lockYaw + cameraYawOffset);

        setYRot(finalYaw);
        yRotO = finalYaw;

        setXRot(cameraPitchOffset);
        xRotO = cameraPitchOffset;
    }

    @Override protected void defineSynchedData() {}

    // コントローラーアイテムから呼び出せるようにpublicにする
    public void resetFirstTick() {
        this.firstTick = true;
    }

    public float getLockYaw() {
        return lockYaw;
    }

    public float getYawOffset() {
        return cameraYawOffset;
    }

    public float getPitchOffset() {
        return cameraPitchOffset;
    }

    @Override
    public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override
    public boolean isInvisible() { return true; }
}