package com.FREEMOD.freemod.effect;

import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOD_EFFECT
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FreeMod.MOD_ID);

    public static final RegistryObject<MobEffect> ACID_EFFECT = MOD_EFFECT.register("acid",
            () -> new AcidEffect(MobEffectCategory.HARMFUL,65280));



    public static void register(IEventBus eventBus){
        MOD_EFFECT.register(eventBus);
    }
}
