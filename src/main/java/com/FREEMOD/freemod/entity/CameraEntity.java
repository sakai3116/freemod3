package com.FREEMOD.freemod.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.network.NetworkHooks;

public class CameraEntity extends Entity {
    public CameraEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true; // 壁を突き抜ける/物理演算を無効化
    }

    @Override
    public void tick() {
        // 動かないように毎ジャック重力を無視（あるいは何もしない）
        super.tick();
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {}

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // プレイヤーの視点（Spectator等）から見て完全に透明にする
    @Override
    public boolean isInvisible() {
        return true;
    }
}