package com.buuz135.sushigocrafting.block.plant;


import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collections;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CustomCropBlock extends CropBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    private final int maxAge;
    private final Supplier<? extends Item> seedSupplier;
    private final Predicate<BlockState> groundPredicate;

    public CustomCropBlock(Properties builder, Supplier<? extends Item> seedSupplier, Predicate<BlockState> groundPredicate) {
        super(builder);
        this.maxAge = Collections.max(getAgeProperty().getPossibleValues());
        this.seedSupplier = seedSupplier;
        this.groundPredicate = groundPredicate;
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public ItemLike getBaseSeedId() {
        return seedSupplier.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return groundPredicate.test(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty());
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public boolean isMaxAge(BlockState state) {
        return state.getValue(getAgeProperty()) == getMaxAge();
    }

    @Override
    protected int getBonemealAgeIncrease(Level worldIn) {
        return super.getBonemealAgeIncrease(worldIn) / 3;
    }

}
