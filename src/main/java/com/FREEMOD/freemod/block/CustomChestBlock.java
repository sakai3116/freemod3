package com.FREEMOD.freemod.block;

import com.FREEMOD.freemod.blockentity.CustomChestBlockEntity;
import com.FREEMOD.freemod.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CustomChestBlock extends ChestBlock {
    public CustomChestBlock(BlockBehaviour.Properties properties) {
        // 第2引数には、このチェストに紐づくBlockEntityTypeのSupplierを渡します
        super(properties, () -> BlockEntityRegister.CUSTOM_CHEST_BLOCK_ENTITY.get());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CustomChestBlockEntity(pos, state);
    }
}