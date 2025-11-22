package com.FREEMOD.freemod.register;

import com.FREEMOD.freemod.block.portal.OblivionPortalBlock;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegister {
    // レジストリを作成
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FreeMod.MOD_ID);
    // ブロック追加時、以下に追加

    // biomes -> oblivion
    public static final RegistryObject<Block> OBLIVION_GRASS_BLOCK = registerBlockItem("oblivion_grass_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));
    public static final RegistryObject<Block> OBLIVION_DIRT = registerBlockItem("oblivion_dirt",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
    public static final RegistryObject<Block> OBLIVION_STONE = registerBlockItem("oblivion_stone",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    // tree

    // ore

    // other
    public static final RegistryObject<Block> OBLIVION_PORTAL_GATE_BLOCK = registerBlockItem("oblivion_portal_gate_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(9f)));
    public static final RegistryObject<Block> OBLIVION_PORTAL_BLOCK = BLOCKS.register("oblivion_portal", OblivionPortalBlock::new);

    // ブロックアイテム作成用メソッド 基本的に触らない
    private static <T extends Block> RegistryObject<T> registerBlockItem(String name, Supplier<T> supplier) {
        // レジストリにブロックを追加
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        // ブロックアイテムをアイテムレジストリに追加
        ItemRegister.ITEMS.register(name,
                () -> new BlockItem(block.get(), new Item.Properties().tab(FreeMod.FREEMOD_BLOCK_TAB)));
        return block;
    }

    public static void register(IEventBus eventBus) {
        // レジストリをイベントバスに登録
        BLOCKS.register(eventBus);
    }
}
