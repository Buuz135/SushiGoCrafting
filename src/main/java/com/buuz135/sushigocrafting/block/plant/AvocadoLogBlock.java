package com.buuz135.sushigocrafting.block.plant;

import com.buuz135.sushigocrafting.block.SushiGoCraftingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class AvocadoLogBlock extends SushiGoCraftingBlock {

    public static VoxelShape SHAPE = Block.makeCuboidShape(3, 0, 3, 13, 16, 13);

    public AvocadoLogBlock(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
