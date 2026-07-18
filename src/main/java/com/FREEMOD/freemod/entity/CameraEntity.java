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

    private float initialYaw = Float.NaN;

    public CameraEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide()) {
            Minecraft mc = Minecraft.getInstance();

            // ★【修正ポイント：ここを最上部に移動】
            // カメラ視点に入っているかどう方に関わらず、このエンティティがクライアントに読み込まれた最初の1フレーム目で、
            // サーバー側から送られてきた不動の設置角度（getYRot）を確実に initialYaw に焼き付けてロックする
            if (Float.isNaN(this.initialYaw)) {
                this.initialYaw = Mth.wrapDegrees(this.getYRot());
            }

            if (mc.getCameraEntity() == this && mc.player != null) {
                Player player = mc.player;

                this.xRotO = this.getXRot();
                this.yRotO = this.getYRot();

                // 1. 左右の旋回制限（Yaw）の計算：正面を中心に左右に均等に90度（合計180度）
                float currentYaw = player.getYRot();
                float yawDifference = Mth.wrapDegrees(currentYaw - this.initialYaw);
                float clampedDifference = Mth.clamp(yawDifference, -90.0F, 90.0F);
                float finalYaw = this.initialYaw + clampedDifference;

                // 2. 上下の旋回制限（Pitch）の計算：上下チルト60度
                float finalPitch = Mth.clamp(player.getXRot(), -60.0F, 60.0F);

                // 角度の完全同期
                this.setYRot(finalYaw);
                this.setXRot(finalPitch);
                player.setYRot(finalYaw);
                player.setXRot(finalPitch);
            }
            // elseブロック（if (Float.isNaN(this.initialYaw)) のリセット処理）は
            // 毎回初期化されてプレイヤーの視点に引っ張られる原因になるため、完全に消去します
        }
    }

    @Override protected void defineSynchedData() {}
    @Override protected void readAdditionalSaveData(CompoundTag compound) {}
    @Override protected void addAdditionalSaveData(CompoundTag compound) {}

    @Override
    public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
    @Override
    public boolean isInvisible() { return true; }
}