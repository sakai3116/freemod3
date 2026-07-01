package com.FREEMOD.freemod.entity.model;

import com.FREEMOD.freemod.entity.DroneEntity;
import com.FREEMOD.freemod.main.FreeMod;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class DroneModel extends net.minecraft.client.model.ListModel<DroneEntity> {

	public static final ModelLayerLocation DRONE_LAYER = new ModelLayerLocation(new ResourceLocation(FreeMod.MOD_ID, "drone"), "main");

	private final ModelPart body;
	private final ModelPart support;
	private final ModelPart shaft;
	private final ModelPart wingFR;
	private final ModelPart wingFL;
	private final ModelPart wingRR;
	private final ModelPart wingRL;

	public DroneModel(ModelPart root) {
		this.body = root.getChild("body");
		this.support = this.body.getChild("support");
		this.shaft = this.support.getChild("shaft");
		this.wingFR = this.shaft.getChild("wingFR");
		this.wingFL = this.shaft.getChild("wingFL");
		this.wingRR = this.shaft.getChild("wingRR");
		this.wingRL = this.shaft.getChild("wingRL");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -4.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition support = body.addOrReplaceChild("support", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		support.addOrReplaceChild("supportFL_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -3.0F, -4.0F, 0.0F, -0.7854F, 0.0F));
		support.addOrReplaceChild("supportRR_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -3.0F, 7.0F, 0.0F, 0.7854F, 0.0F));
		support.addOrReplaceChild("supportRL_r1", CubeListBuilder.create().texOffs(20, 12).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -3.0F, 7.0F, 0.0F, -0.7854F, 0.0F));
		support.addOrReplaceChild("supportFR_r1", CubeListBuilder.create().texOffs(18, 22).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -3.0F, -4.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition shaft = support.addOrReplaceChild("shaft", CubeListBuilder.create()
				.texOffs(32, 8).addBox(-7.0F, -6.0F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(28, 8).addBox(6.0F, -6.0F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(36, 8).addBox(-7.0F, -6.0F, 7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(36, 30).addBox(6.0F, -6.0F, 7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		// 💡 補正ポイント: PartPose.offsetをプロペラの中央に指定し、addBoxをその原点から対称（-0.5Fから展開）に配置します
		// これにより、それぞれの羽が自分の中心軸でブレずに綺麗に回転します
		shaft.addOrReplaceChild("wingFR", CubeListBuilder.create().texOffs(28, 0).addBox(-0.5F, 0.0F, -4.0F, 1.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -6.0F, -7.5F));
		shaft.addOrReplaceChild("wingFL", CubeListBuilder.create().texOffs(0, 31).addBox(-0.5F, 0.0F, -4.0F, 1.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(6.5F, -6.0F, -7.5F));
		shaft.addOrReplaceChild("wingRR", CubeListBuilder.create().texOffs(18, 31).addBox(-0.5F, 0.0F, -4.0F, 1.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(6.5F, -6.0F, 7.5F));
		shaft.addOrReplaceChild("wingRL", CubeListBuilder.create().texOffs(36, 22).addBox(-0.5F, 0.0F, -4.0F, 1.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, -6.0F, 7.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(DroneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float speed = 1.8F; // プロペラの回転スピード
		this.wingFR.yRot = ageInTicks * speed;
		this.wingFL.yRot = ageInTicks * speed;
		this.wingRR.yRot = ageInTicks * speed;
		this.wingRL.yRot = ageInTicks * speed;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.body);
	}
}