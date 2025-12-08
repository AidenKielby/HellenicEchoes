package com.aiden.hellenic_echoes.datagen;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HellenicEchoes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.AETHER_CRISTAL);
        simpleItem(ModItems.AETHER_SHARD);

        simpleItem(ModItems.BRONZE);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(
                item.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")
        ).texture(
                "layer0",
                ResourceLocation.fromNamespaceAndPath(HellenicEchoes.MODID, "item/" + item.getId().getPath())
        );
    }
}
