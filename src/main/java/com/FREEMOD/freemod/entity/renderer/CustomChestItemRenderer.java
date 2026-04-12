package com.FREEMOD.freemod.entity.renderer; // ご自身の環境に合わせてください

import com.FREEMOD.freemod.blockentity.CustomChestBlockEntity;
import com.FREEMOD.freemod.register.BlockRegister;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class CustomChestItemRenderer extends BlockEntityWithoutLevelRenderer {

    // 描画用に一時的に使用する「ダミーのチェスト」
    private final CustomChestBlockEntity blockEntity;

    public CustomChestItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        // ダミーチェストを初期化
        this.blockEntity = new CustomChestBlockEntity(BlockPos.ZERO, BlockRegister.CUSTOM_CHEST_BLOCK.get().defaultBlockState());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        // アイテムとして描画される際に、設置用のレンダラー（CustomChestRenderer）を呼び出して3D描画する
        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.blockEntity, poseStack, buffer, packedLight, packedOverlay);
    }
}