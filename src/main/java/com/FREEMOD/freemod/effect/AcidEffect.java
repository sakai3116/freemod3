package com.FREEMOD.freemod.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AcidEffect extends MobEffect {
    public AcidEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide) {
            // ダメージを与える
            pLivingEntity.hurt(DamageSource.MAGIC, 1.0F);

            // 防御力を低下させる
            AttributeModifier modifier = new AttributeModifier(
                    "Acid Debuff",
                    -0.5 * (pAmplifier + 1),
                    AttributeModifier.Operation.ADDITION
            );
            pLivingEntity.getAttribute(Attributes.ARMOR).addTransientModifier(modifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        // ティックごとに効果を適用
        return true;
    }
}