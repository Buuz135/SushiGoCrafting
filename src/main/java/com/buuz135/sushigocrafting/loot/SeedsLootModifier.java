package com.buuz135.sushigocrafting.loot;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SeedsLootModifier extends LootModifier {

    public static final Supplier<Codec<SeedsLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .apply(inst, SeedsLootModifier::new)));

    private final List<ItemWeightedItem> seeds = new ArrayList<>();

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
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ObjectArrayList<ItemStack> customLoot = new ObjectArrayList<>();
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


    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
