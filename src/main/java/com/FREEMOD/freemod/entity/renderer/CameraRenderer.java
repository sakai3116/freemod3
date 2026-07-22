package com.FREEMOD.freemod.entity.renderer;

import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.entity.model.CameraModel;
import com.FREEMOD.freemod.item.CameraControllerItem;
import com.FREEMOD.freemod.main.FreeMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CameraRenderer extends EntityRenderer<CameraEntity> {
    public static final ModelLayerLocation CAMERA_LAYER = new ModelLayerLocation(new ResourceLocation(FreeMod.MOD_ID, "camera"), "main");
    private static final ResourceLocation TEXTURE = new ResourceLocation(FreeMod.MOD_ID, "textures/entity/camera_block.png");

    private final CameraModel<CameraEntity> model;

    public CameraRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CameraModel<>(context.bakeLayer(CAMERA_LAYER));
    }

    @Override
    public void render(CameraEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 【修正】entityYaw(全体角度) ではなく、壁に固定された基準角度(lockYaw) のみでモデル全体を回転させる
        float baseYaw = entity.getLockYaw();
        poseStack.mulPose(com.mojang.math.Vector3f.YP.rotationDegrees(-baseYaw));

        // Blockbenchモデルの上下反転補正
        poseStack.mulPose(com.mojang.math.Vector3f.XP.rotationDegrees(180.0F));
        poseStack.translate(0.0D, -0.8D, 0.0D); // 位置の微調整

        // 1. 相対首振り角度（Offset）のみを取得
        float yawOffset = entity.getYawOffset();
        float pitchOffset = entity.getPitchOffset();

        // 2. モデルのアニメーション設定に渡す
        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, yawOffset, pitchOffset);

        // 3. 通常のモデル描画
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CameraEntity entity) {
        return TEXTURE;
    }
}