package com.FREEMOD.freemod.item;

import com.FREEMOD.freemod.main.FreeMod;
import com.FREEMOD.freemod.main.handler.ClientCameraHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DroneCameraItem extends Item {
    public DroneCameraItem() {
        super(new Properties()
                .tab(FreeMod.FREEMOD_TAB)
                .stacksTo(1)
        );
    }

    // memo 右クリでドローンモードのOn、Off　Qでドロップ時の対策で右CTRLで強制モード終了のキーバインドあり

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide()){
            ClientCameraHandler.toggleDroneMode();
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
