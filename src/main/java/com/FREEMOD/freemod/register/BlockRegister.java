package com.FREEMOD.freemod.register;

import com.FREEMOD.freemod.block.StrippedRotatedPillarBlock;
import com.FREEMOD.freemod.block.portal.OblivionPortalBlock;
import com.FREEMOD.freemod.main.FreeMod;
import com.FREEMOD.freemod.world.feature.tree.OblivionGrower;
import net.minecraft.util.valueproviders.UniformInt;
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
    public static final RegistryObject<RotatedPillarBlock> OBLIVION_LOG = registerBlockItem("oblivion_log",
            () -> new StrippedRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> OBLIVION_WOOD = registerBlockItem("oblivion_wood",
            () -> new StrippedRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_OBLIVION_LOG = registerBlockItem("stripped_oblivion_log",
            () -> new StrippedRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_OBLIVION_WOOD = registerBlockItem("stripped_oblivion_wood",
            () -> new StrippedRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> OBLIVION_PLANK = registerBlockItem("oblivion_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> OBLIVION_LEAVES = registerBlockItem("oblivion_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<SaplingBlock> OBLIVION_SAPLING = registerBlockItem("oblivion_sapling",
            () -> new SaplingBlock(new OblivionGrower(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    // ore
    public static final RegistryObject<Block> PLATINUM_ORE = registerBlockItem("platinum_ore",
            () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE) //part 40 UniformInt.of(3,7)の追加　経験値ドロップ
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3,7)));
    public static final RegistryObject<Block> DEEPSLATE_PLATINUM_ORE = registerBlockItem("deepslate_platinum_ore",
            () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE) //part 40 UniformInt.of(3,7)の追加　経験値ドロップ
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3,7)));

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
