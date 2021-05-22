package com.buuz135.sushigocrafting;

import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.cap.ISushiWeightDiscovery;
import com.buuz135.sushigocrafting.cap.SushiDiscoveryProvider;
import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.buuz135.sushigocrafting.client.ClientProxy;
import com.buuz135.sushigocrafting.client.render.ContributorsBackRender;
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
import com.hrznstudio.titanium.TitaniumClient;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardGiver;
import com.hrznstudio.titanium.reward.RewardManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Mod(SushiGoCrafting.MOD_ID)
public class SushiGoCrafting {

    public static final String MOD_ID = "sushigocrafting";

    public static final ItemGroup TAB = new SushiTab(MOD_ID);
    public static NetworkHandler NETWORK = new NetworkHandler(MOD_ID);
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

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
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> new ClientProxy().fmlClient(fmlClientSetupEvent)).subscribe());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> EventManager.mod(ModelRegistryEvent.class).process(modelRegistryEvent -> {
            ModelLoader.addSpecialModel(new ResourceLocation(SushiGoCrafting.MOD_ID, "block/salmon_back"));
            ModelLoader.addSpecialModel(new ResourceLocation(SushiGoCrafting.MOD_ID, "block/tuna_back"));
        }).subscribe());
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
        EventManager.modGeneric(RegistryEvent.Register.class, EntityType.class)
                .process(register -> {
                    EntitySpawnPlacementRegistry.register(SushiContent.EntityTypes.SHRIMP.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
                    EntitySpawnPlacementRegistry.register(SushiContent.EntityTypes.TUNA.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractFishEntity::func_223363_b);
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
        EventManager.forge(LivingDropsEvent.class).filter(livingDropsEvent -> livingDropsEvent.getEntity() instanceof AbstractFishEntity).process(livingDropsEvent -> {
            if (livingDropsEvent.getEntity().world.getRandom().nextInt(10) <= 2) {
                livingDropsEvent.getDrops().add(new ItemEntity(livingDropsEvent.getEntity().world, livingDropsEvent.getEntity().getPosX(), livingDropsEvent.getEntity().getPosY(), livingDropsEvent.getEntity().getPosZ(), SushiContent.Items.TOBIKO.get().random(null, livingDropsEvent.getEntity().world)));
            }
        }).subscribe();
        RewardGiver giver = RewardManager.get().getGiver(UUID.fromString("d28b7061-fb92-4064-90fb-7e02b95a72a6"), "Buuz135");
        try {
            giver.addReward(new Reward(new ResourceLocation(SushiGoCrafting.MOD_ID, "back"), new URL("https://raw.githubusercontent.com/Buuz135/Industrial-Foregoing/master/contributors.json"), () -> dist -> {
                if (dist == Dist.CLIENT) {
                    registerReward();
                }
            }, new String[]{"salmon", "tuna"}));
        } catch (MalformedURLException e) {
            LOGGER.catching(e);
        }
        //EventManager.forge(ItemTooltipEvent.class).process(itemTooltipEvent -> {
        //    itemTooltipEvent.getToolTip().addAll(itemTooltipEvent.getItemStack().getItem().getTags().stream().map(resourceLocation -> new StringTextComponent(resourceLocation.toString())).collect(Collectors.toList()));
        //}).subscribe();
    }

    public void fmlCommon(FMLCommonSetupEvent event) {
        registerCapability();
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

    @OnlyIn(Dist.CLIENT)
    private void registerReward() {
        Minecraft instance = Minecraft.getInstance();
        EntityRendererManager manager = instance.getRenderManager();
        manager.getSkinMap().get("default").addLayer(new ContributorsBackRender(TitaniumClient.getPlayerRenderer(Minecraft.getInstance())));
        manager.getSkinMap().get("slim").addLayer(new ContributorsBackRender(TitaniumClient.getPlayerRenderer(Minecraft.getInstance())));
    }
}
