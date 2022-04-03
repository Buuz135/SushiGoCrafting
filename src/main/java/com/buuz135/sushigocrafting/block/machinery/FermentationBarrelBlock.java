package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.tile.machinery.FermentationBarrelTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Item;
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
import java.util.Random;
import java.util.stream.Stream;

public class FermentationBarrelBlock extends RotatableBlock<FermentationBarrelTile> {

    public static VoxelShape NORMAL = Stream.of(
            Block.box(2, 1, 1, 14, 15, 2),
            Block.box(2, 1, 14, 14, 15, 15),
            Block.box(2, 0, 2, 14, 1, 14),
            Block.box(2, 14.5, 2, 14, 15.5, 14),
            Block.box(1, 1, 2, 2, 15, 14),
            Block.box(14, 1, 2, 15, 15, 14),
            Block.box(4, 15, 6, 5, 16, 10),
            Block.box(11, 15, 6, 12, 16, 10)
    ).reduce((v1, v2) -> {
        return Shapes.join(v1, v2, BooleanOp.OR);
    }).get();


    public FermentationBarrelBlock() {
        super("fermentation_barrel", Properties.copy(Blocks.OAK_WOOD), FermentationBarrelTile.class);
    }

    @Override
    public Item asItem() {
        return Item.byBlock(this);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return FermentationBarrelTile::new;
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.NONE;
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext) {
        return NORMAL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return NORMAL;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        this.getTile(worldIn, pos).filter(riceCookerTile -> riceCookerTile.getBar().getProgress() > 0).ifPresent(riceCookerTile -> {
            double d0 = (double) pos.getX() + 0.5D - worldIn.random.nextDouble() / 2D + 0.25D;
            double d1 = (double) pos.getY() + 1.01D;
            double d2 = (double) pos.getZ() + 0.5D - worldIn.random.nextDouble() / 2D + 0.25D;

            worldIn.addParticle(ParticleTypes.BUBBLE_POP, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        });
    }
}
