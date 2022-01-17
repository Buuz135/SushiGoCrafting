package com.buuz135.sushigocrafting.world;

import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SushiTab extends CreativeModeTab {

    private List<ItemStack> values = new ArrayList<>();

    public SushiTab(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        if (values.isEmpty())
            values = FoodHelper.REGISTERED.values().stream().flatMap(Collection::stream).map(ItemStack::new).collect(Collectors.toList());
        return values.get(0);
    }

    @Override
    public ItemStack getIconItem() {
        makeIcon();
        return values.get((int) (Minecraft.getInstance().level.getGameTime() / 30 % values.size()));
    }
}
