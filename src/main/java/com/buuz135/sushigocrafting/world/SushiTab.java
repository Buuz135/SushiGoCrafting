package com.buuz135.sushigocrafting.world;

import com.buuz135.sushigocrafting.api.FoodHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SushiTab extends ItemGroup {

    private List<ItemStack> values = new ArrayList<>();

    public SushiTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        if (values.isEmpty())
            values = FoodHelper.REGISTERED.values().stream().flatMap(Collection::stream).map(ItemStack::new).collect(Collectors.toList());
        return values.get(0);
    }

    @Override
    public ItemStack getIcon() {
        createIcon();
        return values.get((int) (Minecraft.getInstance().world.getGameTime() / 30 % values.size()));
    }
}
