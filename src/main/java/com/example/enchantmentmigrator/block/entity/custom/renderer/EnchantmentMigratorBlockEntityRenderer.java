package com.example.enchantmentmigrator.block.entity.custom.renderer;

//import java.util.concurrent.ThreadLocalRandom;

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
    //private static final float scale = 0.5f;
    //private static final int xRotation =  (int)(Math.round(Math.random()*360));
    private final int zRotation1 = /*(int)(Math.round(ThreadLocalRandom.current().nextGaussian()*120));*/ EnchantmentMigratorBlockEntity.getZRotation1();
    private final int zRotation2 = /*(int)(Math.round(ThreadLocalRandom.current().nextGaussian()*120));*/ EnchantmentMigratorBlockEntity.getZRotation2();

    public EnchantmentMigratorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(EnchantmentMigratorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, int overlay) {

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        ItemStack itemStack = entity.getStack(0);
        ItemStack bookStack = entity.getStack(1);
        ItemStack razuliStack = entity.getStack(2);

        if (!itemStack.isEmpty()) { renderPlacedItem(itemStack, matrices, vertexConsumers, overlay, itemRenderer, entity, 0.5, 1.5, 0.5, 0, 0, true); }
        if (!bookStack.isEmpty()) { renderPlacedItem(bookStack, matrices, vertexConsumers, overlay, itemRenderer, entity,0.35, 0.8, 0.35, 90, zRotation1, false); }
        if (!razuliStack.isEmpty()) { renderPlacedItem(razuliStack, matrices, vertexConsumers, overlay,itemRenderer, entity, 0.65, 0.8, 0.65, 90, zRotation2, false); }
    }

    private void renderPlacedItem(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay, ItemRenderer itemRenderer, EnchantmentMigratorBlockEntity entity,
         double x, double y, double z, int yRotation, int zRotation, boolean spin) {

        matrices.push();
        matrices.translate(x, y + (entity.isTier4() ? 1 : 0), z);   // position on top of block
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(yRotation));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(zRotation));
        if (spin) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getRotation()));
            matrices.translate(0, entity.getMovement(), 0);
        }
        matrices.scale(0.5f, 0.5f, 0.5f); // shrink the item
        itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, getLightLevel(entity.getWorld(), entity.getPos()),overlay, matrices, vertexConsumers, null, 0);
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
