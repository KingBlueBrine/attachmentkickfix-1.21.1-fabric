package com.example.enchantmentmigrator.item;

import com.example.enchantmentmigrator.EnchantmentMigratorMod;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RazuliDustItem {
    public static final Item RAZULI_DUST = registerItem("razuli_dust", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(EnchantmentMigratorMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        EnchantmentMigratorMod.LOGGER.info("Registering Items for " + EnchantmentMigratorMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(RAZULI_DUST);
        });
    }

}
