package com.aiden.hellenic_echoes.recipe;

import com.aiden.hellenic_echoes.HellenicEchoes;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, HellenicEchoes.MODID);

    public static final RegistryObject<RecipeSerializer<AlloyForgeRecipe>> ALLOY_FORGE_SERIALIZER =
            SERIALIZERS.register("alloy_forge", () -> AlloyForgeRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
