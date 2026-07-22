package com.FREEMOD.freemod.block;

import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class CameraBlock extends HorizontalDirectionalBlock {
    public CameraBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    // 【★追加1】プレイヤーがブロックを置く際、向いている方向（壁の向き）をBlockStateにセットする
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // プレイヤーが向いている方向の「反対側（＝設置する壁の方向）」を取得
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, net.minecraft.world.item.ItemStack stack) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            CameraEntity camera = EntityRegister.CAMERA.get().create(serverLevel);
            if (camera != null) {
                // 【★修正2】確定した BlockState から正確な FACING（向き）を取得する
                Direction facing = state.getValue(FACING);

                double x = pos.getX() + 0.5;
                double y = pos.getY() + 0.5;
                double z = pos.getZ() + 0.5;

                camera.moveTo(
                        x,
                        y,
                        z,
                        facing.toYRot(), // BlockStateの向き角度をそのまま設定
                        0.0F
                );

                serverLevel.addFreshEntity(camera);
            }
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide()) {
            level.getEntitiesOfClass(CameraEntity.class, new net.minecraft.world.phys.AABB(pos))
                    .forEach(net.minecraft.world.entity.Entity::discard);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public net.minecraft.world.level.block.RenderShape getRenderShape(BlockState state) {
        return net.minecraft.world.level.block.RenderShape.INVISIBLE;
    }

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}