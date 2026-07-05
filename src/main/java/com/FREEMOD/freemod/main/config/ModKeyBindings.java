package com.FREEMOD.freemod.main.config;

import com.FREEMOD.freemod.main.FreeMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FreeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModKeyBindings {
    public static KeyMapping droneExitKey;

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        // キーの登録名、デフォルトキー(Xキー)、カテゴリ名を設定
        droneExitKey = new KeyMapping(
                "key.freemod.drone_exit",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RCONTROL,
                "key.categories.freemod"
        );

        // 1.18.2のForgeでキーバインドをクライアントに登録するメソッド
        ClientRegistry.registerKeyBinding(droneExitKey);
    }
}