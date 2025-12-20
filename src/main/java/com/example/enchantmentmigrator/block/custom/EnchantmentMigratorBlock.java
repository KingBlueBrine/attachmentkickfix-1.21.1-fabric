package com.example.enchantmentmigrator.block.custom;

import org.jetbrains.annotations.Nullable;

import com.example.enchantmentmigrator.block.entity.ModBlockEntities;
import com.example.enchantmentmigrator.block.entity.custom.EnchantmentMigratorBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.screen.NamedScreenHandlerFactory;

@SuppressWarnings("unused")
public class EnchantmentMigratorBlock extends BlockWithEntity implements BlockEntityProvider {
    
    public static final MapCodec<EnchantmentMigratorBlock> CODEC = EnchantmentMigratorBlock.createCodec(EnchantmentMigratorBlock::new);

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 12, 16);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return SHAPE;
    }

    
    public EnchantmentMigratorBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnchantmentMigratorBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof EnchantmentMigratorBlockEntity) {
                ItemScatterer.spawn(world, pos, ((EnchantmentMigratorBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        //if (!(world.getBlockEntity(pos) instanceof EnchantmentMigratorBlockEntity blockEntity)) {
        //    return super.onUse(state, world, pos, player, hit);
        //}
        if (!world.isClient) {
            //BlockEntity blockEntity1 = world.getBlockEntity(pos);
            //if (blockEntity1 instanceof EnchantmentMigratorBlockEntity embe) {
            NamedScreenHandlerFactory screenHandlerFactory = ((EnchantmentMigratorBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> {
        if (blockEntity instanceof EnchantmentMigratorBlockEntity migrator) {
            migrator.tick();
        }
    };
}

}
