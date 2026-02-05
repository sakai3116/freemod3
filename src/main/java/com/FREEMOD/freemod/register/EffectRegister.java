package com.FREEMOD.freemod.register;

import com.FREEMOD.freemod.effect.AcidEffect;
import com.FREEMOD.freemod.effect.HealBlessingEffect;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegister {
    public static final DeferredRegister<MobEffect> EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FreeMod.MOD_ID);

    public static final RegistryObject<MobEffect> ACID_EFFECT = EFFECTS.register("acid",
            () -> new AcidEffect(MobEffectCategory.HARMFUL,65280));

    public static final RegistryObject<MobEffect> HEAL_BLESSING = EFFECTS.register("heal_blessing",
            () -> new HealBlessingEffect(MobEffectCategory.BENEFICIAL,65280));

    public static void register(IEventBus eventBus){
        EFFECTS.register(eventBus);
    }
}
