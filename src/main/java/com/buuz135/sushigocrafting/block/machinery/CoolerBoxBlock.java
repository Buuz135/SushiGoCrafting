package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.item.SushiDataComponent;
import com.buuz135.sushigocrafting.tile.machinery.CoolerBoxTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class CoolerBoxBlock extends RotatableBlock<CoolerBoxTile> {

    public static VoxelShape SHAPE_NORTH = Block.box(0.5, 0, 1.5, 15.5, 14, 14.5);
    public static VoxelShape SHAPE_EAST = Block.box(1.5, 0, 0.5, 14.5, 14, 15.5);

    public CoolerBoxBlock() {
        super("cooler_box", Properties.ofFullCopy(Blocks.STONE), CoolerBoxTile.class);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return CoolerBoxTile::new;
    }

    @Override
    public Item asItem() {
        return Item.byBlock(this);
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

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootParams.Builder builder) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        ItemStack stack = new ItemStack(this);
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof CoolerBoxTile coolerBoxTile) {
            copyTo(coolerBoxTile, stack);
        }
        stacks.add(stack);
        return stacks;
    }

    @Override
    public NonNullList<ItemStack> getDynamicDrops(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        return NonNullList.create();
    }

    protected void copyTo(CoolerBoxTile tile, ItemStack stack) {
        if (!tile.isEmpty()) {
            stack.set(SushiDataComponent.TILE.get(), tile.saveWithoutMetadata(tile.getLevel().registryAccess()));
        }
    }

    protected void copyFrom(ItemStack stack, CoolerBoxTile tile) {
        if (stack.has(SushiDataComponent.TILE.get())) {
            tile.loadAdditional(stack.get(SushiDataComponent.TILE.get()), tile.getLevel().registryAccess());
            tile.markForUpdate();
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof CoolerBoxTile tile) {
            copyFrom(stack, tile);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, tooltip, tooltipFlag);
        if (stack.has(SushiDataComponent.TILE.get())) {
            var tileData = stack.get(SushiDataComponent.TILE.get());

            if (tileData.contains("input")) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(tileData.getCompoundOrEmpty("input"), nonnulllist, Minecraft.getInstance().level.registryAccess());
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist) {
                    if (!itemstack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableComponent iformattabletextcomponent = itemstack.getHoverName().copy();
                            iformattabletextcomponent.append(" x").append(String.valueOf(itemstack.getCount())).withStyle(ChatFormatting.DARK_AQUA);
                            tooltip.accept(iformattabletextcomponent);
                        }
                    }
                }

                if (j - i > 0) {
                    tooltip.accept(Component.translatable("container.shulkerBox.more", j - i).withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_AQUA));
                }
            }
        }
    }

}
