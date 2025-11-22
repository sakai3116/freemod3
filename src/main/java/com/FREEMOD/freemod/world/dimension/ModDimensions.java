package com.FREEMOD.freemod.world.dimension;

import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class ModDimensions {
    public static final ResourceKey<Level> OBLIVION_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY,
            new ResourceLocation(FreeMod.MOD_ID,"oblivion_world"));

    public static final ResourceKey<DimensionType> OBLIVION_TYPE =
            ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY,OBLIVION_KEY.getRegistryName());

    public static void register(){
        System.out.println("Registering ModDimensions for " + FreeMod.MOD_ID);
    }
}
