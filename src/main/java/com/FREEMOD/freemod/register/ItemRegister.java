package com.FREEMOD.freemod.register;

import com.FREEMOD.freemod.item.tool.WorldLineItem;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    // レジストリを作成
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FreeMod.MOD_ID);

    public static final RegistryObject<Item> WORLD_LINE = ITEMS.register("world_line", WorldLineItem::new);

    public static void register(IEventBus eventBus){
        // レジストリをイベントバスに登録
        ITEMS.register(eventBus);
    }
}
