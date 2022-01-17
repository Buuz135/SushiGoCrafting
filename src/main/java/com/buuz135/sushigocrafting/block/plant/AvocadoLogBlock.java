package com.buuz135.sushigocrafting.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class AvocadoLogBlock extends RotatedPillarBlock {

    public static VoxelShape SHAPE_Y = Block.box(3, 0, 3, 13, 16, 13);
    public static VoxelShape SHAPE_Z = Block.box(3, 3, 0, 13, 13, 16);
    public static VoxelShape SHAPE_X = Block.box(0, 3, 3, 16, 13, 13);

    public AvocadoLogBlock(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext) {
        Direction.Axis axis = state.getValue(AXIS);
        if (axis == Direction.Axis.Z) {
            return SHAPE_Z;
        }
        if (axis == Direction.Axis.X) {
            return SHAPE_X;
        }
        return SHAPE_Y;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction.Axis axis = state.getValue(AXIS);
        if (axis == Direction.Axis.Z) {
            return SHAPE_Z;
        }
        if (axis == Direction.Axis.X) {
            return SHAPE_X;
        }
        return SHAPE_Y;
    }
}
