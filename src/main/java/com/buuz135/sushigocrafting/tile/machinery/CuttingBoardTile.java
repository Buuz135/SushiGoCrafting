package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.util.RecipeUtil;
import com.hrznstudio.titanium.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class CuttingBoardTile extends ActiveTile<CuttingBoardTile> {

    public static Tag<Item> KNIFE = TagUtil.getItemTag(new ResourceLocation("forge", "tools/knife"));

    @Save
    private InventoryComponent<CuttingBoardTile> input;
    @Save
    private int click;

    public CuttingBoardTile(BlockPos pos, BlockState state) {
        super(SushiContent.Blocks.CUTTING_BOARD.get(), pos, state);
        this.addInventory(this.input = new InventoryComponent<CuttingBoardTile>("input", 0, 0, 1)
                .setInputFilter((stack, integer) -> accepts(stack))
        );
        this.click = 0;
    }

    @Override
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty()) {
            if (!this.input.getStackInSlot(0).isEmpty() && stack.is(KNIFE)) {
                ++click;
                if (click > 5) {
                    for (CuttingBoardRecipe recipe : RecipeUtil.getRecipes(this.level, CuttingBoardRecipe.SERIALIZER.getRecipeType())) {
                        if (recipe.getInput().test(this.input.getStackInSlot(0))) {
                            Item item = FoodAPI.get().getIngredientFromName(recipe.getIngredient()).getItem();
                            if (item instanceof AmountItem) {
                                ItemHandlerHelper.giveItemToPlayer(player, ((AmountItem) item).random(player, level));
                            } else {
                                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(item));
                            }
                            this.input.getStackInSlot(0).shrink(1);
                        }
                    }
                    click = 0;
                }
                syncObject(click);
                return InteractionResult.SUCCESS;
            } else if (this.input.getStackInSlot(0).isEmpty() && accepts(stack)) {
                this.input.insertItem(0, stack.copy(), false);
                stack.setCount(0);
                return InteractionResult.SUCCESS;
            }
        } else if (player.isShiftKeyDown()) {
            ItemStack inserted = this.input.getStackInSlot(0).copy();
            this.input.setStackInSlot(0, ItemStack.EMPTY);
            ItemHandlerHelper.giveItemToPlayer(player, inserted);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Nonnull
    @Override
    public CuttingBoardTile getSelf() {
        return this;
    }

    public InventoryComponent<CuttingBoardTile> getInput() {
        return input;
    }

    public int getClick() {
        return click;
    }

    private boolean accepts(ItemStack input) {
        return RecipeUtil.getRecipes(this.level, CuttingBoardRecipe.SERIALIZER.getRecipeType()).stream().anyMatch(cuttingBoardRecipe -> cuttingBoardRecipe.getInput().test(input));
    }
}
