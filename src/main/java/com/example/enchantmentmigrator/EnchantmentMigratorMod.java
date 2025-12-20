package com.example.enchantmentmigrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.enchantmentmigrator.block.ModBlocks;
import com.example.enchantmentmigrator.block.entity.ModBlockEntities;
import com.example.enchantmentmigrator.item.RazuliDustItem;
import com.example.enchantmentmigrator.screen.ModScreenHandlers;
import com.example.enchantmentmigrator.sound.ModSounds;

import net.fabricmc.api.ModInitializer;

public class EnchantmentMigratorMod implements ModInitializer {
    public static final String MOD_ID = "enchantmentmigrator";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        RazuliDustItem.registerModItems();
        //EnchantmentMigratorBlock.registerEnchantmentMigratorBlock();
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerBlockEntities();
        //Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MOD_ID, "enchantment_migrator_be"), EnchantmentMigratorBlockEntity.ENCHANTMENT_MIGRATOR_BE);
        ModSounds.registerSounds();
        ModScreenHandlers.registerScreenHandlers();
    }
}