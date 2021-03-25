package com.buuz135.sushigocrafting.loot;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemAmountLootModifier extends LootModifier {

    public ItemAmountLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> customLoot = new ArrayList<>();
        for (ItemStack stack : generatedLoot) {
            if (stack.getItem() instanceof AmountItem) {
                customLoot.add(((AmountItem) stack.getItem()).random(null, context.getWorld()));
            } else {
                customLoot.add(stack);
            }
        }
        return customLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<ItemAmountLootModifier> {

        @Override
        public ItemAmountLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new ItemAmountLootModifier(ailootcondition);
        }

        @Override
        public JsonObject write(ItemAmountLootModifier instance) {
            return makeConditions(instance.conditions);
        }

    }

}
