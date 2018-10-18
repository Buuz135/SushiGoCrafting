package com.buuz135.sushigocrafting.block.crop;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockRiceCrop extends BlockCrops {

    public BlockRiceCrop() {
        setRegistryName(SushiGoCrafting.MOD_ID, "rice_crop");
        setTranslationKey(getRegistryName().toString());
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getMaterial() == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return super.canBlockStay(worldIn, pos, state);
    }

    protected Item getSeed() {
        return SushiContent.Items.RICE_SEED_ITEM;
    }

    protected Item getCrop() {
        return Items.WHEAT;
    }

    public static class RiceSeedItem extends Item {

        public RiceSeedItem() {
            setRegistryName(SushiGoCrafting.MOD_ID, "rice_crop_seed");
            setTranslationKey(getRegistryName().toString());
        }

        public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

            if (raytraceresult == null) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            else {
                if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos blockpos = raytraceresult.getBlockPos();
                    if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                    }
                    BlockPos blockpos1 = blockpos.up();
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);

                    if (iblockstate.getMaterial() == Material.WATER && ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0 && worldIn.isAirBlock(blockpos1)) {
                        net.minecraftforge.common.util.BlockSnapshot blocksnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(worldIn, blockpos1);
                        worldIn.setBlockState(blockpos1, SushiContent.Blocks.BLOCK_RICE_CROP.getDefaultState());
                        if (net.minecraftforge.event.ForgeEventFactory.onPlayerBlockPlace(playerIn, blocksnapshot, net.minecraft.util.EnumFacing.UP, handIn).isCanceled()) {
                            blocksnapshot.restore(true, false);
                            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                        }
                        worldIn.setBlockState(blockpos1, SushiContent.Blocks.BLOCK_RICE_CROP.getDefaultState(), 11);
                        if (!playerIn.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }
                        worldIn.playSound(playerIn, blockpos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                    }
                }

                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
        }

    }
}
