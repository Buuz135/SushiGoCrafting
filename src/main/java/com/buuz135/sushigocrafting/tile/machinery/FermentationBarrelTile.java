package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class FermentationBarrelTile extends ActiveTile<FermentationBarrelTile> {

    @Save
    private final FluidTankComponent<FermentationBarrelTile> fluid;
    @Save
    private final InventoryComponent<FermentationBarrelTile> input;
    @Save
    private final InventoryComponent<FermentationBarrelTile> output;
    @Save
    private ProgressBarComponent<FermentationBarrelTile> bar;

    public FermentationBarrelTile(BlockPos pos, BlockState state) {
        super(SushiContent.Blocks.FERMENTATION_BARREL.get(), SushiContent.TileEntities.FERMENTATION_BARREL.get(), pos, state);
        addProgressBar(this.bar = new ProgressBarComponent<FermentationBarrelTile>(93, 48, 100)
                .setCanIncrease(FermentationBarrelTile::canStart)
                .setCanReset(FermentationBarrelTile::canStart)
                .setOnTickWork(() -> syncObject(this.bar))
                .setBarDirection(ProgressBarComponent.BarDirection.ARROW_RIGHT)
                .setOnFinishWork(() -> {
                    onFinish();
                    syncObject(this.bar);
                    setChanged();
                }));
        addInventory(this.input = new InventoryComponent<FermentationBarrelTile>("input", 30, 48, 1)
                .setSlotToColorRender(0, DyeColor.BLUE)
                .setOutputFilter((i, s) -> false)
        );
        addInventory(this.output = new InventoryComponent<FermentationBarrelTile>("output", 130, 48, 1)
                .setSlotToColorRender(0, DyeColor.ORANGE)
                .setInputFilter((i, s) -> false)
        );
        addTank(this.fluid = new FluidTankComponent<FermentationBarrelTile>("fluid", 2000, 59, 46)
                .setTankType(FluidTankComponent.Type.SMALL)
                .setTankAction(FluidTankComponent.Action.FILL)
        );
    }

    @Nonnull
    @Override
    public FermentationBarrelTile getSelf() {
        return this;
    }

    @Override
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        InteractionResult type = super.onActivated(player, hand, facing, hitX, hitY, hitZ);
        if (!type.consumesAction()) {
            openGui(player);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public boolean canStart() {
        return RecipeUtil.getRecipes(this.level, (RecipeType<FermentingBarrelRecipe>) SushiContent.RecipeTypes.FERMENTING_BARREL.get()).stream()
                .anyMatch(fermentingBarrelRecipe -> Ingredient.testOptionalIngredient(fermentingBarrelRecipe.getInput(), this.input.getStackInSlot(0))
                        && (fermentingBarrelRecipe.getFluid().isEmpty() || (fermentingBarrelRecipe.getFluid().is(this.fluid.getFluid().getFluid()) && this.fluid.getFluid().getAmount() >= fermentingBarrelRecipe.getFluid().getAmount()))
                        && (canStack(fermentingBarrelRecipe))
                );
    }

    public boolean canStack(FermentingBarrelRecipe recipe) {
        var outputStack = this.output.getStackInSlot(0);
        if (outputStack.isEmpty()) return true;
        if (!ItemStack.isSameItem(outputStack, recipe.getOutput()) || !(outputStack.getItem() instanceof AmountItem outputAmount))
            return false;
        return outputAmount.getCurrentAmount(outputStack.copy()) < outputAmount.getMaxCombineAmount();
    }

    public void onFinish() {
        RecipeUtil.getRecipes(this.level, (RecipeType<FermentingBarrelRecipe>) SushiContent.RecipeTypes.FERMENTING_BARREL.get()).stream()
                .filter(fermentingBarrelRecipe -> Ingredient.testOptionalIngredient(fermentingBarrelRecipe.getInput(), this.input.getStackInSlot(0))
                        && (fermentingBarrelRecipe.getFluid().isEmpty() || (fermentingBarrelRecipe.getFluid().is(this.fluid.getFluid().getFluid()) && this.fluid.getFluid().getAmount() >= fermentingBarrelRecipe.getFluid().getAmount()))
                        && (canStack(fermentingBarrelRecipe))
                )
                .findFirst()
                .ifPresent(fermentingBarrelRecipe -> {
                    this.input.getStackInSlot(0).shrink(1);
                    try (Transaction transaction = Transaction.openRoot()) {
                        int amount = fermentingBarrelRecipe.getFluid().getAmount();
                        if (this.fluid.extractForced(FluidResource.of(fermentingBarrelRecipe.getFluid()), amount, transaction) != amount) {
                            throw new IllegalStateException("Fermentation barrel has insufficient fluid");
                        }
                        transaction.commit();
                    }
                    if (fermentingBarrelRecipe.getOutput().getItem() instanceof AmountItem) {
                        ItemStack outputStack = this.output.getStackInSlot(0);
                        ItemStack recipeOutput = ((AmountItem) fermentingBarrelRecipe.getOutput().getItem()).random(null, level);
                        if (outputStack.isEmpty()) {
                            this.output.setStackInSlot(0, recipeOutput);
                        } else {
                            AmountItem.combineStacks(outputStack, recipeOutput);
                        }
                    } else {
                        this.output.setStackInSlot(0, fermentingBarrelRecipe.getOutput().copy());
                    }

                });
    }

    public ProgressBarComponent<FermentationBarrelTile> getBar() {
        return bar;
    }


}
