package com.FREEMOD.freemod.item;

import com.FREEMOD.freemod.block.CameraBlock;
import com.FREEMOD.freemod.entity.CameraEntity;
import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class CameraControllerItem extends Item {
    private static boolean isViewing = false;
    private static CameraEntity activeCamera = null;

    public static boolean isViewing() { return isViewing; }
    public static CameraEntity getActiveCamera() { return activeCamera; }
    public static void setViewing(boolean viewing) { isViewing = viewing; }
    public static void setActiveCamera(CameraEntity camera) { activeCamera = camera; }

    public CameraControllerItem() {
        super(new Properties().tab(FreeMod.FREEMOD_TAB).stacksTo(1));
    }

    // 1. ブロックを通常右クリックしたとき（カメラの登録）
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        // すでにカメラを覗いている最中なら、設置登録処理をスキップして空中右クリック(use)側へ流す
        if (isViewing) return InteractionResult.PASS;

        if (level.getBlockState(pos).getBlock() instanceof CameraBlock) {
            // 【重要】スニーク（SHIFT）していない時だけ登録する
            if (player != null && !player.isShiftKeyDown()) {
                if (!level.isClientSide()) {
                    CompoundTag nbt = stack.getOrCreateTag();
                    nbt.putInt("CamX", pos.getX());
                    nbt.putInt("CamY", pos.getY());
                    nbt.putInt("CamZ", pos.getZ());
                    nbt.putBoolean("Linked", true);
                    player.displayClientMessage(new TextComponent("カメラをコントローラーに同期しました: " + pos.toShortString()), true);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return super.useOn(context);
    }

    // 2. SHIFT+右クリックで起動 / 通常右クリックで解除
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag nbt = stack.getTag();

        if (nbt != null && nbt.getBoolean("Linked")) {
            if (level.isClientSide()) {
                // 【解除処理】すでにカメラを覗いている状態で、通常右クリック（SHIFTなし）されたら解除
                if (isViewing && !player.isShiftKeyDown()) {
                    ClientProxy.toggleCameraView(level, nbt, player, false);
                    player.getCooldowns().addCooldown(this, 10); // チャタリング暴発防止
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
                }
                // 【起動処理】まだ覗いていない状態で、SHIFT+右クリックされたらカメラを覗く
                else if (!isViewing && player.isShiftKeyDown()) {
                    ClientProxy.toggleCameraView(level, nbt, player, true);
                    player.getCooldowns().addCooldown(this, 10); // チャタリング暴発防止
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        } else {
            if (!level.isClientSide()) {
                player.displayClientMessage(new TextComponent("カメラが同期されていません。ブロックを右クリックしてください。"), true);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    private static class ClientProxy {
        private static void toggleCameraView(Level level, CompoundTag nbt, Player player, boolean wantToView) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();

            // 解除リクエストの処理
            if (!wantToView && isViewing) {
                isViewing = false;
                activeCamera = null;
                mc.setCameraEntity(player);
                player.displayClientMessage(new TextComponent("カメラ接続を解除し、プレイヤー視点に戻りました。"), true);
                return;
            }

            // 起動リクエストの処理
            if (wantToView && !isViewing) {
                int x = nbt.getInt("CamX");
                int y = nbt.getInt("CamY");
                int z = nbt.getInt("CamZ");
                BlockPos targetPos = new BlockPos(x, y, z);

                List<CameraEntity> cameras = level.getEntitiesOfClass(CameraEntity.class, new AABB(targetPos).inflate(1.0D));
                if (!cameras.isEmpty()) {
                    CameraEntity targetCamera = cameras.get(0);
                    isViewing = true;
                    activeCamera = targetCamera;

                    // 覗いた瞬間のカメラの初期正面を、現在のプレイヤーの向きにリセット
                    targetCamera.setXRot(player.getXRot());
                    targetCamera.setYRot(player.getYRot());

                    mc.setCameraEntity(targetCamera);
                    player.displayClientMessage(new TextComponent("カメラ視点に接続しました（解除: 通常右クリック）"), true);
                } else {
                    player.displayClientMessage(new TextComponent("カメラが見つかりません（チャンクがアンロードされたか、破壊されています）"), true);
                }
            }
        }
    }
}