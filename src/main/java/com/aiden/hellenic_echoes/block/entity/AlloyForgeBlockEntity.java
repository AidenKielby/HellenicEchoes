package com.aiden.hellenic_echoes.block.entity;

import com.aiden.hellenic_echoes.item.ModItems;
import com.aiden.hellenic_echoes.recipe.AlloyForgeRecipe;
import com.aiden.hellenic_echoes.screen.AlloyForgeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AlloyForgeBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4);

    private static final int LEFT_INPUT_SLOT = 0;
    private static final int RIGHT_INPUT_SLOT = 1;
    private static final int LAVA_INPUT_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;
    private int lavaAmount = 0;
    private int maxLavaAmount = 100;

    public AlloyForgeBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ALLOY_FORGE_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> AlloyForgeBlockEntity.this.progress;
                    case 1 -> AlloyForgeBlockEntity.this.maxProgress;
                    case 2 -> AlloyForgeBlockEntity.this.lavaAmount;
                    case 3 -> AlloyForgeBlockEntity.this.maxLavaAmount;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i){
                    case 0 -> AlloyForgeBlockEntity.this.progress = i1;
                    case 1 -> AlloyForgeBlockEntity.this.maxProgress = i1;
                    case 2 -> AlloyForgeBlockEntity.this.lavaAmount = i1;
                    case 3 -> AlloyForgeBlockEntity.this.maxLavaAmount = i1;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++){
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.hellenicechoes.alloy_forge_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new AlloyForgeMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("alloy_forge_block.progress", progress);
        pTag.putInt("alloy_forge_block.lava", lavaAmount);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("alloy_forge_block.progress");
        lavaAmount = pTag.getInt("alloy_forge_block.lava");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasProgressFinished()){
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
        if (hasLavaInSlot()){
            addLava();
        }

        //System.out.println("Has recipe: " + hasRecipe());
        //System.out.println("Lava amount: " + lavaAmount);
        //System.out.println("Slot 0: " + itemHandler.getStackInSlot(0));
        //System.out.println("Slot 1: " + itemHandler.getStackInSlot(1));
    }

    private void addLava() {
        ItemStack stack = this.itemHandler.getStackInSlot(LAVA_INPUT_SLOT);

        if (this.lavaAmount + 10 <= this.maxLavaAmount) {
            this.lavaAmount += 10;

            // Consume the lava bucket
            this.itemHandler.extractItem(LAVA_INPUT_SLOT, 1, false);

            // Replace it with an empty bucket
            this.itemHandler.setStackInSlot(LAVA_INPUT_SLOT, new ItemStack(Items.BUCKET));

            setChanged(); // IMPORTANT so the game updates the block entity
        }
    }

    private boolean hasLavaInSlot() {
        return this.itemHandler.getStackInSlot(LAVA_INPUT_SLOT).getItem() == Items.LAVA_BUCKET;
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<AlloyForgeRecipe> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        ItemStack result = recipe.get().getResultItem(null);
        this.itemHandler.extractItem(LEFT_INPUT_SLOT, 1, false);
        this.itemHandler.extractItem(RIGHT_INPUT_SLOT, 1, false);
        this.lavaAmount -= recipe.get().getLavaUsage();

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<AlloyForgeRecipe> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        boolean hasLava = this.lavaAmount >= recipe.get().getLavaUsage();
        ItemStack result = recipe.get().getResultItem(null);

        return hasLava && canInsertAmmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<AlloyForgeRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(AlloyForgeRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }
}
