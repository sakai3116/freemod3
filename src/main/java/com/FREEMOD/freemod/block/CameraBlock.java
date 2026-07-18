package com.FREEMOD.freemod.block;

import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CameraBlock extends Block {
    public CameraBlock(Properties properties) {
        super(properties);
    }

    // ブロックがプレイヤーによって設置された時に呼び出されるバニラのメソッド
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, net.minecraft.world.item.ItemStack stack) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            CameraEntity camera = EntityRegister.CAMERA.get().create(serverLevel);
            if (camera != null) {
                float blockYaw = 0.0F;
                if (placer != null) {
                    // 設置したプレイヤーの水平方向（南:0, 西:90, 北:180, 東:270）を取得
                    Direction facing = placer.getDirection();
                    blockYaw = facing.toYRot();
                }

                // エンティティをブロックの中心に、固定された方角（blockYaw）でスポーンさせる
                camera.moveTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, blockYaw, 0.0F);
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
}