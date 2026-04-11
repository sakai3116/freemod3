package com.FREEMOD.freemod.entity.renderer; // ご自身の環境に合わせて調整してください

import com.FREEMOD.freemod.blockentity.CustomChestBlockEntity;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;

public class CustomChestRenderer extends ChestRenderer<CustomChestBlockEntity> {

    // テクスチャの場所を指定 (assets/freemod/textures/entity/chest/custom_chest.png を読み込む)
    public static final ResourceLocation CUSTOM_CHEST_LOCATION = new ResourceLocation(FreeMod.MOD_ID, "entity/chest/custom_chest");

    // マイクラ内のチェスト用画像シート（アトラス）に、このテクスチャを紐づける
    public static final Material CUSTOM_CHEST_MATERIAL = new Material(Sheets.CHEST_SHEET, CUSTOM_CHEST_LOCATION);

    public CustomChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    // ★ここでバニラのテクスチャの代わりに、オリジナルのテクスチャを返すように書き換えます
    @Override
    protected Material getMaterial(CustomChestBlockEntity blockEntity, ChestType chestType) {
        // ※ラージチェスト（チェストを2つ繋げた状態）を作る場合はここで条件分岐しますが、まずはシングルチェスト用を返します
        return CUSTOM_CHEST_MATERIAL;
    }
}