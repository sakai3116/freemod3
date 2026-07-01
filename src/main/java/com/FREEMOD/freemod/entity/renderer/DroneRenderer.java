package com.FREEMOD.freemod.entity.renderer;

import com.FREEMOD.freemod.entity.DroneEntity;
import com.FREEMOD.freemod.entity.model.DroneModel;
import com.FREEMOD.freemod.main.FreeMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f; // 💡 1.18.2の回転用インポート
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture; // 💡 オーバーレイのインポート
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class DroneRenderer extends EntityRenderer<DroneEntity> {

    public static final ModelLayerLocation DRONE_LAYER =
            new ModelLayerLocation(new ResourceLocation(FreeMod.MOD_ID, "drone"), "main");

    private final DroneModel model;
    private static final ResourceLocation DRONE_TEXTURE = new ResourceLocation(FreeMod.MOD_ID, "textures/entity/drone.png");

    public DroneRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DroneModel(context.bakeLayer(DRONE_LAYER));
    }

    @Override
    public void render(DroneEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // 💡 座標調整：中心点のズレがなければ 0.0D でOKです
        poseStack.translate(0.0D, 0.0D, 0.0D);

        // 💡 プレイヤーの視線（Yaw/Pitch）に合わせてドローン本体を回転
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));

        // Blockbenchモデルの上下反転とサイズ調整（1.0Fは100%サイズ）
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        // プロペラのアニメーションを進行させる処理 (ageInTicksに値を渡す)
        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);

        // 💡 暗闇バグ対策：packedLight の代わりに「15728880」を入れると、夜間でもドローンが明るく綺麗に見えます
        var vertexConsumer = bufferSource.getBuffer(this.model.renderType(DRONE_TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DroneEntity entity) {
        return DRONE_TEXTURE;
    }
}