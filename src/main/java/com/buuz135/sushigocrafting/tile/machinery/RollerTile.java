package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.buuz135.sushigocrafting.client.gui.RollerWeightSelectorButtonComponent;
import com.buuz135.sushigocrafting.client.gui.provider.RollerAssetProvider;
import com.buuz135.sushigocrafting.component.FoodTypeButtonComponent;
import com.buuz135.sushigocrafting.component.RollerCraftButtonComponent;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class RollerTile extends ActiveTile<RollerTile> {

    @Save
    private final InventoryComponent<RollerTile> slots;
    @Save
    private String selected;
    @Save
    private final WeightTracker weightTracker;
    @Save
    private int craftProgress;
    @Save
    private final InventoryComponent<RollerTile> spices;

    public RollerTile(BlockPos pos, BlockState state) {
        super(SushiContent.Blocks.ROLLER.get(), SushiContent.TileEntities.ROLLER.get(), pos, state);
        int i = 0;
        int max = 0;
        this.craftProgress = 0;
        for (IFoodType foodType : FoodAPI.get().getFoodTypes()) {
            if (selected == null) selected = foodType.getName();
            max = Math.max(max, foodType.getFoodIngredients().size());
            ++i;
        }
        weightTracker = new WeightTracker(max);
        slots = new InventoryComponent<RollerTile>("slots", 0, 0, max)
                .setSlotPosition(FoodAPI.get().getTypeFromName(selected).get().getSlotPosition())
                .setInputFilter((stack, integer) -> {
                    List<IFoodIngredient[]> ingredients = FoodAPI.get().getTypeFromName(selected).get().getFoodIngredients();
                    if (integer >= ingredients.size()) return false;
                    for (IFoodIngredient ingredient : ingredients.get(integer)) {
                        if (ingredient.isEmpty()) continue;
                        if (ingredient.getItem().equals(stack.getItem())) return true;
                    }
                    return false;
                });
        addInventory(slots);
        addInventory(this.spices = new InventoryComponent<RollerTile>("spices", 130, 76, 2)
                .setSlotLimit(1)
                .setSlotToColorRender(0, DyeColor.YELLOW)
                .setSlotToColorRender(1, DyeColor.YELLOW)
                .setSlotToItemStackRender(0, new ItemStack(SushiContent.Items.SOY_SAUCE.get()))
                .setSlotToItemStackRender(1, new ItemStack(SushiContent.Items.WASABI_PASTE.get()))
                .setInputFilter((stack, integer) -> {
                    if (integer == 0) return stack.getItem().equals(SushiContent.Items.SOY_SAUCE.get());
                    if (integer == 1) return stack.getItem().equals(SushiContent.Items.WASABI_PASTE.get());
                    return false;
                })
        );
        addInventory(spices);
        FoodAPI.get().getTypeFromName(this.selected).ifPresent(iFoodType -> {
            for (int slot = 0; slot < slots.getSlots(); slot++) {
                slots.setSlotToItemStackRender(slot, iFoodType.getSlotStackRender().apply(slot));
                slots.setSlotToColorRender(slot, 0xffc769);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initClient() {
        super.initClient();
        int i = 0;
        for (IFoodType foodType : FoodAPI.get().getFoodTypes()) {
            addButton(new FoodTypeButtonComponent(foodType, -20, i * 20 + 10, 18, 18) {
                @Override
                public Supplier<String> getSelected() {
                    return () -> selected;
                }
            }.setComponent(this::getSlots));
            ++i;
        }
        FoodAPI.get().getTypeFromName(selected).ifPresent(iFoodType -> {
            for (int i1 = 0; i1 < slots.getSlots(); i1++) {
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
        addButton(new RollerCraftButtonComponent(148, 20, 18, 18).setId(101));
    }

    @Override
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        InteractionResult type = super.onActivated(player, hand, facing, hitX, hitY, hitZ);
        if (!type.shouldSwing()) {
            if (player instanceof ServerPlayer) {
                player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.requestUpdate((ServerPlayer) player, ItemStack.EMPTY));
            }
            openGui(player);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public void onClick(Player player) {
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
                        Random random = new Random(((ServerLevel) this.level).getSeed() + selected.hashCode());
                        craftProgress = 0;
                        List<IFoodIngredient> foodIngredients = new ArrayList<>();
                        List<Integer> weightValues = new ArrayList<>();
                        AtomicReference<ItemStack> discovery = new AtomicReference<>(ItemStack.EMPTY);
                        for (int slot = 0; slot < slots.getSlots(); slot++) {
                            if (slot < iFoodType.getFoodIngredients().size()) {
                                IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(slots.getStackInSlot(slot).getItem());
                                foodIngredients.add(ingredient);
                            }
                        }
                        FoodItem item = FoodHelper.getFoodFromIngredients(selected, foodIngredients);
                        if (item != null) {
                            ItemStack stack = new ItemStack(item);
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
                                                discovery.set(stack.copy());
                                            }
                                        });
                                    }
                                }
                            }
                            stack.getOrCreateTag().putIntArray(FoodItem.WEIGHTS_TAG, weightValues);
                            CompoundTag spicesNBT = new CompoundTag();
                            for (int i = 0; i < spices.getSlots(); i++) {
                                if (!spices.getStackInSlot(i).isEmpty()) {
                                    IFoodIngredient soy = FoodAPI.get().getIngredientFromItem(spices.getStackInSlot(i).getItem());
                                    if (soy.getIngredientConsumer().canConsume(soy, spices.getStackInSlot(i), 0)) {
                                        soy.getIngredientConsumer().consume(soy, spices.getStackInSlot(i), 0);
                                        spicesNBT.putBoolean(soy.getName(), true);
                                    }
                                }
                            }
                            stack.getOrCreateTag().put(FoodItem.SPICES_TAG, spicesNBT);
                            Containers.dropItemStack(this.level, this.worldPosition.getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), stack);
                        }
                        if (player instanceof ServerPlayer) {
                            player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.requestUpdate((ServerPlayer) player, discovery.get()));
                        }
                        markForUpdate();
                    }
                }
            });
        }
    }

    @Override
    public void handleButtonMessage(int id, Player playerEntity, CompoundTag compound) {
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
                for (int i = 0; i < slots.getSlots(); i++) {
                    slots.setSlotToItemStackRender(i, iFoodType.getSlotStackRender().apply(i));
                }
                syncObject(slots);
                for (int i = 0; i < slots.getSlots(); i++) {
                    if (!slots.isItemValid(i, slots.getStackInSlot(i))) {
                        ItemStack slotStack = slots.getStackInSlot(i).copy();
                        slots.setStackInSlot(i, ItemStack.EMPTY);
                        ItemHandlerHelper.giveItemToPlayer(playerEntity, slotStack);
                    }
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
        if (id == 101) {
            int amount = 1;
            if (compound.contains("Button")) {
                amount = compound.getInt("Button") == 1 ? 64 : 1;
            }
            for (int i = 0; i < amount; i++) {
                this.craftProgress = 4;
                onClick(playerEntity);
            }
        }
    }

    @Override
    public RollerTile getSelf() {
        return this;
    }

    public InventoryComponent<RollerTile> getSlots() {
        return slots;
    }

    @Override
    public IAssetProvider getAssetProvider() {
        return RollerAssetProvider.INSTANCE;
    }

    public WeightTracker getWeightTracker() {
        return weightTracker;
    }

    public InventoryComponent<RollerTile> getSpices() {
        return spices;
    }

    public class WeightTracker implements INBTSerializable<CompoundTag> {

        private List<Integer> weights;

        public WeightTracker(int amount) {
            weights = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                weights.add(0);
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.putIntArray("Weights", weights);
            return compoundNBT;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            weights = new ArrayList<>();
            for (int i : nbt.getIntArray("Weights")) {
                weights.add(i);
            }
        }

        public List<Integer> getWeights() {
            return weights;
        }
    }
}
