package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class CuttingBoardTile extends ActiveTile<CuttingBoardTile> {

    @Save
    private InventoryComponent<CuttingBoardTile> input;
    @Save
    private int click;

    public CuttingBoardTile() {
        super(SushiContent.Blocks.CUTTING_BOARD.get());
        this.addInventory(this.input = new InventoryComponent<CuttingBoardTile>("input", 0, 0, 1)
                .setInputFilter((stack, integer) -> accepts(stack))
        );
        this.click = 0;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty()) {
            if (!this.input.getStackInSlot(0).isEmpty() && stack.getItem().equals(SushiContent.Items.KNIFE_CLEAVER.get())) {
                ++click;
                if (click > 5) {
                    for (CuttingBoardRecipe recipe : RecipeUtil.getRecipes(this.world, CuttingBoardRecipe.SERIALIZER.getRecipeType())) {
                        if (recipe.getInput().test(this.input.getStackInSlot(0))) {
                            Item item = FoodAPI.get().getIngredientFromName(recipe.getIngredient()).getItem();
                            if (item instanceof AmountItem) {
                                ItemHandlerHelper.giveItemToPlayer(player, ((AmountItem) item).random(player, world));
                            } else {
                                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(item));
                            }
                            this.input.getStackInSlot(0).shrink(1);
                        }
                    }
                    click = 0;
                }
                syncObject(click);
                return ActionResultType.SUCCESS;
            } else if (this.input.getStackInSlot(0).isEmpty() && accepts(stack)) {
                this.input.insertItem(0, stack.copy(), false);
                stack.setCount(0);
                return ActionResultType.SUCCESS;
            }
        } else if (player.isSneaking()) {
            ItemStack inserted = this.input.getStackInSlot(0).copy();
            this.input.setStackInSlot(0, ItemStack.EMPTY);
            ItemHandlerHelper.giveItemToPlayer(player, inserted);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
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
        return RecipeUtil.getRecipes(this.world, CuttingBoardRecipe.SERIALIZER.getRecipeType()).stream().anyMatch(cuttingBoardRecipe -> cuttingBoardRecipe.getInput().test(input));
    }
}
