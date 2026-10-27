package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class SushiItemTagsProvider extends ItemTagsProvider {

    public SushiItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, completableFuture, SushiGoCrafting.MOD_ID);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.LOGS).add(SushiContent.Blocks.AVOCADO_LOG.get().asItem(), SushiContent.Blocks.AVOCADO_LEAVES_LOG.get().asItem());
        tag(ItemTags.SAPLINGS).add(SushiContent.Blocks.AVOCADO_SAPLING.get().asItem());
        for (CustomCropBlock block : new CustomCropBlock[]{SushiContent.Blocks.RICE_CROP.get(), SushiContent.Blocks.CUCUMBER_CROP.get(), SushiContent.Blocks.SOY_CROP.get(), SushiContent.Blocks.WASABI_CROP.get(), SushiContent.Blocks.SESAME_CROP.get()}) {
            tag(Tags.Items.SEEDS).add(block.getBaseSeedId().asItem());
            tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "seeds/" + getNameFor(BuiltInRegistries.ITEM.getKey(block.getBaseSeedId().asItem()).getPath())))).add(block.getBaseSeedId().asItem());
        }
        for (Item item : new Item[]{SushiContent.Items.RICE.get(), SushiContent.Items.CUCUMBER.get(), SushiContent.Items.SOY_BEAN.get(), SushiContent.Items.WASABI_ROOT.get(), SushiContent.Items.SESAME_SEED.get()}) {
            tag(Tags.Items.CROPS).add(item);
            tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "crops/" + getNameFor(BuiltInRegistries.ITEM.getKey(item).getPath())))).add(item.asItem());
        }
        tag(ItemTags.FISHES).add(SushiContent.Items.RAW_TUNA.get()).add(SushiContent.Items.SHRIMP.get());
        tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "raw_fishes"))).add(Items.SALMON).add(SushiContent.Items.SHRIMP.get()).add(SushiContent.Items.RAW_TUNA.get());
        for (Item item : new Item[]{Items.SALMON, SushiContent.Items.SHRIMP.get(), SushiContent.Items.RAW_TUNA.get()}) {
            tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "raw_fishes/" + (item.equals(SushiContent.Items.RAW_TUNA.get()) ? "tuna" : BuiltInRegistries.ITEM.getKey(item).getPath())))).add(item.asItem());
        }
        tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "fruits"))).add(SushiContent.Items.AVOCADO.get());
        tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "fruits/avocado"))).add(SushiContent.Items.AVOCADO.get());
        tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "tools/knives"))).add(SushiContent.Items.KNIFE_CLEAVER.get());
        for (Map.Entry<String, List<DeferredHolder<Item, Item>>> stringListEntry : FoodHelper.REGISTERED.entrySet()) {
            for (DeferredHolder<Item, Item> foodItem : stringListEntry.getValue()) {
                for (IFoodIngredient foodIngredient : ((FoodItem) foodItem.get()).getIngredientList()) {
                    if (!foodIngredient.isEmpty()) {
                        tag(ItemTags.create(Identifier.fromNamespaceAndPath("diet", foodIngredient.getDietType().name().toLowerCase(Locale.ROOT)))).add(foodItem.get());
                    }
                }
            }
        }
    }

    public String getNameFor(String name){
        if (name.equals("soy_bean")) return "soybean";
        return name.replaceAll("_seeds", "");
    }
}
