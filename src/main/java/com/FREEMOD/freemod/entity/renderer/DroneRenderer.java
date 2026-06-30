package com.FREEMOD.freemod.entity.renderer;

import com.FREEMOD.freemod.entity.model.DroneModel;
import com.FREEMOD.freemod.main.FreeMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import com.mojang.math.Vector3f;

public class DroneRenderer extends BoatRenderer {
    // モデルレイヤーの識別子
    public static final net.minecraft.client.model.geom.ModelLayerLocation DRONE_LAYER =
            new net.minecraft.client.model.geom.ModelLayerLocation(new ResourceLocation(FreeMod.MOD_ID, "drone"), "main");

    private final DroneModel model;
    // テクスチャのパス (assets/freemod/textures/entity/drone.png) [cite: 197]
    private static final ResourceLocation DRONE_TEXTURE = new ResourceLocation(FreeMod.MOD_ID, "textures/entity/drone.png");

    public DroneRenderer(EntityRendererProvider.Context context) {
        super(context);
        // Blockbench形式にバインドされた新しいモデルをベイクして取得 [cite: 199]
        this.model = new DroneModel(context.bakeLayer(DRONE_LAYER));
    }

    @Override
    public void render(Boat boat, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // 💡 重要な座標調整ポイント！
        // ボート本来の低い描画位置（0.375D）を上書きし、三人称カメラの視線の中心（画面中央）に
        // ドローンモデルがぴったり重なるように高さを調整します。
        poseStack.translate(0.0D, 0.5D, 0.0D);

        // 💡 角度同期の修正
        // ドローンが向いている方角に合わせて、モデルを正確に回転させます。
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));

        // 💡 1.18.2 Java Entity 描画の標準的なお作法
        // Blockbenchのモデルを上下反転・および適切なサイズ（スケール）に調整します。
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        // 💡 新しいBlockbenchモデルパーツ（body）にテクスチャを焼き込んで描画を命令
        var vertexConsumer = bufferSource.getBuffer(this.model.renderType(DRONE_TEXTURE));

        // 描画メソッドを直接呼び出し、第4引数にOverlayTextureを渡してモデルを強制レンダリング
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Boat boat) {
        return DRONE_TEXTURE; // [cite: 207]
    }
}

//package com.FREEMOD.freemod.entity.renderer;
//
//import com.FREEMOD.freemod.entity.model.DroneModel;
//import com.FREEMOD.freemod.main.FreeMod;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Vector3f;
//import net.minecraft.client.model.geom.ModelLayerLocation;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.entity.BoatRenderer; // ボートのレンダラー
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.vehicle.Boat;
//
//public class DroneRenderer extends BoatRenderer {
//    // モデルレイヤーの識別子を定義
//    public static final ModelLayerLocation DRONE_LAYER = new ModelLayerLocation(new ResourceLocation(FreeMod.MOD_ID, "drone"), "main");
//
//    private final DroneModel model;
//    // 💡 ドローンの見た目（テクスチャ画像）のパスを指定
//    private static final ResourceLocation DRONE_TEXTURE = new ResourceLocation(FreeMod.MOD_ID, "textures/entity/drone.png");
//
//    public DroneRenderer(EntityRendererProvider.Context context) {
//        super(context);
//        // コンテキストからモデルパーツを引っ張り出して初期化
//        this.model = new DroneModel(context.bakeLayer(DRONE_LAYER));
//    }
//
//    @Override
//    public void render(Boat boat, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
//        // 💡 ボート本来の「水面でのガタつき」などの描画をカットし、独自のドローン描画を走らせる
//        poseStack.pushPose();
//        poseStack.translate(0.0D, 0.375D, 0.0D);
//
//        // 向き（Yaw回転）の適応
//        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
//
//        // ボートがダメージを受けた時の「揺れ」の計算（ボートの流用メリット）
//        float damageTime = (float)boat.getHurtTime() - partialTicks;
//        float damageIntensity = boat.getDamage() - partialTicks;
//        if (damageIntensity < 0.0F) {
//            damageIntensity = 0.0F;
//        }
//        if (damageTime > 0.0F) {
//            poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(damageTime) * damageTime * damageIntensity / 10.0F * (float)boat.getHurtDir()));
//        }
//
//        // 上下反転の防止とスケール調整
//        poseStack.scale(-1.0F, -1.0F, 1.0F);
//
//        // モデルにテクスチャを貼り付けて描画
//        var vertexConsumer = bufferSource.getBuffer(this.model.renderType(DRONE_TEXTURE));
//        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//
//        poseStack.popPose();
//    }
//
//    @Override
//    public ResourceLocation getTextureLocation(Boat boat) {
//        return DRONE_TEXTURE;
//    }
//}
