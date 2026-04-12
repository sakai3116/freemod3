//長距離探知、半分の距離で射撃と高精度、射撃間隔あり
package com.FREEMOD.freemod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import java.util.EnumSet;

public class FastShotSkeleton extends Skeleton {

    public FastShotSkeleton(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 100.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // バニラの攻撃AIを削除 [cite: 135]
        this.goalSelector.getAvailableGoals().removeIf(goal ->
                goal.getGoal() instanceof net.minecraft.world.entity.ai.goal.RangedBowAttackGoal);

        // 自作の「常時構え・即射」ゴールを追加
        this.goalSelector.addGoal(2, new InstantSniperGoal(this));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        AbstractArrow abstractarrow = ProjectileUtil.getMobArrow(this,
                this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW))), 1.0F);

        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = target.getZ() - this.getZ();

        // 精度(inaccuracy) 0.0F で完璧な狙い。弾道補正なし。
        abstractarrow.shoot(d0, d1, d2, 4.0F, 0.0F);

        this.playSound(net.minecraft.sounds.SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrow);
    }

    @Override
    protected boolean isSunBurnTick() {
        return false; // 日光耐性 [cite: 138]
    }

    /**
     * 常時弓を構え、視界に入った瞬間にバニラ間隔で射撃するAI
     */
    static class InstantSniperGoal extends Goal {
        private final FastShotSkeleton mob;
        private int attackCooldown = 0;

        public InstantSniperGoal(FastShotSkeleton mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.mob.getTarget() != null && this.mob.isHolding(Items.BOW);
        }

        @Override
        public void start() {
            super.start();
            // ターゲットを見つけた瞬間から「アグレッシブ状態（弓を構えるポーズ）」にする
            this.mob.setAggressive(true);
        }

        @Override
        public void stop() {
            super.stop();
            // ターゲットを見失ったら構えを解く（常時構えにしたい場合はここをコメントアウト）
            this.mob.setAggressive(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) return;

            // 常にターゲットを向く
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

            // 常に「弓を引いているフラグ」を立てることで、アニメーション上の予備動作をなくす
            this.mob.setAggressive(true);

            double distanceSq = this.mob.distanceToSqr(target);
            boolean canSee = this.mob.getSensing().hasLineOfSight(target);

            if (canSee && distanceSq <= Math.pow(15.0D, 2)) {
                if (attackCooldown <= 0) {
                    // 即座に発射
                    this.mob.performRangedAttack(target, 1.0F);
                    // 射撃間隔（バニラ通り：約1〜2秒）
                    this.attackCooldown = 20 + this.mob.getRandom().nextInt(20);
                }
            }

            if (attackCooldown > 0) {
                this.attackCooldown--;
            }
        }
    }
}