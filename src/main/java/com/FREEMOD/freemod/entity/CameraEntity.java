package com.FREEMOD.freemod.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.network.NetworkHooks;

public class CameraEntity extends Entity {

    private float lockYaw;

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

        // 💡 ここを追加：クライアントのMinecraftインスタンスを取得
        Minecraft mc = Minecraft.getInstance();

        // mc が null でないこと、およびプレイヤーが存在することを確認（マルチプレイ等でのクラッシュ防止）
        if (mc != null && mc.player != null && mc.getCameraEntity() == this) {

            float deltaYaw = Mth.wrapDegrees(
                    mc.player.getYRot() - lockYaw);

            deltaYaw = Mth.clamp(deltaYaw, -90, 90);

            setYRot(lockYaw + deltaYaw);

            setXRot(Mth.clamp(
                    mc.player.getXRot(),
                    -60,
                    60));
        }
    }

    @Override protected void defineSynchedData() {}


    @Override
    public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override
    public boolean isInvisible() { return true; }
}