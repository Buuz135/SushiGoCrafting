package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.CuttingBoardTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class CuttingBoardBlock extends RotatableBlock<CuttingBoardTile> {

    public static VoxelShape SHAPE_NORTH = Block.box(1, 0, 3, 15, 1, 13);
    public static VoxelShape SHAPE_EAST = Block.box(3, 0, 1, 13, 1, 15);

    public CuttingBoardBlock() {
        super("cutting_board", Properties.copy(Blocks.OAK_TRAPDOOR), CuttingBoardTile.class);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return CuttingBoardTile::new;
    }

    @Override
    public BlockEntityType getTileEntityType() {
        return SushiContent.TileEntities.CUTTING_BOARD.get();
    }

    @Override
    public Item asItem() {
        return Item.byBlock(this);
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext) {
        Direction direction = state.getValue(RotatableBlock.FACING_HORIZONTAL);
        return direction == Direction.NORTH || direction == Direction.SOUTH ? SHAPE_NORTH : SHAPE_EAST;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(RotatableBlock.FACING_HORIZONTAL);
        return direction == Direction.NORTH || direction == Direction.SOUTH ? SHAPE_NORTH : SHAPE_EAST;
    }
}
