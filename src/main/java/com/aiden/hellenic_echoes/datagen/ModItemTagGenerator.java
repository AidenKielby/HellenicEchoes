package com.aiden.hellenic_echoes.datagen;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.item.ModItems;
import com.aiden.hellenic_echoes.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, HellenicEchoes.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.BRONZE_HELMET.get(),
                        ModItems.BRONZE_CHESTPLATE.get(),
                        ModItems.BRONZE_LEGGINGS.get(),
                        ModItems.BRONZE_BOOTS.get());

        this.tag(ModTags.Items.AETHER_TAG_ITEM)
                .addTag(ItemTags.SWORDS)
                .addTag(ItemTags.AXES)
                .add(Items.TRIDENT);

        this.tag(ModTags.Items.AETHER_TAG_ARMOUR)
                .addTag(ItemTags.TRIMMABLE_ARMOR);
    }
}
