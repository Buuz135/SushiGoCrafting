package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.RollerTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class RollerBlock extends RotatableBlock<RollerTile> {

    public static VoxelShape SHAPE_NORTH = Block.makeCuboidShape(2, 0, 1, 14, 0.5, 15);
    public static VoxelShape SHAPE_EAST = Block.makeCuboidShape(1, 0, 2, 15, 0.5, 14);

    public RollerBlock() {
        super(Properties.from(Blocks.OAK_TRAPDOOR), RollerTile.class);
    }

    @Override
    public IFactory<RollerTile> getTileEntityFactory() {
        return RollerTile::new;
    }

    @Override
    public Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) Item.getItemFromBlock(this));
        return super.asItem();
    }

    @Override
    public TileEntityType getTileEntityType() {
        return SushiContent.TileEntities.ROLLER.get();
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        this.getTile(worldIn, pos).ifPresent(rollerTile -> rollerTile.onClick(player));
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
