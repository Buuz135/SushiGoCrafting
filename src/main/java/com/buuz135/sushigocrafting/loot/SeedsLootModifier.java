package com.buuz135.sushigocrafting.loot;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SeedsLootModifier extends LootModifier {

    private List<ItemWeightedItem> seeds = new ArrayList<>();

    public SeedsLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
        seeds.add(new ItemWeightedItem(Items.WHEAT_SEEDS, 150));
        seeds.add(new ItemWeightedItem(SushiContent.Items.RICE_SEEDS.get(), 50));
        seeds.add(new ItemWeightedItem(SushiContent.Items.SESAME_SEEDS.get(), 50));
        seeds.add(new ItemWeightedItem(SushiContent.Items.WASABI_SEEDS.get(), 50));
        seeds.add(new ItemWeightedItem(SushiContent.Items.SOY_SEEDS.get(), 50));
        seeds.add(new ItemWeightedItem(SushiContent.Items.CUCUMBER_SEEDS.get(), 50));
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> customLoot = new ArrayList<>();
        for (ItemStack stack : generatedLoot) {
            if (stack.getItem() == Items.WHEAT_SEEDS) {
                ItemWeightedItem weightedItem = WeightedRandom.getRandomItem(context.getRandom(), seeds);
                ItemStack weightedStack = new ItemStack(weightedItem.getStack());
                weightedStack.setCount(stack.getCount());
                customLoot.add(weightedStack);
            } else {
                customLoot.add(stack);
            }
        }
        return customLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SeedsLootModifier> {

        @Override
        public SeedsLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new SeedsLootModifier(ailootcondition);
        }

        @Override
        public JsonObject write(SeedsLootModifier instance) {
            return makeConditions(instance.conditions);
        }

    }

}
