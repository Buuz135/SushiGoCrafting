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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class FermentationBarrelTile extends ActiveTile<FermentationBarrelTile> {

    @Save
    private ProgressBarComponent<FermentationBarrelTile> bar;
    @Save
    private final FluidTankComponent<FermentationBarrelTile> fluid;
    @Save
    private final InventoryComponent<FermentationBarrelTile> input;
    @Save
    private final InventoryComponent<FermentationBarrelTile> output;

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
        if (!type.shouldSwing()) {
            openGui(player);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public boolean canStart() {
        return RecipeUtil.getRecipes(this.level, FermentingBarrelRecipe.SERIALIZER.getRecipeType()).stream()
                .anyMatch(fermentingBarrelRecipe -> fermentingBarrelRecipe.input.test(this.input.getStackInSlot(0))
                        && (fermentingBarrelRecipe.fluid.isEmpty() || (fermentingBarrelRecipe.fluid.isFluidEqual(this.fluid.getFluid()) && this.fluid.getFluid().getAmount() >= fermentingBarrelRecipe.fluid.getAmount()))
                        && (this.output.getStackInSlot(0).isEmpty())
                );
    }

    public void onFinish() {
        RecipeUtil.getRecipes(this.level, FermentingBarrelRecipe.SERIALIZER.getRecipeType()).stream()
                .filter(fermentingBarrelRecipe -> fermentingBarrelRecipe.input.test(this.input.getStackInSlot(0))
                        && (fermentingBarrelRecipe.fluid.isEmpty() || (fermentingBarrelRecipe.fluid.isFluidEqual(this.fluid.getFluid()) && this.fluid.getFluid().getAmount() >= fermentingBarrelRecipe.fluid.getAmount()))
                        && (this.output.getStackInSlot(0).isEmpty())
                )
                .findFirst()
                .ifPresent(fermentingBarrelRecipe -> {
                    this.input.getStackInSlot(0).shrink(1);
                    this.fluid.drainForced(fermentingBarrelRecipe.getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
                    if (fermentingBarrelRecipe.output.getItem() instanceof AmountItem) {
                        ItemHandlerHelper.insertItem(this.output, ((AmountItem) fermentingBarrelRecipe.output.getItem()).random(null, level), false);
                    } else {
                        ItemHandlerHelper.insertItem(this.output, fermentingBarrelRecipe.output.copy(), false);
                    }

                });
    }

    public ProgressBarComponent<FermentationBarrelTile> getBar() {
        return bar;
    }


}
