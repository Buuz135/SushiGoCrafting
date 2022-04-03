package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.tile.machinery.RollerTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class RollerBlock extends RotatableBlock<RollerTile> {

    public static VoxelShape SHAPE_NORTH = Block.box(2, 0, 1, 14, 0.5, 15);
    public static VoxelShape SHAPE_EAST = Block.box(1, 0, 2, 15, 0.5, 14);

    public RollerBlock() {
        super("roller", Properties.copy(Blocks.OAK_TRAPDOOR), RollerTile.class);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return RollerTile::new;
    }

    @Override
    public Item asItem() {
        return Item.byBlock(this);
    }

    @Override
    public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) {
        this.getTile(worldIn, pos).ifPresent(rollerTile -> rollerTile.onClick(player));
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
