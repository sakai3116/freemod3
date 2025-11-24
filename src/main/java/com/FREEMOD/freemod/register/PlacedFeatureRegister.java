package com.FREEMOD.freemod.register;

import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class PlacedFeatureRegister {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, FreeMod.MOD_ID);

    //tree
    public static final RegistryObject<PlacedFeature> OBLIVION_TREE = PLACED_FEATURES.register("oblivion_tree",() -> new PlacedFeature(ConfiguredFeatureRegister.OBLIVION_TREE.getHolder().get(),tree(1)));

    // ore
    public static final RegistryObject<PlacedFeature> PLATINUM_ORE = PLACED_FEATURES.register("platinum_ore", () -> new PlacedFeature(ConfiguredFeatureRegister.PLATINUM_ORE.getHolder().get(), commonOrePlacement(17, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(128)))));


    private static List<PlacementModifier> tree(int count) {
        return List.of(CountOnEveryLayerPlacement.of(count), BiomeFilter.biome(), BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)));
    }

    private static List<PlacementModifier> patch(int count) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> rareOrePlacement(int count, PlacementModifier heightRange) {
        return List.of(RarityFilter.onAverageOnceEvery(count), InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    public static void register(IEventBus eventBus) {
        // レジストリをイベントバスに登録
        PLACED_FEATURES.register(eventBus);
    }
}
