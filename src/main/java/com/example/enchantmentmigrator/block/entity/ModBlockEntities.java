package com.example.enchantmentmigrator.block.entity;

import com.example.enchantmentmigrator.EnchantmentMigratorMod;
import com.example.enchantmentmigrator.block.ModBlocks;
import com.example.enchantmentmigrator.block.entity.custom.EnchantmentMigratorBlockEntity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<EnchantmentMigratorBlockEntity> ENCHANTMENT_MIGRATOR_BE = 
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(EnchantmentMigratorMod.MOD_ID, "enchantment_migrator_be"),
            BlockEntityType.Builder.create(EnchantmentMigratorBlockEntity::new, ModBlocks.ENCHANTMENT_MIGRATOR_BLOCK).build(null));
    
    public static void registerBlockEntities() {
        EnchantmentMigratorMod.LOGGER.info("Registering Block Entity for "+ EnchantmentMigratorMod.MOD_ID);
    }
}