package com.FREEMOD.freemod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ParalyzeEffect extends MobEffect {
    public ParalyzeEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide) {
            // 毎ティック、位置を完全に固定（ノックバックや慣性による移動をロック）
            pLivingEntity.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        // 10秒間、毎ティック（1秒間に20回）確実に移動ロックとAI停止の判定を行うため true
        return true;
    }
}