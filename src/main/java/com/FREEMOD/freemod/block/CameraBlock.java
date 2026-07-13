package com.FREEMOD.freemod.block;

import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CameraBlock extends Block {
    public CameraBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            // ブロックの中心に視点用のエンティティをスポーンさせる
            CameraEntity camera = EntityRegister.CAMERA.get().create(serverLevel);
            if (camera != null) {
                camera.moveTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0F, 0.0F);
                serverLevel.addFreshEntity(camera);
            }
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide()) {
            // ブロックが壊されたら、その位置にあるカメラエンティティを消去する
            level.getEntitiesOfClass(CameraEntity.class, new net.minecraft.world.phys.AABB(pos))
                    .forEach(net.minecraft.world.entity.Entity::discard);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}