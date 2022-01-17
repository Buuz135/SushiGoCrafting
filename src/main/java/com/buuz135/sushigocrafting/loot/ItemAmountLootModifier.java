package com.buuz135.sushigocrafting.loot;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemAmountLootModifier extends LootModifier {

    public ItemAmountLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> customLoot = new ArrayList<>();
        for (ItemStack stack : generatedLoot) {
            if (stack.getItem() instanceof AmountItem) {
                customLoot.add(((AmountItem) stack.getItem()).random(null, context.getLevel()));
            } else {
                customLoot.add(stack);
            }
        }
        return customLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<ItemAmountLootModifier> {

        @Override
        public ItemAmountLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new ItemAmountLootModifier(ailootcondition);
        }

        @Override
        public JsonObject write(ItemAmountLootModifier instance) {
            return makeConditions(instance.conditions);
        }

    }

}
