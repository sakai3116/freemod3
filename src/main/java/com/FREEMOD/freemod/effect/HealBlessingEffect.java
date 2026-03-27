package com.FREEMOD.freemod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HealBlessingEffect extends MobEffect {
    public HealBlessingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            // 毎チック実行される（高速回復）
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(1.0F); // 必要に応じて調整
            }
            if (player.getFoodData().needsFood()) {
                player.getFoodData().eat(1, 0.5F);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
