package com.FREEMOD.freemod.blockentity;

import com.FREEMOD.freemod.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CustomChestBlockEntity extends ChestBlockEntity {
    public CustomChestBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.CUSTOM_CHEST_BLOCK_ENTITY.get(), pos, state);
    }
}
