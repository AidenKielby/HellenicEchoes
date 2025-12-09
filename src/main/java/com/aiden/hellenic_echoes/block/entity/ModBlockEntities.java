package com.aiden.hellenic_echoes.block.entity;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HellenicEchoes.MODID);

    public static final RegistryObject<BlockEntityType<AlloyForgeBlockEntity>> ALLOY_FORGE_BE =
            BLOCK_ENTITIES.register("alloy_forge_be", () ->
                    BlockEntityType.Builder.of(AlloyForgeBlockEntity::new,
                            ModBlocks.ALLOY_FORGE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

}
