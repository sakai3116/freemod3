package com.FREEMOD.freemod.block.fluid;

import com.FREEMOD.freemod.register.EffectRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AcidLiquidBlock extends LiquidBlock {

    private static final int EFFECT_DURATION = 120;
    private static final int REFRESH_THRESHOLD = 40;

    public AcidLiquidBlock(Supplier<? extends FlowingFluid> fluid, Properties props) {
        super(fluid, props);
    }

    @Override
    public void entityInside(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof LivingEntity living)) return;

        MobEffectInstance cur = living.getEffect(EffectRegister.ACID_EFFECT.get());
        if (cur == null || cur.getDuration() < REFRESH_THRESHOLD) {
            living.addEffect(new MobEffectInstance(
                    EffectRegister.ACID_EFFECT.get(),
                    EFFECT_DURATION,
                    0,
                    true,
                    true,
                    true
            ));
        }
    }
}