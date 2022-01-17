package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Locale;
import java.util.Map;


public class SushiItemTagsProvider extends ItemTagsProvider {

    public SushiItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
        super(gen, blockTagsProvider, SushiGoCrafting.MOD_ID,existingFileHelper);
    }

    @Override
    public void addTags() {
        //super.registerTags();
        this.copy(BlockTags.LOGS, ItemTags.LOGS);
        this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        for (CustomCropBlock block : new CustomCropBlock[]{SushiContent.Blocks.RICE_CROP.get(), SushiContent.Blocks.CUCUMBER_CROP.get(), SushiContent.Blocks.SOY_CROP.get(), SushiContent.Blocks.WASABI_CROP.get(), SushiContent.Blocks.SESAME_CROP.get()}) {
            tag(Tags.Items.SEEDS).add(block.getBaseSeedId().asItem());
            tag(ItemTags.createOptional(new ResourceLocation("forge", "seeds/" + block.getBaseSeedId().asItem().getRegistryName().getPath()))).add(block.getBaseSeedId().asItem());
        }
        for (Item item : new Item[]{SushiContent.Items.RICE.get(), SushiContent.Items.CUCUMBER.get(), SushiContent.Items.SOY_BEAN.get(), SushiContent.Items.WASABI_ROOT.get(), SushiContent.Items.SESAME_SEED.get()}) {
            tag(Tags.Items.CROPS).add(item);
            tag(ItemTags.createOptional(new ResourceLocation("forge", "crops/" + item.getRegistryName().getPath()))).add(item.asItem());
        }
        tag(ItemTags.FISHES).add(SushiContent.Items.RAW_TUNA.get()).add(SushiContent.Items.SHRIMP.get());
        tag(ItemTags.createOptional(new ResourceLocation("forge", "raw_fishes"))).add(Items.SALMON).add(SushiContent.Items.SHRIMP.get()).add(SushiContent.Items.RAW_TUNA.get());
        for (Item item : new Item[]{Items.SALMON, SushiContent.Items.SHRIMP.get(), SushiContent.Items.RAW_TUNA.get()}) {
            tag(ItemTags.createOptional(new ResourceLocation("forge", "raw_fishes/" + (item.equals(SushiContent.Items.RAW_TUNA.get()) ? "tuna" : item.getRegistryName().getPath())))).add(item.asItem());
        }
        tag(ItemTags.createOptional(new ResourceLocation("forge", "fruits"))).add(SushiContent.Items.AVOCADO.get());
        tag(ItemTags.createOptional(new ResourceLocation("forge", "fruits/avocado"))).add(SushiContent.Items.AVOCADO.get());
        tag(ItemTags.createOptional(new ResourceLocation("forge", "tools/knife"))).add(SushiContent.Items.KNIFE_CLEAVER.get());
        for (Map.Entry<String, List<FoodItem>> stringListEntry : FoodHelper.REGISTERED.entrySet()) {
            for (FoodItem foodItem : stringListEntry.getValue()) {
                for (IFoodIngredient foodIngredient : foodItem.getIngredientList()) {
                    if (!foodIngredient.isEmpty()) {
                        tag(ItemTags.createOptional(new ResourceLocation("diet", foodIngredient.getDietType().name().toLowerCase(Locale.ROOT)))).add(foodItem);
                    }
                }
            }
        }
    }
}
