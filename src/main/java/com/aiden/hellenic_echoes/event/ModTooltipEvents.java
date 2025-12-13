package com.aiden.hellenic_echoes.event;

import com.aiden.hellenic_echoes.HellenicEchoes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HellenicEchoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModTooltipEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (!stack.hasTag()) return;
        if (!stack.getTag().contains("AetherTag")) return;

        String tag = stack.getTag().getString("AetherTag");

        switch (tag) {
            case "aether_dash":
                event.getToolTip().add(Component.literal("Aether Dash")
                        .withStyle(ChatFormatting.AQUA));
                break;

            case "moons_power":
                event.getToolTip().add(Component.literal("Moon's Power")
                        .withStyle(ChatFormatting.DARK_PURPLE));
                break;

            case "greed":
                event.getToolTip().add(Component.literal("Greed")
                        .withStyle(ChatFormatting.GOLD));
                break;

            case "apollos_sight":
                event.getToolTip().add(Component.literal("Apollo's Sight")
                        .withStyle(ChatFormatting.DARK_AQUA));
                break;
        }
    }
}
