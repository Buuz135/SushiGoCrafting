package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.tile.machinery.RiceCookerTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class RiceCookerBlock extends RotatableBlock<RiceCookerTile> {

    public static VoxelShape SOUTH = Stream.of(
            Block.box(4, 15, 7, 12, 16, 9),
            Block.box(13, 0, 2, 14, 1, 3),
            Block.box(2, 0, 2, 3, 1, 3),
            Block.box(13, 0, 13, 14, 1, 14),
            Block.box(2, 0, 13, 3, 1, 14),
            Block.box(1, 1, 1, 15, 13, 15),
            Block.box(6, 3, 0.5, 10, 7, 1),
            Block.box(7, 4, 0, 9, 6, 0.5),
            Block.box(15, 6, 5, 16, 8, 6),
            Block.box(0, 6, 5, 1, 8, 6),
            Block.box(15, 6, 10, 16, 8, 11),
            Block.box(0, 6, 10, 1, 8, 11),
            Block.box(15, 8, 5, 16, 9, 11),
            Block.box(0, 8, 5, 1, 9, 11),
            Block.box(2, 13, 2, 14, 14, 14),
            Block.box(11, 14, 7, 12, 15, 9),
            Block.box(4, 14, 7, 5, 15, 9)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static VoxelShape WEST = Stream.of(
            Block.box(7, 15, 4, 9, 16, 12),
            Block.box(13, 0, 13, 14, 1, 14),
            Block.box(13, 0, 2, 14, 1, 3),
            Block.box(2, 0, 13, 3, 1, 14),
            Block.box(2, 0, 2, 3, 1, 3),
            Block.box(1, 1, 1, 15, 13, 15),
            Block.box(15, 3, 6, 15.5, 7, 10),
            Block.box(15.5, 4, 7, 16, 6, 9),
            Block.box(10, 6, 15, 11, 8, 16),
            Block.box(10, 6, 0, 11, 8, 1),
            Block.box(5, 6, 15, 6, 8, 16),
            Block.box(5, 6, 0, 6, 8, 1),
            Block.box(5, 8, 15, 11, 9, 16),
            Block.box(5, 8, 0, 11, 9, 1),
            Block.box(2, 13, 2, 14, 14, 14),
            Block.box(7, 14, 11, 9, 15, 12),
            Block.box(7, 14, 4, 9, 15, 5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static VoxelShape NORTH = Stream.of(
            Block.box(4, 15, 7, 12, 16, 9),
            Block.box(2, 0, 13, 3, 1, 14),
            Block.box(13, 0, 13, 14, 1, 14),
            Block.box(2, 0, 2, 3, 1, 3),
            Block.box(13, 0, 2, 14, 1, 3),
            Block.box(1, 1, 1, 15, 13, 15),
            Block.box(6, 3, 15, 10, 7, 15.5),
            Block.box(7, 4, 15.5, 9, 6, 16),
            Block.box(0, 6, 10, 1, 8, 11),
            Block.box(15, 6, 10, 16, 8, 11),
            Block.box(0, 6, 5, 1, 8, 6),
            Block.box(15, 6, 5, 16, 8, 6),
            Block.box(0, 8, 5, 1, 9, 11),
            Block.box(15, 8, 5, 16, 9, 11),
            Block.box(2, 13, 2, 14, 14, 14),
            Block.box(4, 14, 7, 5, 15, 9),
            Block.box(11, 14, 7, 12, 15, 9)
    ).reduce((v1, v2) -> {
        return Shapes.join(v1, v2, BooleanOp.OR);
    }).get();

    public static VoxelShape EAST = Stream.of(
            Block.box(7, 15, 4, 9, 16, 12),
            Block.box(2, 0, 2, 3, 1, 3),
            Block.box(2, 0, 13, 3, 1, 14),
            Block.box(13, 0, 2, 14, 1, 3),
            Block.box(13, 0, 13, 14, 1, 14),
            Block.box(1, 1, 1, 15, 13, 15),
            Block.box(0.5, 3, 6, 1, 7, 10),
            Block.box(0, 4, 7, 0.5, 6, 9),
            Block.box(5, 6, 0, 6, 8, 1),
            Block.box(5, 6, 15, 6, 8, 16),
            Block.box(10, 6, 0, 11, 8, 1),
            Block.box(10, 6, 15, 11, 8, 16),
            Block.box(5, 8, 0, 11, 9, 1),
            Block.box(5, 8, 15, 11, 9, 16),
            Block.box(2, 13, 2, 14, 14, 14),
            Block.box(7, 14, 4, 9, 15, 5),
            Block.box(7, 14, 11, 9, 15, 12)
    ).reduce((v1, v2) -> {
        return Shapes.join(v1, v2, BooleanOp.OR);
    }).get();

    public RiceCookerBlock() {
        super("rice_cooker", Properties.copy(Blocks.IRON_BLOCK), RiceCookerTile.class);
    }

    @Override
    public Item asItem() {
        return Item.byBlock(this);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return RiceCookerTile::new;
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(RotatableBlock.FACING_HORIZONTAL, context.getHorizontalDirection());
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext) {
        Direction direction = state.getValue(RotationType.FOUR_WAY.getProperties()[0]);
        if (direction == Direction.NORTH) {
            return NORTH;
        }
        if (direction == Direction.EAST) {
            return EAST;
        }
        if (direction == Direction.WEST) {
            return WEST;
        }
        return SOUTH;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(RotationType.FOUR_WAY.getProperties()[0]);
        if (direction == Direction.NORTH) {
            return NORTH;
        }
        if (direction == Direction.EAST) {
            return EAST;
        }
        if (direction == Direction.WEST) {
            return WEST;
        }
        return SOUTH;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        this.getTile(worldIn, pos).filter(riceCookerTile -> riceCookerTile.getBar().getProgress() > 0).ifPresent(riceCookerTile -> {
            Direction direction = riceCookerTile.getFacingDirection().getOpposite();
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 1;
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.05D) {
                worldIn.playLocalSound(d0, d1, d2, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 0.0F, true);
            }

            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
            worldIn.addParticle(ParticleTypes.CLOUD, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        });
    }
}
