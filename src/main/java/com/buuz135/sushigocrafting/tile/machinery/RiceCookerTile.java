package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.Nonnull;

public class RiceCookerTile extends ActiveTile<RiceCookerTile> {

    public static TagKey<Item> RICE = TagUtil.getItemTag(Identifier.fromNamespaceAndPath("c", "crops/rice"));

    @Save
    private final InventoryComponent<RiceCookerTile> input;
    @Save
    private final InventoryComponent<RiceCookerTile> fuel;
    @Save
    private ProgressBarComponent<RiceCookerTile> bar;
    @Save
    private InventoryComponent<RiceCookerTile> output;
    @Save
    private FluidTankComponent<RiceCookerTile> water;
    @Save
    private double burnTime;


    public RiceCookerTile(BlockPos pos, BlockState state) {
        super(SushiContent.Blocks.RICE_COOKER.get(), SushiContent.TileEntities.RICE_COOKER.get(), pos, state);
        this.burnTime = 0;
        addInventory(this.input = new InventoryComponent<RiceCookerTile>("input", 20, 38, 4)
                .setRange(2, 2)
                .setSlotToColorRender(0, DyeColor.BLUE)
                .setSlotToColorRender(1, DyeColor.BLUE)
                .setSlotToColorRender(2, DyeColor.BLUE)
                .setSlotToColorRender(3, DyeColor.BLUE)
                .setSlotLimit(1)
                .setInputFilter((stack, integer) -> stack.is(RICE))
                .setOutputFilter((stack, integer) -> false));
        addProgressBar(this.bar = new ProgressBarComponent<RiceCookerTile>(99, 48, 100)
                .setCanIncrease(RiceCookerTile::canStart)
                .setCanReset(RiceCookerTile::canStart)
                .setOnTickWork(() -> syncObject(this.bar))
                .setBarDirection(ProgressBarComponent.BarDirection.ARROW_RIGHT)
                .setOnFinishWork(() -> {
                    ItemStack result = SushiContent.Items.COOKED_RICE.get().withAmount(getSlotsFilled() * 50);
                    if (!this.output.getStackInSlot(0).isEmpty()) {
                        ItemStack original = this.output.getStackInSlot(0);
                        result = SushiContent.Items.COOKED_RICE.get().withAmount(Math.min(((AmountItem) original.getItem()).getMaxCombineAmount(), ((AmountItem) original.getItem()).getCurrentAmount(original) + ((AmountItem) result.getItem()).getCurrentAmount(result)));
                    }
                    this.output.setStackInSlot(0, result);
                    for (int i = 0; i < this.input.getSlots(); i++) {
                        this.input.setStackInSlot(i, ItemStack.EMPTY);
                    }
                    try (Transaction transaction = Transaction.openRoot()) {
                        if (this.water.extractForced(FluidResource.of(Fluids.WATER), 1000, transaction) != 1000) {
                            throw new IllegalStateException("Rice cooker has insufficient water");
                        }
                        transaction.commit();
                    }
                    --this.burnTime;
                    syncObject(this.bar);
                    setChanged();
                }));
        addInventory(this.output = new InventoryComponent<RiceCookerTile>("output", 134, 48, 1)
                .setSlotToColorRender(0, DyeColor.ORANGE)
                .setSlotLimit(1)
                .setInputFilter((stack, integer) -> false)
                .setOutputFilter((stack, integer) -> true));
        addTank(this.water = new FluidTankComponent<RiceCookerTile>("water", 8000, 68, 27) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.is(Fluids.WATER);
            }
        }.setTankAction(FluidTankComponent.Action.FILL).setTankType(FluidTankComponent.Type.SMALL));
        addInventory(this.fuel = new InventoryComponent<RiceCookerTile>("fuel", 69, 67, 1)
                .setSlotToColorRender(0, DyeColor.RED)
                .setSlotLimit(64)
                .setInputFilter((stack, integer) -> stack.getBurnTime(RecipeType.SMELTING, level.fuelValues()) > 0));
    }

    @Nonnull
    @Override
    public RiceCookerTile getSelf() {
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
        if (burnTime < 1 && !this.fuel.getStackInSlot(0).isEmpty()) {
            this.burnTime += this.fuel.getStackInSlot(0).getBurnTime(RecipeType.SMELTING, level.fuelValues()) / 200D;
            var stack = this.fuel.getStackInSlot(0);
            var remainder = stack.getCraftingRemainder();
            if (remainder != null) {
                this.fuel.setStackInSlot(0, remainder.create());
            } else {
                this.fuel.getStackInSlot(0).shrink(1);
            }
            setChanged();
        }
        return burnTime > 0 && getSlotsFilled() > 0 && (this.output.getStackInSlot(0).isEmpty() || ((AmountItem) this.output.getStackInSlot(0).getItem()).getCurrentAmount(this.output.getStackInSlot(0)) < ((AmountItem) this.output.getStackInSlot(0).getItem()).getMaxCombineAmount()) && this.water.getFluidAmount() >= 1000;
    }

    private int getSlotsFilled() {
        int amount = 0;
        for (int i = 0; i < this.input.getSlots(); i++) {
            if (!this.input.getStackInSlot(i).isEmpty()) ++amount;
        }
        return amount;
    }

    public ProgressBarComponent<RiceCookerTile> getBar() {
        return bar;
    }


}
