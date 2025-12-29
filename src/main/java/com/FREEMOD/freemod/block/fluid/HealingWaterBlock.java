package com.FREEMOD.freemod.block.fluid;

import com.FREEMOD.freemod.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class HealingWaterBlock extends LiquidBlock {
    private static final int DURATION = 100; // 5秒分
    private static final int THRESHOLD = 40; // 残り2秒を切ったら更新

    public HealingWaterBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties);
    }

    @Override
    public void entityInside(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        // 独自エフェクトを取得（YOUR_REGISTER はご自身の登録クラスに置き換えてください）
        MobEffectInstance current = player.getEffect(ModEffects.HEALING_EFFECT.get());

        // エフェクトがない、または残り時間が短い場合のみ更新（ここが軽量化のキモ）
        if (current == null || current.getDuration() < THRESHOLD) {
            player.addEffect(new MobEffectInstance(
                    ModEffects.HEALING_EFFECT.get(),
                    DURATION,
                    0,
                    true, // 環境由来（ビーコン等と同じ扱い）
                    false, // パーティクルを消す場合はfalse
                    true  // アイコンを表示
            ));
        }
    }
}
