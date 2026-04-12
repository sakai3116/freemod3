package com.FREEMOD.freemod.item.spawnegg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.Supplier;

public class FastShotSkeletonSpawnEgg extends ForgeSpawnEggItem {

    public FastShotSkeletonSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, Item.Properties properties){
        super(type, primaryColor, secondaryColor, properties);
    }
}
