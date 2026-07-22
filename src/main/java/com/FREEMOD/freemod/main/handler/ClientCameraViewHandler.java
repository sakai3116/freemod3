package com.FREEMOD.freemod.main.handler;

import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.item.CameraControllerItem;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FreeMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientCameraViewHandler {

    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (!CameraControllerItem.isViewing()) return;

        CameraEntity camera = CameraControllerItem.getActiveCamera();
        if (camera == null) return;

        event.setYaw(camera.getYRot());
        event.setPitch(camera.getXRot());
    }

    /**
     * 【追加】カメラ視聴中にプレイヤーの手（腕や持っているアイテム）を非表示にする
     */
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (CameraControllerItem.isViewing()) {
            event.setCanceled(true);
        }
    }

    /**
     * カメラ視点中のマウス入力・キー入力を横取りしてキャンセルする
     */
    @SubscribeEvent
    public static void onClickInput(InputEvent.ClickInputEvent event) {
        if (!CameraControllerItem.isViewing()) return;

        Minecraft mc = Minecraft.getInstance();

        if (event.isAttack() || event.isPickBlock()) {
            event.setCanceled(true);
            event.setSwingHand(false);
            return;
        }

        if (event.isUseItem()) {
            if (mc.player != null && mc.player.isShiftKeyDown()) {
                event.setCanceled(true);
                event.setSwingHand(false);
            }
        }
    }

    /**
     * 左クリック長押しによるブロック破壊（採掘）を防止する
     */
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (CameraControllerItem.isViewing()) {
            event.setCanceled(true);
        }
    }

    /**
     * カメラ視聴中に「通常の右クリック」を入力したら安全に解除する処理
     */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();

        if (CameraControllerItem.isViewing() && mc.player != null) {

            if (mc.options.keyUse.isDown() && !mc.player.isShiftKeyDown()) {

                CameraControllerItem.setViewing(false);
                CameraControllerItem.setActiveCamera(null);

                mc.setCameraEntity(mc.player);
                mc.player.displayClientMessage(new net.minecraft.network.chat.TextComponent("カメラ接続を解除し、プレイヤー視点に戻りました。"), true);

                if (!mc.player.getMainHandItem().isEmpty()) {
                    mc.player.getCooldowns().addCooldown(mc.player.getMainHandItem().getItem(), 15);
                }
            }
        }
    }
}