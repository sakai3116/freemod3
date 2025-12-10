package com.FREEMOD.freemod.world.structure;

import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class OblivionStructures {
    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, FreeMod.MOD_ID);


    public static void register(IEventBus eventBus){
        DEFERRED_REGISTRY_STRUCTURE.register(eventBus);
    }
}
