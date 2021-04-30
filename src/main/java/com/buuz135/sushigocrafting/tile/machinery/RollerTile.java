package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.buuz135.sushigocrafting.client.gui.RollerWeightSelectorButtonComponent;
import com.buuz135.sushigocrafting.client.gui.provider.RollerAssetProvider;
import com.buuz135.sushigocrafting.component.FoodTypeButtonComponent;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

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
        int i = 0;
        int max = 0;
        this.craftProgress = 0;
        for (IFoodType foodType : FoodAPI.get().getFoodTypes()) {
            addButton(new FoodTypeButtonComponent(foodType, -20 - 20 * (i % 3), (i / 3) * 20, 18, 18) {
                @Override
                public Supplier<String> getSelected() {
                    return () -> selected;
                }
            }.setComponent(this::getSlots));
            ++i;
            if (selected == null) selected = foodType.getName();
            if (foodType.getFoodIngredients().size() > max) max = foodType.getFoodIngredients().size();
        }
        weightTracker = new WeightTracker(max);
        slots = new InventoryComponent<>("slots", 0, 0, max);
        slots.setSlotPosition(FoodAPI.get().getFoodTypes().get(0).getSlotPosition());
        slots.setInputFilter((stack, integer) -> !FoodAPI.get().getIngredientFromItem(stack.getItem()).isEmpty());
        FoodAPI.get().getTypeFromName(selected).ifPresent(iFoodType -> {
            for (int i1 = 0; i1 < slots.getSlots(); i1++) {
                slots.setSlotLimit(i1, i1 < iFoodType.getFoodIngredients().size() ? 64 : 0);
                int finalI = i1;
                addGuiAddonFactory(() -> new RollerWeightSelectorButtonComponent(slots, finalI) {
                    @Override
                    public int getWeight() {
                        return weightTracker.weights.get(finalI);
                    }

                    @Override
                    public String getType() {
                        return selected;
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
            if (player instanceof ServerPlayerEntity) {
                player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.requestUpdate((ServerPlayerEntity) player, false));
            }
            openGui(player);
            return ActionResultType.SUCCESS;
        }
        return type;
    }

    public void onClick(PlayerEntity player) {
        if (isServer()) {
            FoodAPI.get().getTypeFromName(selected).ifPresent(iFoodType -> {
                boolean allFull = true;
                for (int i1 = 0; i1 < slots.getSlots(); i1++) {
                    if (i1 < iFoodType.getFoodIngredients().size()) {
                        IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(slots.getStackInSlot(i1).getItem());
                        if (!ingredient.isEmpty() && !ingredient.getIngredientConsumer().canConsume(ingredient, slots.getStackInSlot(i1), weightTracker.weights.get(i1))) {
                            allFull = false;
                            break;
                        }
                    }
                }
                if (allFull) {
                    ++craftProgress;
                    if (craftProgress >= 4) {
                        Random random = new Random(((ServerWorld) this.world).getSeed() + selected.hashCode());
                        craftProgress = 0;
                        List<IFoodIngredient> foodIngredients = new ArrayList<>();
                        List<Integer> weightValues = new ArrayList<>();
                        AtomicBoolean discovery = new AtomicBoolean(false);
                        for (int slot = 0; slot < slots.getSlots(); slot++) {
                            if (slot < iFoodType.getFoodIngredients().size()) {
                                IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(slots.getStackInSlot(slot).getItem());
                                foodIngredients.add(ingredient);
                            }
                        }
                        FoodItem item = FoodHelper.getFoodFromIngredients(selected, foodIngredients);
                        if (item != null) {
                            for (int slot = 0; slot < slots.getSlots(); slot++) {
                                if (slot < iFoodType.getFoodIngredients().size()) {
                                    IFoodIngredient ingredient = foodIngredients.get(slot);
                                    ingredient.getIngredientConsumer().consume(ingredient, slots.getStackInSlot(slot), weightTracker.weights.get(slot));
                                    int value = random.nextInt(5) - weightTracker.weights.get(slot);
                                    weightValues.add(value);
                                    if (value == 0 && !ingredient.isEmpty()) {
                                        int finalSlot = slot;
                                        player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> {
                                            if (!iSushiWeightDiscovery.hasDiscovery(selected + "-" + finalSlot)) {
                                                iSushiWeightDiscovery.setDiscovery(selected + "-" + finalSlot, weightTracker.weights.get(finalSlot));
                                                discovery.set(true);
                                            }
                                        });
                                    }
                                }
                            }
                            ItemStack stack = new ItemStack(item);
                            stack.getOrCreateTag().putIntArray(FoodItem.WEIGHTS_TAG, weightValues);
                            InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.getPos().getY(), this.getPos().getZ(), stack);
                        }
                        if (player instanceof ServerPlayerEntity) {
                            player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.requestUpdate((ServerPlayerEntity) player, discovery.get()));
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
            FoodAPI.get().getTypeFromName(compound.getString("Type")).ifPresent(iFoodType -> {
                slots.setSlotPosition(iFoodType.getSlotPosition());
                for (int i1 = 0; i1 < slots.getSlots(); i1++) {
                    if (i1 < iFoodType.getFoodIngredients().size()) {
                        slots.setSlotLimit(i1, 64);
                    } else {
                        ItemStack slotStack = slots.getStackInSlot(i1).copy();
                        slots.setStackInSlot(i1, ItemStack.EMPTY);
                        slots.setSlotLimit(i1, 0);
                        ItemHandlerHelper.giveItemToPlayer(playerEntity, slotStack);
                    }
                }
                this.selected = compound.getString("Type");
                syncObject(slots);
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
        return RollerAssetProvider.INSTANCE;
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
