package com.aiden.hellenic_echoes.util;

import com.aiden.hellenic_echoes.HellenicEchoes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {



        private static TagKey<Block> tag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(HellenicEchoes.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> DASH_PROPERTY = tag("dash_property");

        private static TagKey<Item> tag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(HellenicEchoes.MODID, name));
        }
    }
}
