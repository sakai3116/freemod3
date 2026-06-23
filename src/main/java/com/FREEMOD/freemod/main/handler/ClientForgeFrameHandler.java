package com.FREEMOD.freemod.main.handler;

import com.FREEMOD.freemod.main.FreeMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FreeMod.MOD_ID, value = Dist.CLIENT)
public class ClientForgeFrameHandler {

    // ClientForgeTickHandler の中身はこれだけにしてください
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientCameraHandler.onClientTick();
        }
    }
}