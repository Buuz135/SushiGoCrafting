package com.buuz135.sushigocrafting.block.machinery;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.CoolerBoxTile;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CoolerBoxBlock extends RotatableBlock<CoolerBoxTile> {

    public static VoxelShape SHAPE_NORTH = Block.box(0.5, 0, 1.5, 15.5, 14, 14.5);
    public static VoxelShape SHAPE_EAST = Block.box(1.5, 0, 0.5, 14.5, 14, 15.5);

    public CoolerBoxBlock() {
        super("cooler_box", Properties.copy(Blocks.STONE), CoolerBoxTile.class);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return CoolerBoxTile::new;
    }

    @Override
    public Item asItem() {
        return   Item.byBlock(this);
    }

    @Override
    public BlockEntityType getTileEntityType() {
        return SushiContent.TileEntities.COOLER_BOX.get();
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
    public NonNullList<ItemStack> getDynamicDrops(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        return NonNullList.create();
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag compoundnbt = stack.getTagElement("BlockEntityTag");
        if (compoundnbt != null) {
            if (compoundnbt.contains("input")) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(compoundnbt.getCompound("input"), nonnulllist);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist) {
                    if (!itemstack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableComponent iformattabletextcomponent = itemstack.getHoverName().copy();
                            iformattabletextcomponent.append(" x").append(String.valueOf(itemstack.getCount())).withStyle(ChatFormatting.DARK_AQUA);
                            tooltip.add(iformattabletextcomponent);
                        }
                    }
                }

                if (j - i > 0) {
                    tooltip.add((new TranslatableComponent("container.shulkerBox.more", j - i)).withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_AQUA));
                }
            }
        }

    }

}
