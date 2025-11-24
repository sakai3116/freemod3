package com.FREEMOD.freemod.util;

import com.FREEMOD.freemod.main.FreeMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> OBLIVION_PORTAL_FRAME_BLOCKS
                = tag("oblivion_portal_frame_blocks");

        public static final TagKey<Block> PLATINUM_ORE_REPLACEABLES = tag("platinum_ore_replaceables");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(FreeMod.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Items {



        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(FreeMod.MOD_ID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }
}