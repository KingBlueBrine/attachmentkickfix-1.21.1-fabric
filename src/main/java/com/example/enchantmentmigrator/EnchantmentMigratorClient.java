package com.example.enchantmentmigrator;

import com.example.enchantmentmigrator.block.entity.ModBlockEntities;
import com.example.enchantmentmigrator.block.entity.custom.renderer.EnchantmentMigratorBlockEntityRenderer;
import com.example.enchantmentmigrator.screen.ModScreenHandlers;
import com.example.enchantmentmigrator.screen.custom.EnchantmentMigratorScreen;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.gui.screen.ingame.HandledScreens;


public class EnchantmentMigratorClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.ENCHANTMENT_MIGRATOR_BE, EnchantmentMigratorBlockEntityRenderer::new);
        HandledScreens.register(ModScreenHandlers.ENCHANTMENT_MIGRATOR_SH, EnchantmentMigratorScreen::new);
    
    }

}
