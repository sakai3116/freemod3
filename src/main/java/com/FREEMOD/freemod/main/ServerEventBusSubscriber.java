package com.FREEMOD.freemod.main;

import com.FREEMOD.freemod.entity.FastShotSkeleton;
import com.FREEMOD.freemod.register.EntityRegister;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FreeMod.MOD_ID,bus =  Mod.EventBusSubscriber.Bus.MOD)
public class ServerEventBusSubscriber {

    //EntityAttributeCreationEventが走った時に特性を読み込み
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(EntityRegister.FAST_SHOT_SKELETON.get(), FastShotSkeleton.createAttributes().build());

    }
}
