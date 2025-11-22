package com.FREEMOD.freemod.register;

import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegister {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, FreeMod.MOD_ID);

    public static final ResourceKey<Biome> OBLIVION_MOUNTAINS = register("oblivion_mountains");


    public static void toDictionary() {
        BiomeDictionary.addTypes(OBLIVION_MOUNTAINS,BiomeDictionary.Type.COLD,BiomeDictionary.Type.MOUNTAIN);
    }

    private static ResourceLocation name(String name) {
        return new ResourceLocation(FreeMod.MOD_ID, name);
    }

    private static ResourceKey<Biome> register(String name) {
        BIOMES.register(name, OverworldBiomes::theVoid);
        return ResourceKey.create(Registry.BIOME_REGISTRY, name(name));
    }

    public static void register(IEventBus eventBus){
        BIOMES.register(eventBus);
    }

}
