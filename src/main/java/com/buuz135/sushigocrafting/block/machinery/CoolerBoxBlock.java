package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.CoolerBoxTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class CoolerBoxBlock extends RotatableBlock<CoolerBoxTile> {

    public static VoxelShape SHAPE_NORTH = Block.makeCuboidShape(0.5, 0, 1.5, 15.5, 14, 14.5);
    public static VoxelShape SHAPE_EAST = Block.makeCuboidShape(1.5, 0, 0.5, 14.5, 14, 15.5);

    public CoolerBoxBlock() {
        super(Properties.from(Blocks.STONE), CoolerBoxTile.class);
    }

    @Override
    public IFactory<CoolerBoxTile> getTileEntityFactory() {
        return CoolerBoxTile::new;
    }

    @Override
    public Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) Item.getItemFromBlock(this));
        return super.asItem();
    }

    @Override
    public TileEntityType getTileEntityType() {
        return SushiContent.TileEntities.COOLER_BOX.get();
    }


    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext) {
        Direction direction = state.get(RotatableBlock.FACING_HORIZONTAL);
        return direction == Direction.NORTH || direction == Direction.SOUTH ? SHAPE_NORTH : SHAPE_EAST;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(RotatableBlock.FACING_HORIZONTAL);
        return direction == Direction.NORTH || direction == Direction.SOUTH ? SHAPE_NORTH : SHAPE_EAST;
    }

}
