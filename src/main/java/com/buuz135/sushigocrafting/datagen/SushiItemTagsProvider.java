package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

import java.util.List;
import java.util.Locale;
import java.util.Map;


public class SushiItemTagsProvider extends ForgeItemTagsProvider {

    public SushiItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
        super(gen, blockTagsProvider, existingFileHelper);
    }

    @Override
    public void registerTags() {
        //super.registerTags();
        this.copy(BlockTags.LOGS, ItemTags.LOGS);
        this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        for (CustomCropBlock block : new CustomCropBlock[]{SushiContent.Blocks.RICE_CROP.get(), SushiContent.Blocks.CUCUMBER_CROP.get(), SushiContent.Blocks.SOY_CROP.get(), SushiContent.Blocks.WASABI_CROP.get(), SushiContent.Blocks.SESAME_CROP.get()}) {
            getOrCreateBuilder(Tags.Items.SEEDS).add(block.getSeedsItem().asItem());
            getOrCreateBuilder(ItemTags.createOptional(new ResourceLocation("forge", "seeds/" + block.getSeedsItem().asItem().getRegistryName().getPath()))).add(block.getSeedsItem().asItem());
        }
        for (Item item : new Item[]{SushiContent.Items.RICE.get(), SushiContent.Items.CUCUMBER.get(), SushiContent.Items.SOY_BEAN.get(), SushiContent.Items.WASABI_ROOT.get(), SushiContent.Items.SESAME_SEED.get()}) {
            getOrCreateBuilder(Tags.Items.CROPS).add(item);
            getOrCreateBuilder(ItemTags.createOptional(new ResourceLocation("forge", "crops/" + item.getRegistryName().getPath()))).add(item.asItem());
        }
        getOrCreateBuilder(ItemTags.FISHES).add(SushiContent.Items.RAW_TUNA.get()).add(SushiContent.Items.SHRIMP.get());
        getOrCreateBuilder(ItemTags.createOptional(new ResourceLocation("forge", "tools/knife"))).add(SushiContent.Items.KNIFE_CLEAVER.get());
        for (Map.Entry<String, List<FoodItem>> stringListEntry : FoodHelper.REGISTERED.entrySet()) {
            for (FoodItem foodItem : stringListEntry.getValue()) {
                for (IFoodIngredient foodIngredient : foodItem.getIngredientList()) {
                    if (!foodIngredient.isEmpty()) {
                        getOrCreateBuilder(ItemTags.createOptional(new ResourceLocation("diet", foodIngredient.getDietType().name().toLowerCase(Locale.ROOT)))).add(foodItem);
                    }
                }
            }
        }
    }
}
