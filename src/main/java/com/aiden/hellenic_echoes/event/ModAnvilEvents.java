package com.aiden.hellenic_echoes.event;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.aiden.hellenic_echoes.item.ModItems;
import com.aiden.hellenic_echoes.util.ModTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
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
            if(left.is(ModTags.Items.AETHER_TAG_ITEM)){
                ItemStack result = left.copy();
                result.getOrCreateTag().putString("AetherTag", pickRandomItemTag());


                event.setOutput(result);
                event.setCost(5);
                event.setMaterialCost(1);
            }
            else if (left.is(Tags.Items.ARMORS_HELMETS)){
                ItemStack result = left.copy();
                result.getOrCreateTag().putString("AetherTag", pickRandomHelmetTag());

                event.setOutput(result);
                event.setCost(6);
                event.setMaterialCost(1);
            }
            else if (left.is(ModTags.Items.AETHER_TAG_ARMOUR)){
                ItemStack result = left.copy();
                result.getOrCreateTag().putString("AetherTag", pickRandomArmorTag());

                event.setOutput(result);
                event.setCost(8);
                event.setMaterialCost(1);
            }

        }
    }

    public static String pickRandomItemTag(){
        String[] tags = {"aether_dash", "moons_power", "greed"};
        return tags[(int) (Math.random() * tags.length)];
    }

    public static String pickRandomArmorTag(){
        String[] tags = {""};
        return tags[(int) (Math.random() * tags.length)];
    }

    public static String pickRandomHelmetTag(){
        String[] tags = {"apollos_sight"};
        return tags[(int) (Math.random() * tags.length)];
    }
}
