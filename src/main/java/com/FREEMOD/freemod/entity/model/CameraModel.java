package com.FREEMOD.freemod.entity.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
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

public class CameraModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart root;
	private final ModelPart beacon;
	private final ModelPart beacon_right;
	private final ModelPart base;
	private final ModelPart arm;
	private final ModelPart camera;
	private final ModelPart cameraCube; // 【追加】実体キューブ用

	public CameraModel(ModelPart root) {
		this.root = root.getChild("root");
		this.beacon = this.root.getChild("beacon");
		this.beacon_right = this.beacon.getChild("beacon_right");
		this.base = this.root.getChild("base");
		this.arm = this.root.getChild("arm");
		this.camera = this.arm.getChild("camera");

		// 【追加】camera グループの中にある cube_r8 を取得
		this.cameraCube = this.camera.getChild("cube_r8");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition beacon = root.addOrReplaceChild("beacon", CubeListBuilder.create().texOffs(52, 40).addBox(-5.0F, -14.0F, 3.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-8.0F, -14.0F, 2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(32, 51).addBox(-8.0F, -16.0F, 2.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition beacon_right = beacon.addOrReplaceChild("beacon_right", CubeListBuilder.create().texOffs(44, 51).addBox(-8.0F, -15.0F, 2.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(32, 32).addBox(-11.0F, -15.0F, 5.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(32, 43).addBox(-7.0F, -16.0F, 10.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(44, 43).addBox(-10.0F, -16.0F, 10.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -7.0F));

		PartDefinition arm = root.addOrReplaceChild("arm", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition cube_r1 = arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(10, 56).addBox(-4.0F, 4.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 48).addBox(3.0F, 1.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(50, 55).addBox(-4.0F, 4.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = arm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 55).addBox(-6.0F, 3.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = arm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 54).addBox(-6.0F, 3.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = arm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(12, 48).addBox(3.0F, 1.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r6 = arm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(42, 55).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.0436F, 0.0F));

		PartDefinition cube_r7 = arm.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(32, 40).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition camera = arm.addOrReplaceChild("camera", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition cube_r8 = camera.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// 1. アーム（左右旋回）
		this.arm.yRot = netHeadYaw * ((float) Math.PI / 180F);

		// 2. カメラ本体（上下チルト）
		float pitchRadian = headPitch * ((float) Math.PI / 180F);

		// 【修正】親グループ(camera)だけに角度を適用します
		// 子要素(cameraCube)への二重適用を削除することで、正確な 1:1 の角度で動くようになります
		this.camera.xRot = pitchRadian;

        /*
        // 以下の二重適用処理は削除（コメントアウト）します
        if (this.cameraCube != null) {
            this.cameraCube.xRot = pitchRadian;
        }
        */
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}