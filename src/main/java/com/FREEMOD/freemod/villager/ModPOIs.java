package com.FREEMOD.freemod.villager;


import com.FREEMOD.freemod.main.FreeMod;
import com.FREEMOD.freemod.register.BlockRegister;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPOIs {
    public static final DeferredRegister<PoiType> POI
            = DeferredRegister.create(ForgeRegistries.POI_TYPES, FreeMod.MOD_ID);

    public static final RegistryObject<PoiType> OBLIVION_PORTAL =
            POI.register("oblivion_portal",() -> new PoiType("oblivion_portal", PoiType.getBlockStates(BlockRegister.OBLIVION_PORTAL_BLOCK.get()),0,1));


    public static void register(IEventBus eventBus) {
        POI.register(eventBus);
    }
}