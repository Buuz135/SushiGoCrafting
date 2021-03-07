package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.api.FoodHelper;
import com.buuz135.sushigocrafting.api.FoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.component.FoodTypeButtonComponent;
import com.buuz135.sushigocrafting.gui.RollerWeightSelectorButtonComponent;
import com.buuz135.sushigocrafting.gui.provider.SushiAssetProvider;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RollerTile extends ActiveTile<RollerTile> {

    @Save
    private InventoryComponent<RollerTile> slots;
    @Save
    private String selected;
    @Save
    private WeightTracker weightTracker;
    @Save
    private int craftProgress;

    public RollerTile() {
        super(SushiContent.Blocks.ROLLER.get());
        int i = 1;
        int max = 0;
        this.craftProgress = 0;
        for (IFoodType foodType : FoodHelper.getAllFoodTypes()) {
            addButton(new FoodTypeButtonComponent(foodType, -20 * i, 0, 18, 18).setComponent(this::getSlots));
            ++i;
            if (selected == null) selected = foodType.getName();
            if (foodType.getFoodIngredients().size() > max) max = foodType.getFoodIngredients().size();
        }
        weightTracker = new WeightTracker(max);
        slots = new InventoryComponent<>("slots", 0, 0, max);
        slots.setSlotPosition(FoodHelper.getAllFoodTypes().get(0).getSlotPosition());
        slots.setInputFilter((stack, integer) -> FoodIngredient.fromItem(stack.getItem()) != null);
        FoodHelper.getTypeFromName(selected).ifPresent(iFoodType -> {
            for (int i1 = 0; i1 < slots.getSlots(); i1++) {
                slots.setSlotLimit(i1, i1 < iFoodType.getFoodIngredients().size() ? 1 : 0);
                int finalI = i1;
                addGuiAddonFactory(() -> new RollerWeightSelectorButtonComponent(slots, finalI) {
                    @Override
                    public int getWeight() {
                        return weightTracker.weights.get(finalI);
                    }
                });
            }
        });
        addInventory(slots);
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        ActionResultType type = super.onActivated(player, hand, facing, hitX, hitY, hitZ);
        if (!type.isSuccess()) {
            openGui(player);
            return ActionResultType.SUCCESS;
        }
        return type;
    }

    public void onClick(PlayerEntity player) {
        if (isServer()) {
            FoodHelper.getTypeFromName(selected).ifPresent(iFoodType -> {
                boolean allFull = true;
                for (int i1 = 0; i1 < slots.getSlots(); i1++) {
                    if (i1 < iFoodType.getFoodIngredients().size() && slots.getStackInSlot(i1).isEmpty()) {
                        allFull = false;
                        break;
                    }
                }
                if (allFull) {
                    ++craftProgress;
                    if (craftProgress >= 4) {
                        Random random = new Random(((ServerWorld) this.world).getSeed() + selected.hashCode());
                        craftProgress = 0;
                        List<IFoodIngredient> foodIngredients = new ArrayList<>();
                        List<Integer> weightValues = new ArrayList<>();
                        for (int i1 = 0; i1 < slots.getSlots(); i1++) {
                            if (i1 < iFoodType.getFoodIngredients().size()) {
                                foodIngredients.add(FoodIngredient.fromItem(slots.getStackInSlot(i1).getItem()));
                                weightValues.add(random.nextInt(5) - weightTracker.weights.get(i1));
                            }
                        }
                        FoodItem item = FoodHelper.getFoodFromIngredients(selected, foodIngredients);
                        if (item != null) {
                            ItemStack stack = new ItemStack(item);
                            stack.getOrCreateTag().putIntArray(FoodItem.WEIGHTS_TAG, weightValues);
                            InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.getPos().getY(), this.getPos().getZ(), stack);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void handleButtonMessage(int id, PlayerEntity playerEntity, CompoundNBT compound) {
        super.handleButtonMessage(id, playerEntity, compound);
        if (compound.contains("Type")) {
            //Random random = new Random(((ServerWorld) this.world).getSeed() + compound.getString("Type").hashCode());
            FoodHelper.getTypeFromName(compound.getString("Type")).ifPresent(iFoodType -> {
                slots.setSlotPosition(iFoodType.getSlotPosition());
                for (int i1 = 0; i1 < slots.getSlots(); i1++) {
                    slots.setSlotLimit(i1, i1 < iFoodType.getFoodIngredients().size() ? 1 : 0);
                }
                markForUpdate();
            });
        }
        if (id == 100) {
            int weight = compound.getInt("WeightSlot");
            int button = compound.getInt("Button");
            if (button == 0) {
                weightTracker.weights.set(weight, Math.min(4, weightTracker.weights.get(weight) + 1));
            }
            if (button == 1) {
                weightTracker.weights.set(weight, Math.max(0, weightTracker.weights.get(weight) - 1));
            }
            syncObject(weightTracker);
        }
    }

    @Override
    public RollerTile getSelf() {
        return this;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public InventoryComponent<RollerTile> getSlots() {
        return slots;
    }

    @Override
    public IAssetProvider getAssetProvider() {
        return SushiAssetProvider.INSTANCE;
    }

    private class WeightTracker implements INBTSerializable<CompoundNBT> {

        private List<Integer> weights;


        public WeightTracker(int amount) {
            weights = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                weights.add(0);
            }
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putIntArray("Weights", weights);
            return compoundNBT;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            weights = new ArrayList<>();
            for (int i : nbt.getIntArray("Weights")) {
                weights.add(i);
            }
        }
    }
}
