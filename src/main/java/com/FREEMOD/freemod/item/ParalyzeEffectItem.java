package com.FREEMOD.freemod.item;

import com.FREEMOD.freemod.main.FreeMod;
import com.FREEMOD.freemod.register.EffectRegister;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ParalyzeEffectItem extends Item {
    public ParalyzeEffectItem() {
        super(new Properties()
                .tab(FreeMod.FREEMOD_TAB)
                .stacksTo(1)
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            // アイテム使用時に、プレイヤーの周囲15ブロックの敵を探索する
            double range = 15.0D;
            AABB boundingBox = pPlayer.getBoundingBox().inflate(range);

            List<LivingEntity> nearbyEntities = pLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    boundingBox,
                    entity -> entity != pPlayer // 自分自身は除外
            );

            // 登録したParalyzeEffectが存在するかチェック
            if (EffectRegister.PARALYZE.get() != null) {
                for (LivingEntity target : nearbyEntities) {
                    // 敵対Mob、または通常のMobであるか判定
                    if (target instanceof Enemy || target instanceof Mob) {
                        // 対象の敵Mobに直接「ParalyzeEffect」を10秒間付与する
                        // 最後の引数を「true」にすることで、敵Mobから確実にパーティクルが発生します
                        target.addEffect(new MobEffectInstance(EffectRegister.PARALYZE.get(), 200, 0, false, true));
                    }
                }
            }

            // アイテムにクールダウンを設ける（20秒）
            pPlayer.getCooldowns().addCooldown(this, 400);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}