package com.buuz135.sushigocrafting.api;

import java.util.List;

public interface IFoodType {

    List<IFoodIngredient[]> getFoodIngredients();

    int[] getNameIndex();

    String getName();

}
