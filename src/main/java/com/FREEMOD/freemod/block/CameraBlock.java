package com.FREEMOD.freemod.block;

import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public class CameraBlock extends HorizontalDirectionalBlock {
    public CameraBlock(Properties properties) {
        // 1. 親クラス（HorizontalDirectionalBlock）に properties を渡す
        super(properties);

        // 2. ブロックのデフォルト状態として、向き（FACING）を北（NORTH）に初期設定する
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    // ブロックがプレイヤーによって設置された時に呼び出されるバニラのメソッド
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, net.minecraft.world.item.ItemStack stack) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            CameraEntity camera = EntityRegister.CAMERA.get().create(serverLevel);
            if (camera != null) {
                // デフォルトの向き（placerがnullの場合の安全策）
                Direction facing = Direction.NORTH;
                if (placer != null) {
                    // 設置したプレイヤーの水平方向の逆を取得
                    facing = placer.getDirection().getOpposite();
                }

                // 座標計算の追加
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 0.62;
                double z = pos.getZ() + 0.5;

                double offset = 0.36;

                x += facing.getStepX() * offset;
                z += facing.getStepZ() * offset;

                // 最後の引数を「0.0F」に修正（float型を明示）
                camera.moveTo(
                        x,
                        y,
                        z,
                        facing.toYRot(),
                        0.0F
                );

                serverLevel.addFreshEntity(camera);
            }
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    // 以前のonPlaceメソッドは暴発の原因になるため、まるごと消去するか
    // super.onPlace だけを呼び出す形にして中身を空にしてください
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
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
        // ブロックにFACING（向き）のプロパティを登録する
        builder.add(FACING);
    }
}