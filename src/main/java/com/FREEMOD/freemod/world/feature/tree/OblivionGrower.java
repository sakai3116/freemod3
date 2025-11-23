package com.FREEMOD.freemod.world.feature.tree;

import com.FREEMOD.freemod.register.ConfiguredFeatureRegister;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class OblivionGrower extends AbstractTreeGrower {
    @Override
    protected @Nullable Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean b) {
        return ConfiguredFeatureRegister.OBLIVION_TREE.getHolder().get();
    }
}
