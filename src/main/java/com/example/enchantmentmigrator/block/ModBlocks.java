package com.example.enchantmentmigrator.block;

import com.example.enchantmentmigrator.EnchantmentMigratorMod;
import com.example.enchantmentmigrator.block.custom.EnchantmentMigratorBlock;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block ENCHANTMENT_MIGRATOR_BLOCK = registerBlock("enchantment_migrator",
        new EnchantmentMigratorBlock(AbstractBlock.Settings.create().
        strength(5F, 1200.0F).
        requiresTool().
        sounds(BlockSoundGroup.STONE).
        luminance((state) -> {return 7;}).
        nonOpaque()
    )
    );


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(EnchantmentMigratorMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(EnchantmentMigratorMod.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        EnchantmentMigratorMod.LOGGER.info("Registering Blocks for "+EnchantmentMigratorMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(ModBlocks.ENCHANTMENT_MIGRATOR_BLOCK);
        });
        
    }

}
