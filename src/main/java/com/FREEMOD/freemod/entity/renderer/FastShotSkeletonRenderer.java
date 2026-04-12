package com.FREEMOD.freemod.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class FastShotSkeletonRenderer extends SkeletonRenderer {
    // 自作テクスチャのパスを指定 (assets/freemod/textures/entity/fast_shot_skeleton.png)
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/entity/skeleton/skeleton.png");

    public FastShotSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
        return TEXTURE;
    }
}