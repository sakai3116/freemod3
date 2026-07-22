package com.FREEMOD.freemod.register;

import com.FREEMOD.freemod.effect.ParalyzeEffect;
import com.FREEMOD.freemod.entity.renderer.CustomChestItemRenderer;
import com.FREEMOD.freemod.item.CameraControllerItem;
import com.FREEMOD.freemod.item.DroneCameraItem;
import com.FREEMOD.freemod.item.ParalyzeEffectItem;
import com.FREEMOD.freemod.item.spawnegg.FastShotSkeletonSpawnEgg;
import com.FREEMOD.freemod.item.tool.WorldLineItem;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    // レジストリを作成
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FreeMod.MOD_ID);

    //tool
    public static final RegistryObject<Item> WORLD_LINE = ITEMS.register("world_line", WorldLineItem::new);

    public static final RegistryObject<Item> PARALYZE_EFFECT_ITEM = ITEMS.register("paralyze_effect_item", ParalyzeEffectItem::new);

    public static final RegistryObject<Item> CAMERA_CONTROLLER_ITEM = ITEMS.register("camera_item", CameraControllerItem::new);

    public static final RegistryObject<Item> ACID_BUCKET = ITEMS.register("acid_bucket",
            () -> new BucketItem(FluidRegister.ACID_FLUID,new Item.Properties().tab(FreeMod.FREEMOD_TAB).stacksTo(1)));
    public static final RegistryObject<Item> NEKTAR_BUCKET = ITEMS.register("nektar_bucket",
            () -> new BucketItem(FluidRegister.NEKTAR_FLUID,new Item.Properties().tab(FreeMod.FREEMOD_TAB).stacksTo(1)));
    public static final RegistryObject<Item> PLATINUM = ITEMS.register("platinum",
            () -> new Item(new Item.Properties().tab(FreeMod.FREEMOD_TAB)));
    public static final RegistryObject<Item> PLATINUM_INGOT = ITEMS.register("platinum_ingot",
            () -> new Item(new Item.Properties().tab(FreeMod.FREEMOD_TAB)));
    public static final RegistryObject<Item> DRONE_CAMERA = ITEMS.register("drone_camera", DroneCameraItem::new);

    // spawn egg
    public static final RegistryObject<Item> FAST_SHOT_SKELETON_SPAWN_EGG = ITEMS.register("fast_shot_skeleton_spawn_egg", () -> new FastShotSkeletonSpawnEgg(() -> EntityRegister.FAST_SHOT_SKELETON.get(), 0xFFFFFF, 0xFFFFFF, new Item.Properties().tab(FreeMod.FREEMOD_TAB)));

    public static final RegistryObject<Item> CUSTOM_CHEST_ITEM = ITEMS.register("custom_chest_block",
            () -> new BlockItem(BlockRegister.CUSTOM_CHEST_BLOCK.get(), new Item.Properties().tab(FreeMod.FREEMOD_BLOCK_TAB)) {
                @Override
                public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
                    consumer.accept(new net.minecraftforge.client.IItemRenderProperties() {
                        @Override
                        public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                            return new CustomChestItemRenderer();
                        }
                    });
                }
            });


    public static void register(IEventBus eventBus){
        // レジストリをイベントバスに登録
        ITEMS.register(eventBus);
    }
}
