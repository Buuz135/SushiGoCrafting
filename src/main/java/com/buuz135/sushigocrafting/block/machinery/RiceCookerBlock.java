package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.RiceCookerTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class RiceCookerBlock extends RotatableBlock<RiceCookerTile> {

    public static VoxelShape SOUTH = Stream.of(
            Block.makeCuboidShape(4, 15, 7, 12, 16, 9),
            Block.makeCuboidShape(13, 0, 2, 14, 1, 3),
            Block.makeCuboidShape(2, 0, 2, 3, 1, 3),
            Block.makeCuboidShape(13, 0, 13, 14, 1, 14),
            Block.makeCuboidShape(2, 0, 13, 3, 1, 14),
            Block.makeCuboidShape(1, 1, 1, 15, 13, 15),
            Block.makeCuboidShape(6, 3, 0.5, 10, 7, 1),
            Block.makeCuboidShape(7, 4, 0, 9, 6, 0.5),
            Block.makeCuboidShape(15, 6, 5, 16, 8, 6),
            Block.makeCuboidShape(0, 6, 5, 1, 8, 6),
            Block.makeCuboidShape(15, 6, 10, 16, 8, 11),
            Block.makeCuboidShape(0, 6, 10, 1, 8, 11),
            Block.makeCuboidShape(15, 8, 5, 16, 9, 11),
            Block.makeCuboidShape(0, 8, 5, 1, 9, 11),
            Block.makeCuboidShape(2, 13, 2, 14, 14, 14),
            Block.makeCuboidShape(11, 14, 7, 12, 15, 9),
            Block.makeCuboidShape(4, 14, 7, 5, 15, 9)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static VoxelShape WEST = Stream.of(
            Block.makeCuboidShape(7, 15, 4, 9, 16, 12),
            Block.makeCuboidShape(13, 0, 13, 14, 1, 14),
            Block.makeCuboidShape(13, 0, 2, 14, 1, 3),
            Block.makeCuboidShape(2, 0, 13, 3, 1, 14),
            Block.makeCuboidShape(2, 0, 2, 3, 1, 3),
            Block.makeCuboidShape(1, 1, 1, 15, 13, 15),
            Block.makeCuboidShape(15, 3, 6, 15.5, 7, 10),
            Block.makeCuboidShape(15.5, 4, 7, 16, 6, 9),
            Block.makeCuboidShape(10, 6, 15, 11, 8, 16),
            Block.makeCuboidShape(10, 6, 0, 11, 8, 1),
            Block.makeCuboidShape(5, 6, 15, 6, 8, 16),
            Block.makeCuboidShape(5, 6, 0, 6, 8, 1),
            Block.makeCuboidShape(5, 8, 15, 11, 9, 16),
            Block.makeCuboidShape(5, 8, 0, 11, 9, 1),
            Block.makeCuboidShape(2, 13, 2, 14, 14, 14),
            Block.makeCuboidShape(7, 14, 11, 9, 15, 12),
            Block.makeCuboidShape(7, 14, 4, 9, 15, 5)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static VoxelShape NORTH = Stream.of(
            Block.makeCuboidShape(4, 15, 7, 12, 16, 9),
            Block.makeCuboidShape(2, 0, 13, 3, 1, 14),
            Block.makeCuboidShape(13, 0, 13, 14, 1, 14),
            Block.makeCuboidShape(2, 0, 2, 3, 1, 3),
            Block.makeCuboidShape(13, 0, 2, 14, 1, 3),
            Block.makeCuboidShape(1, 1, 1, 15, 13, 15),
            Block.makeCuboidShape(6, 3, 15, 10, 7, 15.5),
            Block.makeCuboidShape(7, 4, 15.5, 9, 6, 16),
            Block.makeCuboidShape(0, 6, 10, 1, 8, 11),
            Block.makeCuboidShape(15, 6, 10, 16, 8, 11),
            Block.makeCuboidShape(0, 6, 5, 1, 8, 6),
            Block.makeCuboidShape(15, 6, 5, 16, 8, 6),
            Block.makeCuboidShape(0, 8, 5, 1, 9, 11),
            Block.makeCuboidShape(15, 8, 5, 16, 9, 11),
            Block.makeCuboidShape(2, 13, 2, 14, 14, 14),
            Block.makeCuboidShape(4, 14, 7, 5, 15, 9),
            Block.makeCuboidShape(11, 14, 7, 12, 15, 9)
    ).reduce((v1, v2) -> {
        return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
    }).get();

    public static VoxelShape EAST = Stream.of(
            Block.makeCuboidShape(7, 15, 4, 9, 16, 12),
            Block.makeCuboidShape(2, 0, 2, 3, 1, 3),
            Block.makeCuboidShape(2, 0, 13, 3, 1, 14),
            Block.makeCuboidShape(13, 0, 2, 14, 1, 3),
            Block.makeCuboidShape(13, 0, 13, 14, 1, 14),
            Block.makeCuboidShape(1, 1, 1, 15, 13, 15),
            Block.makeCuboidShape(0.5, 3, 6, 1, 7, 10),
            Block.makeCuboidShape(0, 4, 7, 0.5, 6, 9),
            Block.makeCuboidShape(5, 6, 0, 6, 8, 1),
            Block.makeCuboidShape(5, 6, 15, 6, 8, 16),
            Block.makeCuboidShape(10, 6, 0, 11, 8, 1),
            Block.makeCuboidShape(10, 6, 15, 11, 8, 16),
            Block.makeCuboidShape(5, 8, 0, 11, 9, 1),
            Block.makeCuboidShape(5, 8, 15, 11, 9, 16),
            Block.makeCuboidShape(2, 13, 2, 14, 14, 14),
            Block.makeCuboidShape(7, 14, 4, 9, 15, 5),
            Block.makeCuboidShape(7, 14, 11, 9, 15, 12)
    ).reduce((v1, v2) -> {
        return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
    }).get();

    public RiceCookerBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), RiceCookerTile.class);
    }

    @Override
    public Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) Item.getItemFromBlock(this));
        return super.asItem();
    }

    @Override
    public IFactory<RiceCookerTile> getTileEntityFactory() {
        return RiceCookerTile::new;
    }

    @Override
    public TileEntityType getTileEntityType() {
        return SushiContent.TileEntities.RICE_COOKER.get();
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(RotatableBlock.FACING_HORIZONTAL, context.getPlacementHorizontalFacing());
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext) {
        Direction direction = state.get(RotationType.FOUR_WAY.getProperties()[0]);
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
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(RotationType.FOUR_WAY.getProperties()[0]);
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
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        this.getTile(worldIn, pos).filter(riceCookerTile -> riceCookerTile.getBar().getProgress() > 0).ifPresent(riceCookerTile -> {
            Direction direction = riceCookerTile.getFacingDirection().getOpposite();
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 1;
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.05D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 0.0F, true);
            }

            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : d4;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : d4;
            worldIn.addParticle(ParticleTypes.CLOUD, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        });
    }
}
