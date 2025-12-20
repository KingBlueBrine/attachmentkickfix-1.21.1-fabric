package com.example.enchantmentmigrator.screen;

import com.example.enchantmentmigrator.EnchantmentMigratorMod;
import com.example.enchantmentmigrator.screen.custom.EnchantmentMigratorScreenHandler;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {

    public static final ScreenHandlerType<EnchantmentMigratorScreenHandler> ENCHANTMENT_MIGRATOR_SH =
        Registry.register(Registries.SCREEN_HANDLER, Identifier.of(EnchantmentMigratorMod.MOD_ID, "enchantment_migrator_sh"),
            new ExtendedScreenHandlerType<>(EnchantmentMigratorScreenHandler::new, BlockPos.PACKET_CODEC));
    
    public static void registerScreenHandlers() {
        EnchantmentMigratorMod.LOGGER.info("Registering Screen Handler for "+ EnchantmentMigratorMod.MOD_ID);
    }

}
