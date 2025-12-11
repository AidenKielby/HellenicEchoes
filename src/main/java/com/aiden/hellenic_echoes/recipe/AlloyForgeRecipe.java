package com.aiden.hellenic_echoes.recipe;

import com.aiden.hellenic_echoes.HellenicEchoes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AlloyForgeRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int lavaUsage;

    public AlloyForgeRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id, int lavaUsage) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.lavaUsage = lavaUsage;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if(level.isClientSide()){
            return false;
        }

        return (inputItems.get(0).test(simpleContainer.getItem(0)) && inputItems.get(1).test(simpleContainer.getItem(1))) ||
                (inputItems.get(0).test(simpleContainer.getItem(1)) && inputItems.get(1).test(simpleContainer.getItem(0)));
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public int getLavaUsage() {
        return lavaUsage;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<AlloyForgeRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "alloy_forge";
    }

    public static class Serializer implements RecipeSerializer<AlloyForgeRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(HellenicEchoes.MODID, "alloy_forge");

        @Override
        public AlloyForgeRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            System.out.println("Loading Alloy Forge recipe: " + resourceLocation);
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            int lavaUsage = GsonHelper.getAsInt(jsonObject, "lava_usage");

            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            System.out.println("Loaded Alloy Forge recipe: " + output.getItem() + ", lava usage: " + lavaUsage);

            return new AlloyForgeRecipe(inputs, output, resourceLocation, lavaUsage);
        }

        @Override
        public @Nullable AlloyForgeRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);
            int lavaUsage = friendlyByteBuf.readInt();

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(friendlyByteBuf));
            }

            ItemStack output = friendlyByteBuf.readItem();
            return new AlloyForgeRecipe(inputs, output, resourceLocation, lavaUsage);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, AlloyForgeRecipe alloyForgeRecipe) {
            friendlyByteBuf.writeInt(alloyForgeRecipe.inputItems.size());
            friendlyByteBuf.writeInt(alloyForgeRecipe.getLavaUsage());

            for (Ingredient ingredient : alloyForgeRecipe.getIngredients()){
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItemStack(alloyForgeRecipe.getResultItem(null), false);
        }
    }
}
