package com.buuz135.sushigocrafting.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WaterCropBlock extends CustomCropBlock implements LiquidBlockContainer {

    public WaterCropBlock(Properties builder, Supplier<? extends Item> seedSupplier, Predicate<BlockState> groundPredicate) {
        super(builder, seedSupplier, groundPredicate);
    }

    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return false;
    }

    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return worldIn.getFluidState(pos).is(FluidTags.WATER) && super.isValidBonemealTarget(worldIn, pos, state, isClient);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? this.defaultBlockState() : null;
    }

}
