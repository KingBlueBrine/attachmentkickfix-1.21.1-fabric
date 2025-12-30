package com.example.enchantmentmigrator.screen.custom;

import com.example.enchantmentmigrator.EnchantmentMigratorMod;
import com.example.enchantmentmigrator.block.entity.custom.EnchantmentMigratorBlockEntity;
import com.example.enchantmentmigrator.item.RazuliDustItem;
import com.example.enchantmentmigrator.screen.ModScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class EnchantmentMigratorScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final Inventory output = new SimpleInventory(1);
    public final EnchantmentMigratorBlockEntity blockEntity;
    private ItemEnchantmentsComponent inputMinusTopEnchants;
    private int xpCost;

    public EnchantmentMigratorScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(pos));
    }

    public EnchantmentMigratorScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {

        super(ModScreenHandlers.ENCHANTMENT_MIGRATOR_SH, syncId);
        this.inventory = ((Inventory) blockEntity);
        this.blockEntity = ((EnchantmentMigratorBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 27, 47) {
            @Override
            public boolean canInsert(ItemStack item) { return (item.hasEnchantments()); }
            @Override
            public void markDirty() {
                super.markDirty();
                //onContentChanged(inventory);
                updateResult();
            }
        });
        
        this.addSlot(new Slot(inventory, 1, 76, 47){
            @Override
            public boolean canInsert(ItemStack item) { return item.isOf(Items.BOOK); }
            /*@Override
            public void markDirty() {
                super.markDirty();
                onContentChanged(inventory);
            }*/
        });

        this.addSlot(new Slot(inventory, 2, 76, 22){
            @Override
            public boolean canInsert(ItemStack item) { return item.isOf(RazuliDustItem.RAZULI_DUST); }
            /*@Override
            public void markDirty() {
                super.markDirty();
                onContentChanged(inventory);
            }*/
        });

        this.addSlot(new Slot(output, 0, 134, 47){
            @Override
            public boolean canInsert(ItemStack item) { return false; }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack item) {
                super.onTakeItem(player, item);
                onTakeOutput(player);
                ((EnchantmentMigratorBlockEntity) blockEntity).onTakeOutput(player);
            }
            
            @Override
            public boolean canTakeItems(PlayerEntity player) {
				//return (hasEnoughLevels(player) && inventory.getStack(0).hasEnchantments() && 
				//	inventory.getStack(1).isOf(Items.BOOK) && inventory.getStack(2).isOf(RazuliDustItem.RAZULI_DUST));
                return (((EnchantmentMigratorBlockEntity) blockEntity).canTakeOutput(player) && hasEnoughLevels(player));
            }

            /*@Override
            public void markDirty() {
                super.markDirty();
                onContentChanged(inventory);
            }*/
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    protected void onTakeOutput(PlayerEntity player) {
        
        EnchantmentHelper.set(inventory.getStack(0), inputMinusTopEnchants);
        inventory.getStack(1).decrement(1);
        inventory.getStack(2).decrement(1);
        inventory.markDirty();

         if (!player.isInCreativeMode()) {
            player.addExperienceLevels(-getXpCost());
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
		EnchantmentMigratorMod.LOGGER.info("Contents change detects");

        updateResult();
		
    }

    private void updateResult() {
        ItemStack inputStack = inventory.getStack(0);

        if (!inputStack.isEmpty() && inputStack.hasEnchantments() && inventory.getStack(1).isOf(Items.BOOK) && inventory.getStack(2).isOf(RazuliDustItem.RAZULI_DUST)) {

            ItemEnchantmentsComponent inputEnchants = EnchantmentHelper.getEnchantments(inputStack);

            var firstEnchant = inputEnchants.getEnchantmentEntries().iterator().next();
            RegistryEntry<Enchantment> firstEnchantKey = firstEnchant.getKey();
            int firstEnchantLevel = firstEnchant.getIntValue();

            ItemEnchantmentsComponent.Builder inputBuilder = new ItemEnchantmentsComponent.Builder(inputEnchants);
            inputBuilder.remove(e -> e.equals(firstEnchantKey));
            this.inputMinusTopEnchants = inputBuilder.build();

            ItemEnchantmentsComponent.Builder outputBuilder = new ItemEnchantmentsComponent.Builder(inputEnchants);
            outputBuilder.remove(e -> true);
            outputBuilder.add(firstEnchantKey, firstEnchantLevel);

            ItemStack outputStack = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.set(outputStack, outputBuilder.build());
            output.setStack(0, outputStack);

            float multiplier = firstEnchantKey.isIn(EnchantmentTags.CURSE) ? 3 : firstEnchantKey.isIn(EnchantmentTags.TREASURE) ? 2 : 1;
            this.xpCost = (int)(Math.round((multiplier * (firstEnchantLevel * 0.5) * (11 - (firstEnchantKey.value().getWeight() * 0.6)))));

			EnchantmentMigratorMod.LOGGER.info("Updating block output result");
        } else {
			output.clear();
			this.xpCost = 0;
		}
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
    
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.size()) { 
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onTakeItem(player, originalStack);
            } else {
                boolean moved = false;

                if (EnchantmentHelper.hasEnchantments(originalStack)) {
                    moved = this.insertItem(originalStack, 0, 1, false);
                }

                else if (originalStack.isOf(Items.BOOK)) {
                    moved = this.insertItem(originalStack, 1, 2, false);
                }

                else if (originalStack.isOf(RazuliDustItem.RAZULI_DUST)) {
                    moved = this.insertItem(originalStack, 2, 3, false);
                }
                if (!moved) {
                    if (invSlot >= this.inventory.size() && invSlot < this.inventory.size() + 27) {
                        moved = this.insertItem(originalStack, this.inventory.size() + 27, this.slots.size(), false);
                    } else if (invSlot >= this.inventory.size() + 27 && invSlot < this.slots.size()) {
                        moved = this.insertItem(originalStack, this.inventory.size(), this.inventory.size() + 27, false);
                    }
                }

                if (!moved) return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    public boolean hasEnoughLevels(PlayerEntity player) { return player.experienceLevel >= this.xpCost || player.isCreative(); }

	public int getXpCost() { return this.xpCost;}


	@Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        output.clear();
    }

    @Override
    public boolean canUse(PlayerEntity player) { return this.inventory.canPlayerUse(player); }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
