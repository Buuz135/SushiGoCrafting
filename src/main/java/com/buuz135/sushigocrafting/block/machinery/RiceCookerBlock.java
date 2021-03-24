package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.RiceCookerTile;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class RiceCookerBlock extends RotatableBlock<RiceCookerTile> {

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

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        this.getTile(worldIn, pos).filter(riceCookerTile -> riceCookerTile.getBar().getProgress() > 0 && riceCookerTile.canStart()).ifPresent(riceCookerTile -> {
            Direction direction = riceCookerTile.getFacingDirection();
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
