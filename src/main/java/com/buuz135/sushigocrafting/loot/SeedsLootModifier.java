package com.buuz135.sushigocrafting.loot;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SeedsLootModifier extends LootModifier {

    private List<ItemWeightedItem> seeds = new ArrayList<>();

    public SeedsLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
        seeds.add(new ItemWeightedItem(Items.WHEAT_SEEDS, 150));
        seeds.add(new ItemWeightedItem(SushiContent.Items.RICE_SEEDS.get(), 25));
        seeds.add(new ItemWeightedItem(SushiContent.Items.SESAME_SEEDS.get(), 10));
        seeds.add(new ItemWeightedItem(SushiContent.Items.WASABI_SEEDS.get(), 25));
        seeds.add(new ItemWeightedItem(SushiContent.Items.SOY_SEEDS.get(), 25));
        seeds.add(new ItemWeightedItem(SushiContent.Items.CUCUMBER_SEEDS.get(), 25));
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> customLoot = new ArrayList<>();
        for (ItemStack stack : generatedLoot) {
            if (stack.getItem() == Items.WHEAT_SEEDS) {
                WeightedRandom.getRandomItem(context.getRandom(), seeds).ifPresent(itemWeightedItem -> {
                    ItemStack weightedStack = new ItemStack(itemWeightedItem.getStack());
                    weightedStack.setCount(stack.getCount());
                    customLoot.add(weightedStack);
                });
            } else {
                customLoot.add(stack);
            }
        }
        return customLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SeedsLootModifier> {

        @Override
        public SeedsLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new SeedsLootModifier(ailootcondition);
        }

        @Override
        public JsonObject write(SeedsLootModifier instance) {
            return makeConditions(instance.conditions);
        }

    }

}
