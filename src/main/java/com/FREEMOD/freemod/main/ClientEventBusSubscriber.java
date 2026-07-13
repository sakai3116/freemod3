package com.FREEMOD.freemod.main;

import java.util.Arrays;
import java.util.List;

import com.FREEMOD.freemod.entity.model.DroneModel;
import com.FREEMOD.freemod.entity.renderer.DroneRenderer;
import com.FREEMOD.freemod.entity.renderer.FastShotSkeletonRenderer;
import com.FREEMOD.freemod.register.BlockRegister;
import com.FREEMOD.freemod.register.EntityRegister;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FreeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // クライアント起動時に初期化する(鯖の出入りでは初期化しない)
        blockRenderType(event);
        animation(event);
    }

    //RegisterLayerDefinitionのイベントが走った時にMobのレイヤを読み込ませる
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(DroneRenderer.DRONE_LAYER, DroneModel::createBodyLayer);
    }

    //RegisterRenderersのイベントが走った時にMobのレンダラーを読み込ませる
    @SubscribeEvent
    public static void registerRender(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegister.FAST_SHOT_SKELETON.get(), FastShotSkeletonRenderer::new);
        event.registerEntityRenderer(EntityRegister.DRONE.get(), DroneRenderer::new);
        event.registerEntityRenderer(EntityRegister.CAMERA.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);    }

    private static void animation(FMLClientSetupEvent event){
        event.enqueueWork(() -> {
            List<Item> animationItems = Arrays.asList(
                     // 必要に応じて複数追加可能
            );
            for (Item item : animationItems){
                ItemProperties.register(item, new ResourceLocation("pull"), (itemstack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    } else {
                        return entity.getUseItem() != itemstack ? 0.0F
                                : (float)(itemstack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                    }
                });

                ItemProperties.register(item, new ResourceLocation("pulling"), (itemstack, world, entity, seed) -> {
                    return entity != null && entity.isUsingItem() && entity.getUseItem() == itemstack ? 1.0F : 0.0F;
                });
            }
        });
    }

    private static void blockRenderType(FMLClientSetupEvent event){
        // ブロックのレンダラータイプの登録
        event.enqueueWork(()->{
            List<Block> cutoutBlocks = Arrays.asList(

                    // portal


                    // sapling
                    BlockRegister.OBLIVION_SAPLING.get()
            );

            List<Block> translucentBlocks = Arrays.asList(

                    // fluid
                    BlockRegister.NEKTAR_LIQUID_BLOCK.get()

            );

            // 各リストに対してRenderLayerを設定
            for (Block block : cutoutBlocks) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
            }

            for (Block block : translucentBlocks) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
            }
        });
    }
}
