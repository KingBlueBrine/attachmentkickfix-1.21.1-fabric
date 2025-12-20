package com.example.enchantmentmigrator.block.entity.custom.renderer;

import com.example.enchantmentmigrator.block.entity.custom.EnchantmentMigratorBlockEntity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class EnchantmentMigratorBlockEntityRenderer implements BlockEntityRenderer<EnchantmentMigratorBlockEntity> {
    private static final float scale = 0.5f;

    public EnchantmentMigratorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(EnchantmentMigratorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay) {

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        ItemStack itemStack = entity.getStack(0);
        if (!itemStack.isEmpty()) { renderPlacedItem(itemStack, matrices, vertexConsumers, overlay, 0.5, 1.5, 0.5, itemRenderer, entity, 0F, true); }
        ItemStack bookStack = entity.getStack(1);
        if (!bookStack.isEmpty()) { renderPlacedItem(bookStack, matrices, vertexConsumers, overlay, 0.35, 0.8, 0.35, itemRenderer, entity, 90F, false); }
        ItemStack razuliStack = entity.getStack(2);
        if (!razuliStack.isEmpty()) { renderPlacedItem(razuliStack, matrices, vertexConsumers, overlay, 0.65, 0.8, 0.65, itemRenderer, entity, 90F, false); }
    }

    private void renderPlacedItem(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, 
            int overlay, double x, double y, double z, ItemRenderer itemRenderer, EnchantmentMigratorBlockEntity entity, float rotation, boolean spin) {
            matrices.push();
            matrices.translate(x, y, z);   // position on top of block
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation));
            if (spin) {
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getRotation()));
                matrices.translate(0, entity.getMovement(), 0);
            }
            matrices.scale(scale, scale, scale); // shrink the item
            itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, getLightLevel(entity.getWorld(), entity.getPos()),overlay, matrices, vertexConsumers, null, 0);
            matrices.pop();
        }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

    

}
