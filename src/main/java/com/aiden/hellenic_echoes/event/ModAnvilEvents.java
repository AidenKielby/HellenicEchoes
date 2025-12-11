package com.aiden.hellenic_echoes.event;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HellenicEchoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModAnvilEvents {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event){
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (right.getItem() == ModItems.AETHER_SHARD.get()){
            ItemStack result = left.copy();
            result.getOrCreateTag().putString("AetherTag", pickRandomTag());

            event.setOutput(result);
            event.setCost(5);
        }
    }

    public static String pickRandomTag(){
        String[] tags = {"aether_dash"};
        return tags[(int) (Math.random() * tags.length)];
    }
}
