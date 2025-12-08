package com.aiden.hellenic_echoes.block;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.block.custom.AlloyForgeBlock;
import com.aiden.hellenic_echoes.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HellenicEchoes.MODID);

    public static final RegistryObject<Block> BRONZE_ORE = registerBlock("bronze_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE)));

    public static final RegistryObject<Block> AETHER_ORE = registerBlock("aether_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> ALLOY_FORGE_BLOCK = registerBlock("alloy_forge_block",
            () -> new AlloyForgeBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
