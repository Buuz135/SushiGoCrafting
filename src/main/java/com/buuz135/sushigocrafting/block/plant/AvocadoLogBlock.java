package com.buuz135.sushigocrafting.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class AvocadoLogBlock extends RotatedPillarBlock {

    public static VoxelShape SHAPE_Y = Block.makeCuboidShape(3, 0, 3, 13, 16, 13);
    public static VoxelShape SHAPE_Z = Block.makeCuboidShape(3, 3, 0, 13, 13, 16);
    public static VoxelShape SHAPE_X = Block.makeCuboidShape(0, 3, 3, 16, 13, 13);

    public AvocadoLogBlock(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext) {
        Direction.Axis axis = state.get(AXIS);
        if (axis == Direction.Axis.Z) {
            return SHAPE_Z;
        }
        if (axis == Direction.Axis.X) {
            return SHAPE_X;
        }
        return SHAPE_Y;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction.Axis axis = state.get(AXIS);
        if (axis == Direction.Axis.Z) {
            return SHAPE_Z;
        }
        if (axis == Direction.Axis.X) {
            return SHAPE_X;
        }
        return SHAPE_Y;
    }
}
