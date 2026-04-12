package com.FREEMOD.freemod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;

public class FastShotSkeleton extends Skeleton {

    public FastShotSkeleton(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
    }

    // FastShotSkeleton.java の修正
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                // オーバーキル級のダメージ（例: 100ダメージ = ハート50個分）
                .add(Attributes.ATTACK_DAMAGE, 100.0D);
    }

    @Override
    protected void registerGoals() {
        // バニラのスケルトンAI（通常の弓攻撃など）を一旦すべて読み込む
        super.registerGoals();

        // 【即射の肝】既存の弓攻撃AIを削除して、設定を書き換えたものを再登録する
        this.goalSelector.getAvailableGoals().removeIf(goal ->
                goal.getGoal() instanceof RangedBowAttackGoal);

        // 第3引数(attackIntervalMin): 1（ほぼ0秒で次を撃つ）
        // 第4引数(maxAttackDistance): 15.0F（射程距離）
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0D, 1, 15.0F));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        // 矢の生成処理（バニラの挙動を流用）
        super.performRangedAttack(target, velocity);

        // 注意: super を呼ぶとバニラの矢が飛んでいきます。
        // 完全に制御したい場合は、ここで自前で Arrow エンティティを生成し、
        // arrow.setBaseDamage(100.0D) のように設定して射出します。
    }

    // 視界に入った瞬間の判定をよりシビアにする場合（任意）
//    @Override
//    public boolean canSee(net.minecraft.world.entity.Entity entity) {
//        return super.canSee(entity);
//    }

    // 日光で燃えたくない場合はここを false に（ゾンビのコードと同じ）
    @Override
    protected boolean isSunBurnTick() {
        return false;
    }
}