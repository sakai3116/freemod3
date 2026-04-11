package com.FREEMOD.freemod.block;

import com.FREEMOD.freemod.blockentity.CustomChestBlockEntity;
import com.FREEMOD.freemod.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class CustomChestBlock extends ChestBlock {
    public CustomChestBlock(BlockBehaviour.Properties properties) {
        // 第2引数には、このチェストに紐づくBlockEntityTypeのSupplierを渡します
        super(properties, () -> BlockEntityRegister.CUSTOM_CHEST_BLOCK_ENTITY.get());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CustomChestBlockEntity(pos, state);
    }

    // ★追加1：チェストを設置する時の処理を上書き
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        // 周りを確認するバニラの処理を消し、「プレイヤーの反対向き」かつ「常にシングルチェスト」として設置する
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(TYPE, ChestType.SINGLE)
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    // ★追加2：隣にブロックが置かれたり壊されたりした時の処理を上書き
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        // 水没処理だけは残しておく
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        // バニラの「隣のチェストと繋がる」処理を呼ばずに、現在の状態（シングルのまま）を返す
        return state;
    }
}