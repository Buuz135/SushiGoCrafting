package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class SushiItemTagsProvider extends ItemTagsProvider {

    public SushiItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, CompletableFuture<TagLookup<Block>> lookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, lookupCompletableFuture, SushiGoCrafting.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        //super.registerTags();
        this.copy(BlockTags.LOGS, ItemTags.LOGS);
        this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        for (CustomCropBlock block : new CustomCropBlock[]{SushiContent.Blocks.RICE_CROP.get(), SushiContent.Blocks.CUCUMBER_CROP.get(), SushiContent.Blocks.SOY_CROP.get(), SushiContent.Blocks.WASABI_CROP.get(), SushiContent.Blocks.SESAME_CROP.get()}) {
            tag(Tags.Items.SEEDS).add(block.getBaseSeedId().asItem());
            tag(ItemTags.create(new ResourceLocation("forge", "seeds/" + ForgeRegistries.ITEMS.getKey(block.getBaseSeedId().asItem()).getPath()))).add(block.getBaseSeedId().asItem());
        }
        for (Item item : new Item[]{SushiContent.Items.RICE.get(), SushiContent.Items.CUCUMBER.get(), SushiContent.Items.SOY_BEAN.get(), SushiContent.Items.WASABI_ROOT.get(), SushiContent.Items.SESAME_SEED.get()}) {
            tag(Tags.Items.CROPS).add(item);
            tag(ItemTags.create(new ResourceLocation("forge", "crops/" + ForgeRegistries.ITEMS.getKey(item).getPath()))).add(item.asItem());
        }
        tag(ItemTags.FISHES).add(SushiContent.Items.RAW_TUNA.get()).add(SushiContent.Items.SHRIMP.get());
        tag(ItemTags.create(new ResourceLocation("forge", "raw_fishes"))).add(Items.SALMON).add(SushiContent.Items.SHRIMP.get()).add(SushiContent.Items.RAW_TUNA.get());
        for (Item item : new Item[]{Items.SALMON, SushiContent.Items.SHRIMP.get(), SushiContent.Items.RAW_TUNA.get()}) {
            tag(ItemTags.create(new ResourceLocation("forge", "raw_fishes/" + (item.equals(SushiContent.Items.RAW_TUNA.get()) ? "tuna" : ForgeRegistries.ITEMS.getKey(item).getPath())))).add(item.asItem());
        }
        tag(ItemTags.create(new ResourceLocation("forge", "fruits"))).add(SushiContent.Items.AVOCADO.get());
        tag(ItemTags.create(new ResourceLocation("forge", "fruits/avocado"))).add(SushiContent.Items.AVOCADO.get());
        tag(ItemTags.create(new ResourceLocation("forge", "tools/knife"))).add(SushiContent.Items.KNIFE_CLEAVER.get());
        for (Map.Entry<String, List<RegistryObject<Item>>> stringListEntry : FoodHelper.REGISTERED.entrySet()) {
            for (RegistryObject<Item> foodItem : stringListEntry.getValue()) {
                for (IFoodIngredient foodIngredient : ((FoodItem)foodItem.get()).getIngredientList()) {
                    if (!foodIngredient.isEmpty()) {
                        tag(ItemTags.create(new ResourceLocation("diet", foodIngredient.getDietType().name().toLowerCase(Locale.ROOT)))).add(foodItem.get());
                    }
                }
            }
        }
    }
}
