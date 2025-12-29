package com.FREEMOD.freemod.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class HealingWaterBlock extends LiquidBlock {
    public HealingWaterBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties);
    }

    @Override
    public void entityInside(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (!level.isClientSide && entity instanceof Player player) {

            // 1. ハート（HP）の回復
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(1.0F); // 0.5個分回復（高速にするなら数値を上げる）
            }

            // 2. 満腹度の回復
            if (player.getFoodData().needsFood()) {
                // 満腹度を1、隠し満腹度を0.5追加
                player.getFoodData().eat(1, 0.5F);
            }
        }
    }
}
