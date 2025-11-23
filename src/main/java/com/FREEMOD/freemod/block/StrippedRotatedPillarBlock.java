package com.FREEMOD.freemod.block;

import com.FREEMOD.freemod.register.BlockRegister;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class StrippedRotatedPillarBlock extends RotatedPillarBlock {
    public StrippedRotatedPillarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof AxeItem) {
            if (state.is(BlockRegister.OBLIVION_LOG.get())) {
                world.setBlock(pos, BlockRegister.STRIPPED_OBLIVION_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)), 11);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
            if (state.is(BlockRegister.OBLIVION_WOOD.get())) {
                world.setBlock(pos, BlockRegister.STRIPPED_OBLIVION_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)), 11);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }

        return super.use(state, world, pos, player, hand, hit);
    }
}
