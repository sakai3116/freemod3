package com.FREEMOD.freemod.register;


import com.FREEMOD.freemod.blockentity.CustomChestBlockEntity;
import com.FREEMOD.freemod.main.FreeMod;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegister {

    // レジストリを作成
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, FreeMod.MOD_ID);

    // BlockEntityTypeの登録例
    public static final RegistryObject<BlockEntityType<CustomChestBlockEntity>> CUSTOM_CHEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("custom_chest",
                    () -> BlockEntityType.Builder.of(CustomChestBlockEntity::new, BlockRegister.CUSTOM_CHEST_BLOCK.get()).build(null));



    public static void register(IEventBus eventBus) {
        // レジストリをイベントバスに登録
        BLOCK_ENTITIES.register(eventBus);
    }

}
