package com.buuz135.sushigocrafting.proxy;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.RegistryObject;

public class SushiCompostables {


    public static void init() {
        register(0.3F,
                SushiContent.Items.RICE_SEEDS,
                SushiContent.Items.CUCUMBER_SEEDS,
                SushiContent.Items.SOY_SEEDS,
                SushiContent.Items.WASABI_SEEDS,
                SushiContent.Items.SESAME_SEEDS,
                SushiContent.Items.AVOCADO_LEAVES,
                SushiContent.Items.AVOCADO_SAPLING,
                SushiContent.Items.SEAWEED
        );
        register(0.5F,
                SushiContent.Items.SEAWEED_BLOCK
        );
        register(0.65F,
                SushiContent.Items.AVOCADO,
                SushiContent.Items.CUCUMBER,
                SushiContent.Items.SOY_BEAN,
                SushiContent.Items.WASABI_ROOT
        );
    }

    @SafeVarargs
    private static void register(float chance, RegistryObject<? extends ItemLike>... items) {
        for (RegistryObject<? extends ItemLike> item : items) {
            register(item.get(), chance);
        }
    }

    private static void register(ItemLike item, float chance) {
        ComposterBlock.COMPOSTABLES.put(item, chance);
    }
}
