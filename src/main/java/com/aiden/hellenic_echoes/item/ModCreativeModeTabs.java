package com.aiden.hellenic_echoes.item;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HellenicEchoes.MODID);

    public static final RegistryObject<CreativeModeTab> HELLENIC_ECHOES_TAB = CREATIVE_MODE_TABS.register("hellenic_echoes_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.AETHER_CRISTAL.get()))
                    .title(Component.translatable("creativetab.hellenic_echoes_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.AETHER_CRISTAL.get());
                        output.accept(ModItems.AETHER_SHARD.get());
                        output.accept(ModItems.BRONZE.get());

                        output.accept(ModBlocks.BRONZE_ORE.get());
                        output.accept(ModBlocks.AETHER_ORE.get());

                        output.accept(ModBlocks.ALLOY_FORGE_BLOCK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
