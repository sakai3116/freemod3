package com.FREEMOD.freemod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;

public class DroneEntity extends Boat {

    public DroneEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
        this.noPhysics = false; // 必要に応じて壁抜け(true)にする
    }

    @Override
    public void tick() {
        super.tick();

        // ドローンなので重力を完全に無効化（ぷかぷか浮く状態にする）
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.0, 1.0));

        // クライアント側でのみ視点ロックとキー入力を受ける処理へ（後述のイベントと連動）
    }

    // ボート特有の「水面に浮かぶ処理」などをオーバーライドして無効化する
//    @Override
//    protected void checkInsideBlocks(float[] p_20049_) {}

    @Override
    public boolean isNoGravity() {
        return true;
    }
}