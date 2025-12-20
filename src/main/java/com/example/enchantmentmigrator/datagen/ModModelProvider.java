package com.example.enchantmentmigrator.datagen;

import static com.example.enchantmentmigrator.block.ModBlocks.ENCHANTMENT_MIGRATOR_BLOCK;
import com.example.enchantmentmigrator.item.RazuliDustItem;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        //gen.registerSimpleCubeAll(ENCHANTMENT_MIGRATOR_BLOCK);
        /*Identifier modelId = ModelIds.getBlockModelId(ENCHANTMENT_MIGRATOR_BLOCK);

        gen.blockStateCollector.accept(
            BlockStateModelGenerator.createSingletonBlockState(ENCHANTMENT_MIGRATOR_BLOCK, modelId));

        gen.modelCollector.accept(modelId, () -> {
            return new JsonUnbakedModel(
                    Identifier.of("minecraft", "block/block"),
                    java.util.List.of(
                            new ModelElement(
                                    new Vector3f(0, 0, 0),
                                    new Vector3f(16, 12, 16),
                                    java.util.Map.of(
                                            Direction.DOWN, new ModelElementFace(null, null, "#bottom", null),
                                            Direction.UP, new ModelElementFace(null, null, "#top", null),
                                            Direction.NORTH, new ModelElementFace(null, null, "#side", null),
                                            Direction.SOUTH, new ModelElementFace(null, null, "#side", null),
                                            Direction.WEST, new ModelElementFace(null, null, "#side", null),
                                            Direction.EAST, new ModelElementFace(null, null, "#side", null)
                                    ),
                                    null,
                                    true
                                )
                            ), 
                            null, null, null, null, null);
        })

        Identifier modelId = Models.CUBE_BOTTOM_TOP.upload(
                ENCHANTMENT_MIGRATOR_BLOCK,
                new TextureMap()
                        .put(TextureKey.TOP,
                                TextureMap.getSubId(ENCHANTMENT_MIGRATOR_BLOCK, "_top"))
                        .put(TextureKey.SIDE,
                                TextureMap.getSubId(ENCHANTMENT_MIGRATOR_BLOCK, "_side"))
                        .put(TextureKey.BOTTOM,
                                TextureMap.getSubId(ENCHANTMENT_MIGRATOR_BLOCK, "_bottom")),
                gen.modelCollector
        );

        gen.blockStateCollector.accept(
                BlockStateModelGenerator.createSingletonBlockState(
                        ENCHANTMENT_MIGRATOR_BLOCK, modelId
                )
        );*/

        gen.registerSimpleState(ENCHANTMENT_MIGRATOR_BLOCK);

    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        //gen.register(ENCHANTMENT_MIGRATOR_BLOCK.asItem(), Models.GENERATED);
        gen.register(RazuliDustItem.RAZULI_DUST, Models.GENERATED);
    }
}
