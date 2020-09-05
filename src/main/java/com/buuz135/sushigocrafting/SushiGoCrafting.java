package com.buuz135.sushigocrafting;

import com.buuz135.sushigocrafting.api.FoodHelper;
import com.buuz135.sushigocrafting.api.FoodType;
import com.buuz135.sushigocrafting.datagen.SushiBlockstateProvider;
import com.buuz135.sushigocrafting.datagen.SushiItemModelProvider;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CombineAmountItemRecipe;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SushiGoCrafting.MOD_ID)
public class SushiGoCrafting {

    public static final String MOD_ID = "sushigocrafting";

    public static final ItemGroup TAB = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(SushiContent.Items.RICE_SEED.get());
        }
    };

    //private static CommonProxy proxy;

    public SushiGoCrafting() {
        //proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        SushiContent.Blocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.Items.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.Features.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        EventManager.mod(FMLClientSetupEvent.class).process(this::fmlClient).subscribe();
        EventManager.mod(FMLCommonSetupEvent.class).process(this::fmlCommon).subscribe();
        EventManager.mod(GatherDataEvent.class).process(this::dataGen).subscribe();
        EventManager.modGeneric(RegistryEvent.Register.class, IRecipeSerializer.class).process(register -> ((RegistryEvent.Register) register).getRegistry().register(CombineAmountItemRecipe.SERIALIZER.setRegistryName(new ResourceLocation(MOD_ID, "amount_combine_recipe")))).subscribe();
        for (FoodType value : FoodType.values()) {
            FoodHelper.generateFood(value).forEach(item -> SushiContent.Items.REGISTRY.register(FoodHelper.getName(item), () -> item));
        }
    }

    public void fmlCommon(FMLCommonSetupEvent event) {
        Biome[] biomes = new Biome[]{Biomes.OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.COLD_OCEAN};
        for (Biome biome : biomes) {
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SushiContent.Features.SEAWEED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(120, 80.0D, 0.0D, Heightmap.Type.OCEAN_FLOOR_WG))));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void fmlClient(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.RICE_CROP.get(), RenderType.getCutout());
    }

    public void dataGen(GatherDataEvent event) {
        //event.getGenerator().addProvider(new SushiModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new SushiBlockstateProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new SushiItemModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
    }

}
