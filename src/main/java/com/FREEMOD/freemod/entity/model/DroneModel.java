package com.FREEMOD.freemod.entity.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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

// 💡 EntityModel ではなく ListModel を継承するように書き換えます
// 💡 ListModelを正しく継承
public class DroneModel extends net.minecraft.client.model.ListModel<com.FREEMOD.freemod.entity.DroneEntity> {

	// ⭕️ レンダラー側のコード（DroneRenderer.java 218行目）
	public static final ModelLayerLocation DRONE_LAYER = new ModelLayerLocation(new ResourceLocation(FreeMod.MOD_ID, "drone"), "main");

	private final ModelPart body;
	// 💡 アニメーション用に4つのプロペラ（羽）を個別に保持します
	private final ModelPart 羽FR;
	private final ModelPart 羽FL;
	private final ModelPart 羽RR;
	private final ModelPart 羽RL;

	public DroneModel(ModelPart root) {
		// 最上層のbodyを取得
		this.body = root.getChild("body");

		// 💡 【重要・クラッシュ修正】
		// createBodyLayer() の構造を見ると、「羽」グループは「軸」の子、
		// 「軸」は「支柱」の子ではなく、直接「支柱」と同じ並びで「body」の子になっています。
		// 正しい階層ルート（body -> 軸 -> 各羽）から取得するように修正します。
		ModelPart 軸 = this.body.getChild("軸");

		this.羽FR = 軸.getChild("羽FR");
		this.羽FL = 軸.getChild("羽FL");
		this.羽RR = 軸.getChild("羽RR");
		this.羽RL = 軸.getChild("羽RL");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(-8, -6).addBox(-3.0F, -6.0F, -4.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition 支柱 = body.addOrReplaceChild("支柱", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		支柱.addOrReplaceChild("支柱FL_r1", CubeListBuilder.create().texOffs(-5, -5).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -3.0F, -4.0F, 0.0F, -0.7854F, 0.0F));
		支柱.addOrReplaceChild("支柱RR_r1", CubeListBuilder.create().texOffs(-6, -6).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -3.0F, 7.0F, 0.0F, 0.7854F, 0.0F));
		支柱.addOrReplaceChild("支柱RL_r1", CubeListBuilder.create().texOffs(-6, -6).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -3.0F, 7.0F, 0.0F, -0.7854F, 0.0F));
		支柱.addOrReplaceChild("支柱FR_r1", CubeListBuilder.create().texOffs(-5, -5).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -3.0F, -4.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition 軸 = body.addOrReplaceChild("軸", CubeListBuilder.create().texOffs(2, 1).addBox(-7.0F, -6.0F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 1).addBox(6.0F, -6.0F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 1).addBox(-7.0F, -6.0F, 7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 1).addBox(6.0F, -6.0F, 7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		軸.addOrReplaceChild("羽FR", CubeListBuilder.create().texOffs(-5, -6).addBox(0.0F, 0.0F, -4.0F, 1.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -6.0F, -8.0F));
		軸.addOrReplaceChild("羽FL", CubeListBuilder.create().texOffs(-5, -6).addBox(-1.0F, 0.0F, -4.0F, 1.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -6.0F, -8.0F));
		軸.addOrReplaceChild("羽RR", CubeListBuilder.create().texOffs(-5, -6).addBox(-14.0F, 0.0F, -4.0F, 1.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -6.0F, 8.0F));
		軸.addOrReplaceChild("羽RL", CubeListBuilder.create().texOffs(-5, -6).addBox(13.0F, 0.0F, -4.0F, 1.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -6.0F, 8.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	// 💡 ListModel<DroneEntity> の正しい引数構成で単一の setupAnim メソッドを定義
	@Override
	public void setupAnim(DroneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// ageInTicks を利用して、4枚の羽のY軸（yRot）を一斉に高速回転させる
		float speed = 1.2F;
		this.羽FR.yRot = ageInTicks * speed;
		this.羽FL.yRot = ageInTicks * speed;
		this.羽RR.yRot = ageInTicks * speed;
		this.羽RL.yRot = ageInTicks * speed;
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