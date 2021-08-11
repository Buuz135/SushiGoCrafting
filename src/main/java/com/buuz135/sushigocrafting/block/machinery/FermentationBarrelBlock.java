package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.FermentationBarrelTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.stream.Stream;

public class FermentationBarrelBlock extends RotatableBlock<FermentationBarrelTile> {

    public static VoxelShape NORMAL = Stream.of(
            Block.makeCuboidShape(2, 1, 1, 14, 15, 2),
            Block.makeCuboidShape(2, 1, 14, 14, 15, 15),
            Block.makeCuboidShape(2, 0, 2, 14, 1, 14),
            Block.makeCuboidShape(2, 14.5, 2, 14, 15.5, 14),
            Block.makeCuboidShape(1, 1, 2, 2, 15, 14),
            Block.makeCuboidShape(14, 1, 2, 15, 15, 14),
            Block.makeCuboidShape(4, 15, 6, 5, 16, 10),
            Block.makeCuboidShape(11, 15, 6, 12, 16, 10)
    ).reduce((v1, v2) -> {
        return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
    }).get();


    public FermentationBarrelBlock() {
        super(Properties.from(Blocks.OAK_WOOD), FermentationBarrelTile.class);
    }

    @Override
    public Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) Item.getItemFromBlock(this));
        return super.asItem();
    }

    @Override
    public IFactory<FermentationBarrelTile> getTileEntityFactory() {
        return FermentationBarrelTile::new;
    }

    @Override
    public TileEntityType getTileEntityType() {
        return SushiContent.TileEntities.FERMENTATION_BARREL.get();
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.NONE;
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext) {
        return NORMAL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return NORMAL;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        this.getTile(worldIn, pos).filter(riceCookerTile -> riceCookerTile.getBar().getProgress() > 0).ifPresent(riceCookerTile -> {
            double d0 = (double) pos.getX() + 0.5D - worldIn.rand.nextDouble() / 2D + 0.25D;
            double d1 = (double) pos.getY() + 1.01D;
            double d2 = (double) pos.getZ() + 0.5D - worldIn.rand.nextDouble() / 2D + 0.25D;

            worldIn.addParticle(ParticleTypes.BUBBLE_POP, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        });
    }
}
