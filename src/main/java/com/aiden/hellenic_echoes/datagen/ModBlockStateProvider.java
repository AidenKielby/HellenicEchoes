package com.aiden.hellenic_echoes.datagen;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HellenicEchoes.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.BRONZE_ORE);
        blockWithItem(ModBlocks.AETHER_ORE);

        simpleBlockWithItem(ModBlocks.ALLOY_FORGE_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/alloy_forge_block")));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
