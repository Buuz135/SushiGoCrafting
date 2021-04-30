package com.buuz135.sushigocrafting;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.IIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.api.impl.effect.AddIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.effect.ModifyIngredientEffect;
import com.buuz135.sushigocrafting.cap.ISushiWeightDiscovery;
import com.buuz135.sushigocrafting.cap.SushiDiscoveryProvider;
import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.buuz135.sushigocrafting.client.entity.ShrimpRenderer;
import com.buuz135.sushigocrafting.client.entity.TunaRenderer;
import com.buuz135.sushigocrafting.client.tesr.CuttingBoardRenderer;
import com.buuz135.sushigocrafting.datagen.*;
import com.buuz135.sushigocrafting.network.CapabilitySyncMessage;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CombineAmountItemRecipe;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.tile.machinery.CoolerBoxTile;
import com.buuz135.sushigocrafting.tile.machinery.CuttingBoardTile;
import com.buuz135.sushigocrafting.tile.machinery.RiceCookerTile;
import com.buuz135.sushigocrafting.tile.machinery.RollerTile;
import com.buuz135.sushigocrafting.world.SushiTab;
import com.buuz135.sushigocrafting.world.tree.AvocadoTree;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import com.hrznstudio.titanium.network.NetworkHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SushiGoCrafting.MOD_ID)
public class SushiGoCrafting {

    public static final String MOD_ID = "sushigocrafting";

    public static final ItemGroup TAB = new SushiTab(MOD_ID);
    public static NetworkHandler NETWORK = new NetworkHandler(MOD_ID);

    static {
        NETWORK.registerMessage(CapabilitySyncMessage.class);
    }

    public SushiGoCrafting() {
        FoodAPI.get();
        FoodAPI.get().init();
        SushiContent.Blocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.Items.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.Features.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.TileEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.Effects.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.EntityTypes.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.LootSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        EventManager.mod(FMLClientSetupEvent.class).process(this::fmlClient).subscribe();
        EventManager.mod(FMLCommonSetupEvent.class).process(this::fmlCommon).subscribe();
        EventManager.mod(GatherDataEvent.class).process(this::dataGen).subscribe();
        EventManager.modGeneric(RegistryEvent.Register.class, IRecipeSerializer.class)
                .process(register -> ((RegistryEvent.Register) register).getRegistry()
                        .registerAll(CombineAmountItemRecipe.SERIALIZER.setRegistryName(new ResourceLocation(MOD_ID, "amount_combine_recipe")),
                                CuttingBoardRecipe.SERIALIZER
                        )).subscribe();
        for (IFoodType value : FoodAPI.get().getFoodTypes()) {
            FoodHelper.generateFood(value).forEach(item -> SushiContent.Items.REGISTRY.register(FoodHelper.getName(item), () -> item));
        }
        NBTManager.getInstance().scanTileClassForAnnotations(RollerTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(RiceCookerTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(CuttingBoardTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(CoolerBoxTile.class);
        EventManager.forge(BiomeLoadingEvent.class).filter(biomeLoadingEvent -> biomeLoadingEvent.getCategory() == Biome.Category.OCEAN).process(biomeLoadingEvent -> {
            biomeLoadingEvent.getSpawns().withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(SushiContent.EntityTypes.TUNA.get(), 8, 3, 6));
            biomeLoadingEvent.getSpawns().withSpawner(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(SushiContent.EntityTypes.SHRIMP.get(), 10, 6, 9));
            biomeLoadingEvent.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() ->
                    SushiContent.Features.SEAWEED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                            .withPlacement(Features.Placements.KELP_PLACEMENT.square().withPlacement(Placement.COUNT_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(80, 80.0D, 0.0D)))));
        }).subscribe();
        EventManager.forge(BiomeLoadingEvent.class).filter(biomeLoadingEvent -> biomeLoadingEvent.getCategory() == Biome.Category.PLAINS).process(biomeLoadingEvent -> {
            biomeLoadingEvent.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() ->
                    Feature.TREE.withConfiguration(AvocadoTree.TREE)
                            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                            .withPlacement(Placement.CHANCE.configure(new ChanceConfig(6))));
        }).subscribe();
        EventManager.forge(PistonEvent.Pre.class).process(pre -> {
            if (pre.getWorld().getBlockState(pre.getFaceOffsetPos()).getBlock().equals(SushiContent.Blocks.SEAWEED_BLOCK.get()) && pre.getWorld().getBlockState(pre.getPos().offset(pre.getDirection(), 2)).getBlock().equals(Blocks.IRON_BLOCK)) {
                pre.getWorld().destroyBlock(pre.getFaceOffsetPos(), false);
                NonNullList<ItemStack> list = NonNullList.create();
                list.add(new ItemStack(SushiContent.Items.NORI_SHEET.get(), 3 + pre.getWorld().getRandom().nextInt(3)));
                InventoryHelper.dropItems((World) pre.getWorld(), pre.getFaceOffsetPos().add(0.5, 0.5, 0.5), list);
            }
        }).subscribe();
        EventManager.mod(EntityAttributeCreationEvent.class).process(entityAttributeCreationEvent -> {
            entityAttributeCreationEvent.put(SushiContent.EntityTypes.TUNA.get(), AbstractFishEntity.func_234176_m_().create());
            entityAttributeCreationEvent.put(SushiContent.EntityTypes.SHRIMP.get(), AbstractFishEntity.func_234176_m_().create());
        }).subscribe();
        EventManager.forge(ItemTooltipEvent.class).process(event -> {
            IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(event.getItemStack().getItem());
            if (!ingredient.isEmpty() && ingredient.getEffect() != null) {
                event.getToolTip().add(new StringTextComponent(""));
                if (Screen.hasShiftDown()) {
                    IIngredientEffect effect = ingredient.getEffect();
                    if (effect instanceof AddIngredientEffect) {
                        event.getToolTip().add(new StringTextComponent(TextFormatting.DARK_AQUA + "Adds Food Effect:"));
                        event.getToolTip().add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + ((AddIngredientEffect) effect).getEffect().get().getDisplayName().getString() + TextFormatting.DARK_AQUA + " (" + TextFormatting.WHITE + ((AddIngredientEffect) effect).getDuration() / 20 + TextFormatting.YELLOW + "s" + TextFormatting.DARK_AQUA + ", " + TextFormatting.YELLOW + "Level " + TextFormatting.WHITE + (((AddIngredientEffect) effect).getLevel() + 1) + TextFormatting.DARK_AQUA + ")"));
                    }
                    if (effect instanceof ModifyIngredientEffect) {
                        event.getToolTip().add(new StringTextComponent(TextFormatting.DARK_AQUA + "Modifies Food Effect:"));
                        event.getToolTip().add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + " Multiplies Time By " + TextFormatting.WHITE + ((ModifyIngredientEffect) effect).getTimeModifier()));
                        if (((ModifyIngredientEffect) effect).getLevelModifier() > 0)
                            event.getToolTip().add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + " Increases Level By " + TextFormatting.WHITE + ((ModifyIngredientEffect) effect).getLevelModifier()));
                    }
                } else {
                    event.getToolTip().add(new StringTextComponent(TextFormatting.YELLOW + "Hold " + TextFormatting.GOLD + "" + TextFormatting.ITALIC + "<Shift>" + TextFormatting.RESET + TextFormatting.YELLOW + " for sushi effect"));
                }
            }
        }).subscribe();
        EventManager.forge(LivingDropsEvent.class).filter(livingDropsEvent -> livingDropsEvent.getEntity() instanceof AbstractFishEntity).process(livingDropsEvent -> {
            if (livingDropsEvent.getEntity().world.getRandom().nextInt(10) <= 2) {
                livingDropsEvent.getDrops().add(new ItemEntity(livingDropsEvent.getEntity().world, livingDropsEvent.getEntity().getPosX(), livingDropsEvent.getEntity().getPosY(), livingDropsEvent.getEntity().getPosZ(), SushiContent.Items.TOBIKO.get().random(null, livingDropsEvent.getEntity().world)));
            }
        }).subscribe();
    }

    public void fmlCommon(FMLCommonSetupEvent event) {
        registerCapability();
    }

    @OnlyIn(Dist.CLIENT)
    public void fmlClient(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.RICE_CROP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.CUCUMBER_CROP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.SOY_CROP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.WASABI_CROP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.AVOCADO_LEAVES_LOG.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.AVOCADO_LEAVES.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.AVOCADO_SAPLING.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.SEAWEED.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.SEAWEED_PLANT.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.SESAME_CROP.get(), RenderType.getCutout());
        RenderingRegistry.registerEntityRenderingHandler(SushiContent.EntityTypes.TUNA.get(), TunaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SushiContent.EntityTypes.SHRIMP.get(), ShrimpRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SushiContent.TileEntities.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
    }

    public void dataGen(GatherDataEvent event) {
        event.getGenerator().addProvider(new SushiModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new SushiBlockstateProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new SushiItemModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new SushiLangProvider(event.getGenerator(), MOD_ID, "en_us"));
        BlockTagsProvider provider = new SushiBlockTagsProvider(event.getGenerator(), event.getExistingFileHelper());
        event.getGenerator().addProvider(provider);
        event.getGenerator().addProvider(new SushiItemTagsProvider(event.getGenerator(), provider, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new SushiSerializableProvider(event.getGenerator(), MOD_ID));
        event.getGenerator().addProvider(new SushiLootTableProvider(event.getGenerator()));
        event.getGenerator().addProvider(new SushiRecipeProvider(event.getGenerator()));
    }

    private void registerCapability() {
        CapabilityManager.INSTANCE.register(ISushiWeightDiscovery.class, new SushiWeightDiscoveryCapability.Storage(), SushiWeightDiscoveryCapability::new);
        EventManager.forgeGeneric(AttachCapabilitiesEvent.class, Entity.class)
                .filter(attachCapabilitiesEvent -> ((AttachCapabilitiesEvent) attachCapabilitiesEvent).getObject() instanceof PlayerEntity)
                .process(attachCapabilitiesEvent -> ((AttachCapabilitiesEvent) attachCapabilitiesEvent).addCapability(new ResourceLocation(MOD_ID, "weight_discovery"), new SushiDiscoveryProvider()))
                .subscribe();
        EventManager.forge(PlayerEvent.Clone.class).process(clone -> {
            clone.getOriginal().getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(original -> {
                clone.getPlayer().getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(future -> {
                    future.deserializeNBT(original.serializeNBT());
                    future.requestUpdate((ServerPlayerEntity) clone.getPlayer(), false);
                });
            });
        }).subscribe();
        EventManager.forge(PlayerEvent.PlayerLoggedInEvent.class)
                .filter(playerLoggedInEvent -> playerLoggedInEvent.getPlayer() instanceof ServerPlayerEntity)
                .process(playerLoggedInEvent -> playerLoggedInEvent.getPlayer().getCapability(SushiWeightDiscoveryCapability.CAPABILITY)
                        .ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.requestUpdate((ServerPlayerEntity) playerLoggedInEvent.getPlayer(), false))).subscribe();
    }
}
