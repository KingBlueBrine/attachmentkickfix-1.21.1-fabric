package com.example.enchantmentmigrator.datagen;

import java.util.concurrent.CompletableFuture;

import com.example.enchantmentmigrator.block.ModBlocks;
import com.example.enchantmentmigrator.item.RazuliDustItem;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        // Razuli Dust
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RazuliDustItem.RAZULI_DUST)
                .input(Items.LAPIS_LAZULI)
                .input(Items.REDSTONE)
                .input(Items.REDSTONE)
                .input(Items.REDSTONE)
                .input(Items.REDSTONE)
                .criterion(hasItem(Items.LAPIS_LAZULI), conditionsFromItem(Items.LAPIS_LAZULI))
                .offerTo(exporter);

        // Enchantment Migrator Block
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.ENCHANTMENT_MIGRATOR_BLOCK)
                .pattern("NSN")
                .pattern("DED")
                .pattern("GAG")
                .input('N', Items.NETHERITE_INGOT)
                .input('S', Items.NETHER_STAR)
                .input('D', Items.DIAMOND)
                .input('E', Items.ENCHANTING_TABLE)
                .input('G', Items.GOLD_BLOCK)
                .input('A', Items.ANVIL)
                .criterion(hasItem(Items.ENCHANTING_TABLE), conditionsFromItem(Items.ENCHANTING_TABLE))
                .offerTo(exporter);
    }

}
