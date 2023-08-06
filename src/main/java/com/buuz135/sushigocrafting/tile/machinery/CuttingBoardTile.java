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
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class CuttingBoardTile extends ActiveTile<CuttingBoardTile> {

    public static TagKey<Item> KNIFE = TagUtil.getItemTag(new ResourceLocation("forge", "tools/knife"));

    @Save
    private final InventoryComponent<CuttingBoardTile> input;
    @Save
    private int click;

    public CuttingBoardTile(BlockPos pos, BlockState state) {
        super(SushiContent.Blocks.CUTTING_BOARD.get(), SushiContent.TileEntities.CUTTING_BOARD.get(), pos, state);
        this.addInventory(this.input = new InventoryComponent<CuttingBoardTile>("input", 0, 0, 1)
                .setInputFilter((stack, integer) -> accepts(stack))
        );
        this.click = 0;
    }

    @Override
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty()) {
            if (!this.input.getStackInSlot(0).isEmpty() && TagUtil.hasTag(ForgeRegistries.ITEMS, stack.getItem(), KNIFE)) {
                ++click;
                if (click > 5) {
                    processRecipe(player);
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


    public boolean processRecipe(LivingEntity entity) {
        for (CuttingBoardRecipe recipe : RecipeUtil.getRecipes(this.level, ((RecipeType<CuttingBoardRecipe>) SushiContent.RecipeTypes.CUTTING_BOARD.get()))) {
            if (recipe.getInput().test(this.input.getStackInSlot(0))) {
                Item item = FoodAPI.get().getIngredientFromName(recipe.getIngredient()).getItem();
                ItemStack stack = null;
                if (item instanceof AmountItem) {
                    stack = ((AmountItem) item).random(entity, level);
                } else {
                    stack = new ItemStack(item);
                }
                if (entity instanceof Player player) {
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                } else {
                    entity.setItemInHand(InteractionHand.MAIN_HAND, stack);
                }
                this.input.getStackInSlot(0).shrink(1);
                return true;
            }
        }
        return false;
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

    public boolean accepts(ItemStack input) {
        return RecipeUtil.getRecipes(this.level, ((RecipeType<CuttingBoardRecipe>)SushiContent.RecipeTypes.CUTTING_BOARD.get())).stream().anyMatch(cuttingBoardRecipe -> cuttingBoardRecipe.getInput().test(input));
    }
}
