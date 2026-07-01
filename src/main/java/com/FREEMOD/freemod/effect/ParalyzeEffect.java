package com.FREEMOD.freemod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ParalyzeEffect extends MobEffect {
    public ParalyzeEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide) {
            // pLivingEntity（エフェクトを受けている敵Mob自身）の動きを完全にロックする
            pLivingEntity.setDeltaMovement(Vec3.ZERO);

            // 同時に、敵Mob自身を発光（ウォールハック状態）させる[cite: 5]
            // エフェクトの更新に合わせ、1秒間（20ティック）の発光を上書きし続けます
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0, false, true));
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        // 毎ティック確実にフリーズと発光の処理を行うため true
        return true;
    }
}