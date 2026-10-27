package com.buuz135.sushigocrafting.loot;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;

public class SeedsLootModifier extends LootModifier {

    public static final MapCodec<SeedsLootModifier> CODEC = RecordCodecBuilder.mapCodec(
            inst -> codecStart(inst)
                    .apply(inst, SeedsLootModifier::new));

    private final WeightedList<Item> seeds;

    public SeedsLootModifier(LootItemCondition[] conditionsIn, int priority) {
        super(conditionsIn, priority);
        seeds = WeightedList.<Item>builder()
                .add(Items.WHEAT_SEEDS, 100)
                .add(SushiContent.Items.RICE_SEEDS.get(), 25)
                .add(SushiContent.Items.SESAME_SEEDS.get(), 10)
                .add(SushiContent.Items.WASABI_SEEDS.get(), 25)
                .add(SushiContent.Items.SOY_SEEDS.get(), 25)
                .add(SushiContent.Items.CUCUMBER_SEEDS.get(), 25)
                .build();
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ObjectArrayList<ItemStack> customLoot = new ObjectArrayList<>();
        for (ItemStack stack : generatedLoot) {
            if (stack.getItem() == Items.WHEAT_SEEDS) {
                {
                    ItemStack weightedStack = new ItemStack(seeds.getRandomOrThrow(context.getRandom()));
                    weightedStack.setCount(stack.getCount());
                    customLoot.add(weightedStack);
                }
            } else {
                customLoot.add(stack);
            }
        }
        return customLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
