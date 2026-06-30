//package com.FREEMOD.freemod.entity.model;
//
//import com.FREEMOD.freemod.entity.DroneEntity;
//import net.minecraft.client.model.ListModel;
//import net.minecraft.client.model.geom.ModelPart;
//import net.minecraft.client.model.geom.builders.CubeListBuilder;
//import net.minecraft.client.model.geom.builders.LayerDefinition;
//import net.minecraft.client.model.geom.builders.MeshDefinition;
//import net.minecraft.client.model.geom.builders.PartDefinition;
//import net.minecraft.client.model.geom.PartPose;
//import com.google.common.collect.ImmutableList;
//
//public class DroneModel_old extends ListModel<DroneEntity> {
//    private final ModelPart root;
//
//    public DroneModel_old(ModelPart root) {
//        this.root = root;
//    }
//
//    // 💡 1.18.2の標準的なモデル立方体の組み立て設定
//    public static LayerDefinition createBodyLayer() {
//        MeshDefinition meshdefinition = new MeshDefinition();
//        PartDefinition partdefinition = meshdefinition.getRoot();
//
//        // 中央のメインボディ (縦横10マス、厚さ4マス程度の薄型ボディ)
//        PartDefinition body = partdefinition.addOrReplaceChild("body",
//                CubeListBuilder.create().texOffs(0, 0)
//                        .addBox(-5.0F, -2.0F, -5.0F, 10.0F, 4.0F, 10.0F),
//                PartPose.offset(0.0F, 22.0F, 0.0F));
//
//        // 4本のプロペラアームを簡易配置（後ほどBlockbenchモデルに差し替える際の目印）
//        body.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(0, 14).addBox(4.0F, -1.0F, 4.0F, 4.0F, 1.0F, 1.0F), PartPose.ZERO);
//        body.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, -1.0F, 4.0F, 4.0F, 1.0F, 1.0F), PartPose.ZERO);
//        body.addOrReplaceChild("arm3", CubeListBuilder.create().texOffs(0, 14).addBox(4.0F, -1.0F, -5.0F, 4.0F, 1.0F, 1.0F), PartPose.ZERO);
//        body.addOrReplaceChild("arm4", CubeListBuilder.create().texOffs(0, 14).addBox(-8.0F, -1.0F, -5.0F, 4.0F, 1.0F, 1.0F), PartPose.ZERO);
//
//        return LayerDefinition.create(meshdefinition, 64, 32);
//    }
//
//    @Override
//    public void setupAnim(DroneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        // ここにプロペラを回転させるアニメーションなどを後々追記できます
//    }
//
//    @Override
//    public Iterable<ModelPart> parts() {
//        return ImmutableList.of(this.root);
//    }
//}