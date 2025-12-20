package com.example.enchantmentmigrator.screen.custom;

import com.example.enchantmentmigrator.block.entity.custom.EnchantmentMigratorBlockEntity;
import com.example.enchantmentmigrator.item.RazuliDustItem;
import com.example.enchantmentmigrator.screen.ModScreenHandlers;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class EnchantmentMigratorScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final EnchantmentMigratorBlockEntity blockEntity;

    public EnchantmentMigratorScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(pos), new ArrayPropertyDelegate(4));
    }

    public EnchantmentMigratorScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {

        super(ModScreenHandlers.ENCHANTMENT_MIGRATOR_SH, syncId);
        this.inventory = ((Inventory) blockEntity);
        this.blockEntity = ((EnchantmentMigratorBlockEntity) blockEntity);
        this.propertyDelegate = arrayPropertyDelegate;

        this.addSlot(new Slot(inventory, 0, 27, 47) {
            @Override
            public boolean canInsert(ItemStack item) {
                return (item.hasEnchantments());
            }
        });
        this.addSlot(new Slot(inventory, 1, 76, 47){
            @Override
            public boolean canInsert(ItemStack item) {
                return item.isOf(Items.BOOK);
            }
        });
        this.addSlot(new Slot(inventory, 2, 76, 22){
            @Override
            public boolean canInsert(ItemStack item) {
                return item.isOf(RazuliDustItem.RAZULI_DUST);
            }
        });
        this.addSlot(new Slot(inventory, 3, 134, 47){
            @Override
            public boolean canInsert(ItemStack item) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(arrayPropertyDelegate);
    }

    public int getXpCost() {
        return propertyDelegate.get(0);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
    
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.size()) { 
                // Shift-clicked from the block inventory
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Shift-clicked from player inventory
                boolean moved = false;

                // Slot 0 = enchantable item
                if (originalStack.getItem().isEnchantable(originalStack)) {
                    moved = this.insertItem(originalStack, 0, 1, false);
                }

                // Slot 1 = book
                else if (originalStack.isOf(Items.BOOK)) {
                    moved = this.insertItem(originalStack, 1, 2, false);
                }

                // Slot 2 = lapis
                else if (originalStack.isOf(RazuliDustItem.RAZULI_DUST)) {
                    moved = this.insertItem(originalStack, 2, 3, false);
                }

                // If it didn't fit in a special slot, try regular player inventory range
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


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public boolean validOutput() {
        return propertyDelegate.get(0) > 0;
    }

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

    //@Override
    public ItemStack onTakeOutput(PlayerEntity player, ItemStack stack) {
        if (blockEntity.canTakeOutput(player)) {
            return stack;
        }
        return ItemStack.EMPTY;
    }


}
