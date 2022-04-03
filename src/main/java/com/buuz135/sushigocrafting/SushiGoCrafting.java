package com.buuz135.sushigocrafting;

import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.cap.ISushiWeightDiscovery;
import com.buuz135.sushigocrafting.cap.SushiDiscoveryProvider;
import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.buuz135.sushigocrafting.client.ClientProxy;
import com.buuz135.sushigocrafting.datagen.*;
import com.buuz135.sushigocrafting.network.CapabilitySyncMessage;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CombineAmountItemRecipe;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import com.buuz135.sushigocrafting.tile.machinery.*;
import com.buuz135.sushigocrafting.world.SeaWeedFeatureHolders;
import com.buuz135.sushigocrafting.world.SushiTab;
import com.buuz135.sushigocrafting.world.tree.AvocadoTree;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardGiver;
import com.hrznstudio.titanium.reward.RewardManager;
import net.minecraft.core.NonNullList;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.UUID;

@Mod(SushiGoCrafting.MOD_ID)
public class SushiGoCrafting extends ModuleController {

    public static final String MOD_ID = "sushigocrafting";

    public static final CreativeModeTab TAB = new SushiTab(MOD_ID);
    public static NetworkHandler NETWORK = new NetworkHandler(MOD_ID);
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    static {
        ForgeMod.enableMilkFluid();
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
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientProxy::register);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> new ClientProxy().fmlClient(fmlClientSetupEvent)).subscribe());
        /*DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> EventManager.mod(ModelBakeEvent.class).process(modelBakeEvent -> {
            ModelLoader.addSpecialModel(new ResourceLocation(SushiGoCrafting.MOD_ID, "block/salmon_back"));
            ModelLoader.addSpecialModel(new ResourceLocation(SushiGoCrafting.MOD_ID, "block/tuna_back"));
        }).subscribe());

         */
        EventManager.mod(FMLCommonSetupEvent.class).process(this::fmlCommon).subscribe();
        EventManager.mod(GatherDataEvent.class).process(this::dataGen).subscribe();
        EventManager.modGeneric(RegistryEvent.Register.class, RecipeSerializer.class)
                .process(register -> ((RegistryEvent.Register) register).getRegistry()
                        .registerAll(CombineAmountItemRecipe.SERIALIZER.setRegistryName(new ResourceLocation(MOD_ID, "amount_combine_recipe")),
                                CuttingBoardRecipe.SERIALIZER,
                                FermentingBarrelRecipe.SERIALIZER
                        )).subscribe();
        EventManager.modGeneric(RegistryEvent.Register.class, Item.class).process(register -> {
            for (IFoodType value : FoodAPI.get().getFoodTypes()) {
                FoodHelper.generateFood(value).forEach(item -> {
                    var itemConfigured = item.setRegistryName(new ResourceLocation(MOD_ID, FoodHelper.getName(item)));
                    ((RegistryEvent.Register<Item>) register).getRegistry().register(itemConfigured);
                });
            }
        }).subscribe();
        NBTManager.getInstance().scanTileClassForAnnotations(RollerTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(RiceCookerTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(CuttingBoardTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(CoolerBoxTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(FermentationBarrelTile.class);
        EventManager.forge(BiomeLoadingEvent.class).filter(biomeLoadingEvent -> biomeLoadingEvent.getCategory() == Biome.BiomeCategory.OCEAN).process(biomeLoadingEvent -> {
            biomeLoadingEvent.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SeaWeedFeatureHolders.PLACEMENT);
            //holder.configured(FeatureConfiguration.NONE).placed(NoiseBasedCountPlacement.of(80, 80.0D, 0.0D), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
            //.decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP.squared().decorated(FeatureDecorator.COUNT_NOISE_BIASED.configured(new NoiseCountFactorDecoratorConfiguration(80, 80.0D, 0.0D)))));
        }).subscribe();
        EventManager.forge(BiomeLoadingEvent.class).filter(biomeLoadingEvent -> biomeLoadingEvent.getCategory() == Biome.BiomeCategory.OCEAN || biomeLoadingEvent.getCategory() == Biome.BiomeCategory.RIVER).process(biomeLoadingEvent -> {
            biomeLoadingEvent.getSpawns().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(SushiContent.EntityTypes.TUNA.get(), 8, 3, 6));
            biomeLoadingEvent.getSpawns().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(SushiContent.EntityTypes.SHRIMP.get(), 10, 6, 9));
        }).subscribe();
        EventManager.modGeneric(RegistryEvent.Register.class, EntityType.class)
                .process(register -> {
                    SpawnPlacements.register(SushiContent.EntityTypes.SHRIMP.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkMobSpawnRules);
                    SpawnPlacements.register(SushiContent.EntityTypes.TUNA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkMobSpawnRules);
                }).subscribe();
        EventManager.forge(BiomeLoadingEvent.class).filter(biomeLoadingEvent -> biomeLoadingEvent.getCategory() == Biome.BiomeCategory.PLAINS).process(biomeLoadingEvent -> {
            biomeLoadingEvent.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AvocadoTree.PLACEMENT);
            //Feature.TREE.configured(AvocadoTree.TREE).placed(PlacementUtils.countExtra(0, 0.05F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome()));
            //.decorated(Features.Decorators.HEIGHTMAP_SQUARE)
            //.decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(6))));
        }).subscribe();
        EventManager.forge(PistonEvent.Pre.class).process(pre -> {
            if (pre.getWorld().getBlockState(pre.getFaceOffsetPos()).getBlock().equals(SushiContent.Blocks.SEAWEED_BLOCK.get()) && pre.getWorld().getBlockState(pre.getPos().relative(pre.getDirection(), 2)).getBlock().equals(Blocks.IRON_BLOCK)) {
                pre.getWorld().destroyBlock(pre.getFaceOffsetPos(), false);
                NonNullList<ItemStack> list = NonNullList.create();
                list.add(new ItemStack(SushiContent.Items.NORI_SHEET.get(), 5 + pre.getWorld().getRandom().nextInt(4)));
                Containers.dropContents((Level) pre.getWorld(), pre.getFaceOffsetPos().offset(0.5, 0.5, 0.5), list);
            }
        }).subscribe();
        EventManager.mod(EntityAttributeCreationEvent.class).process(entityAttributeCreationEvent -> {
            entityAttributeCreationEvent.put(SushiContent.EntityTypes.TUNA.get(), AbstractFish.createAttributes().build());
            entityAttributeCreationEvent.put(SushiContent.EntityTypes.SHRIMP.get(), AbstractFish.createAttributes().build());
        }).subscribe();
        EventManager.forge(LivingDropsEvent.class).filter(livingDropsEvent -> livingDropsEvent.getEntity() instanceof AbstractFish).process(livingDropsEvent -> {
            if (livingDropsEvent.getEntity().level.getRandom().nextInt(10) <= 2) {
                livingDropsEvent.getDrops().add(new ItemEntity(livingDropsEvent.getEntity().level, livingDropsEvent.getEntity().getX(), livingDropsEvent.getEntity().getY(), livingDropsEvent.getEntity().getZ(), SushiContent.Items.TOBIKO.get().random(null, livingDropsEvent.getEntity().level)));
            }
        }).subscribe();
        RewardGiver giver = RewardManager.get().getGiver(UUID.fromString("d28b7061-fb92-4064-90fb-7e02b95a72a6"), "Buuz135");
        try {
            giver.addReward(new Reward(new ResourceLocation(SushiGoCrafting.MOD_ID, "back"), new URL("https://raw.githubusercontent.com/Buuz135/Industrial-Foregoing/master/contributors.json"), () -> dist -> {

            }, new String[]{"salmon", "tuna"}));
        } catch (Exception e) {
            LOGGER.catching(e);
        }
        //EventManager.forge(ItemTooltipEvent.class).process(itemTooltipEvent -> {
        //    itemTooltipEvent.getToolTip().addAll(itemTooltipEvent.getItemStack().getItem().getTags().stream().map(resourceLocation -> new StringTextComponent(resourceLocation.toString())).collect(Collectors.toList()));
        //}).subscribe();
    }

    @Override
    protected void initModules() {

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
        EventManager.mod(RegisterCapabilitiesEvent.class).process(registerCapabilitiesEvent -> {
            registerCapabilitiesEvent.register(ISushiWeightDiscovery.class);
        }).subscribe();
        EventManager.forgeGeneric(AttachCapabilitiesEvent.class, Entity.class)
                .filter(attachCapabilitiesEvent -> ((AttachCapabilitiesEvent) attachCapabilitiesEvent).getObject() instanceof Player)
                .process(attachCapabilitiesEvent -> ((AttachCapabilitiesEvent) attachCapabilitiesEvent).addCapability(new ResourceLocation(MOD_ID, "weight_discovery"), new SushiDiscoveryProvider()))
                .subscribe();
        EventManager.forge(PlayerEvent.Clone.class).process(clone -> {
            clone.getOriginal().getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(original -> {
                clone.getPlayer().getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(future -> {
                    future.deserializeNBT(original.serializeNBT());
                    future.requestUpdate((ServerPlayer) clone.getPlayer(), ItemStack.EMPTY);
                });
            });
        }).subscribe();
        EventManager.forge(PlayerEvent.PlayerLoggedInEvent.class)
                .filter(playerLoggedInEvent -> playerLoggedInEvent.getPlayer() instanceof ServerPlayer)
                .process(playerLoggedInEvent -> playerLoggedInEvent.getPlayer().getCapability(SushiWeightDiscoveryCapability.CAPABILITY)
                        .ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.requestUpdate((ServerPlayer) playerLoggedInEvent.getPlayer(), ItemStack.EMPTY))).subscribe();
    }

}
