package com.FREEMOD.freemod.main;

import com.FREEMOD.freemod.entity.renderer.CustomChestRenderer;
import com.FREEMOD.freemod.main.tab.FreeModBlockTab;
import com.FREEMOD.freemod.main.tab.FreeModTab;
import com.FREEMOD.freemod.register.*;
import com.FREEMOD.freemod.villager.ModPOIs;
import com.FREEMOD.freemod.world.dimension.ModDimensions;
import com.FREEMOD.freemod.world.structure.OblivionStructures;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("freemod")
public class FreeMod {
    //MOD_ID
    public static final String MOD_ID = "freemod";
    //クリエイトタブの登録
    public static final CreativeModeTab FREEMOD_TAB = new FreeModTab();
    public static final CreativeModeTab FREEMOD_BLOCK_TAB = new FreeModBlockTab();

    public FreeMod(){
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::commonSetup);
        //この下に追加していく

        //ブロックの登録
        BlockRegister.register(eventBus);
        //アイテムの登録
        ItemRegister.register(eventBus);

        //ディメンションの登録
        ModDimensions.register();
        //バイオームの登録
        BiomeRegister.register(eventBus);
        //ポイントオブインスタンスの登録（POI）
        ModPOIs.register(eventBus);
        // placed
        PlacedFeatureRegister.register(eventBus);
        // feature
        ConfiguredFeatureRegister.register(eventBus);
        // effect
        EffectRegister.register(eventBus);
        // structure
        OblivionStructures.register(eventBus);

        BlockEntityRegister.register(eventBus);

        eventBus.addListener(this::clientSetup);

        eventBus.addListener(this::onTextureStitch);

        EntityRegister.register(eventBus);

        //独自の登録
        FluidRegister.register(eventBus);

    }
    private void commonSetup(final FMLCommonSetupEvent event) {

    }
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // ここに入れます！
            // ※第一引数はこれから作成するBlockEntityRegisterの変数名に合わせてください
            BlockEntityRenderers.register(BlockEntityRegister.CUSTOM_CHEST_BLOCK_ENTITY.get(), CustomChestRenderer::new);
        });
    }

    private void onTextureStitch(final TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            event.addSprite(CustomChestRenderer.CUSTOM_CHEST_LOCATION);
        }
    }
}
