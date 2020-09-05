package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FoodItem extends Item {

    private final List<IFoodIngredient> ingredientList;
    private final IFoodType type;

    public FoodItem(Properties properties, IFoodType type) {
        super(properties);
        this.type = type;
        ingredientList = new ArrayList<>();
    }

    public List<IFoodIngredient> getIngredientList() {
        return ingredientList;
    }

    public IFoodType getType() {
        return type;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Ingredients: "));
        for (IFoodIngredient iFoodIngredient : ingredientList) {
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + " - " + new TranslationTextComponent(iFoodIngredient.getItem().getTranslationKey()).getString()));
        }
    }
}
