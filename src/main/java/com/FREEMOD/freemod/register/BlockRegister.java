package com.FREEMOD.freemod.register;

import com.FREEMOD.freemod.block.CameraBlock;
import com.FREEMOD.freemod.block.CustomChestBlock;
import com.FREEMOD.freemod.block.StrippedRotatedPillarBlock;
import com.FREEMOD.freemod.block.fluid.AcidLiquidBlock;
import com.FREEMOD.freemod.block.fluid.NektarWaterBlock;
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
    public static final RegistryObject<LiquidBlock> ACID_LIQUID_BLOCK = BLOCKS.register("acid_water",
            () -> new AcidLiquidBlock(FluidRegister.ACID_FLUID, BlockBehaviour.Properties.of(Material.WATER)
                    .noCollission().strength(100f).noDrops()));
    public static final RegistryObject<LiquidBlock> NEKTAR_LIQUID_BLOCK = BLOCKS.register("nektar_water",
            () -> new NektarWaterBlock(FluidRegister.NEKTAR_FLUID, BlockBehaviour.Properties.of(Material.WATER)
                    .noCollission().strength(100f).noDrops()));

    // biomes -> oblivion
    public static final RegistryObject<Block> OBLIVION_GRASS_BLOCK = registerBlockItem("oblivion_grass_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));
    public static final RegistryObject<Block> OBLIVION_DIRT = registerBlockItem("oblivion_dirt",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
    public static final RegistryObject<Block> OBLIVION_STONE = registerBlockItem("oblivion_stone",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> OBLIVION_BRICKS = registerBlockItem("oblivion_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<Block> MOSSY_OBLIVION_BRICKS = registerBlockItem("mossy_oblivion_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)));

    // 装飾系
    public static final RegistryObject<Block> OBLIVION_STONE_STAIRS = registerBlockItem("oblivion_stone_stairs",
            () -> new StairBlock(() -> BlockRegister.OBLIVION_STONE.get().defaultBlockState()
                    ,BlockBehaviour.Properties.of(Material.STONE).strength(5F).requiresCorrectToolForDrops()));


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
    public static final RegistryObject<Block> CAMERA_BLOCK = registerBlockItem("camera_block",
            () -> new CameraBlock(BlockBehaviour.Properties.of(Material.STONE).strength(9F).noOcclusion()));

    public static final RegistryObject<Block> OBLIVION_PORTAL_GATE_BLOCK = registerBlockItem("oblivion_portal_gate_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(9f)));
    public static final RegistryObject<Block> OBLIVION_PORTAL_BLOCK = BLOCKS.register("oblivion_portal", OblivionPortalBlock::new);

    public static final RegistryObject<ChestBlock> CUSTOM_CHEST_BLOCK = BLOCKS.register("custom_chest_block",
            () -> new CustomChestBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)));

    // ブロックアイテム作成用メソッド 基本的に触らない
    private static <T extends Block> RegistryObject<T> registerBlockItem(String name, Supplier<T> supplier) {
        // レジストリにブロックを追加
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        // ブロックアイテムをアイテムレジストリに追加
        ItemRegister.ITEMS.register(name,
                () -> new BlockItem(block.get(), new Item.Properties().tab(FreeMod.FREEMOD_BLOCK_TAB)));
        return block;
    }



//    // 2. アイテムとしての登録（ここで先ほど作ったレンダラーを紐づけます）
//    // ※ItemRegister.ITEMS がエラーになる場合は、上部にインポートを追加してください
//    public static final RegistryObject<Item> CUSTOM_CHEST_ITEM = ItemRegister.ITEMS.register("custom_chest_block",
//            () -> new BlockItem(CUSTOM_CHEST_BLOCK.get(), new Item.Properties().tab(FreeMod.FREEMOD_BLOCK_TAB)) {
//                @Override
//                public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
//                    consumer.accept(new net.minecraftforge.client.IItemRenderProperties() {
//                        @Override
//                        public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//                            // 先ほど作成したクラスを指定します
//                            return new com.FREEMOD.freemod.client.renderer.CustomChestItemRenderer();
//                        }
//                    });
//                }
//            });

    public static void register(IEventBus eventBus) {
        // レジストリをイベントバスに登録
        BLOCKS.register(eventBus);
    }
}
