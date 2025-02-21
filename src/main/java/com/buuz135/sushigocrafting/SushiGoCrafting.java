package com.buuz135.sushigocrafting;

import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.client.ClientProxy;
import com.buuz135.sushigocrafting.datagen.*;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.item.SushiDataComponent;
import com.buuz135.sushigocrafting.network.CapabilitySyncMessage;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.*;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardGiver;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.PistonEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;

@Mod(SushiGoCrafting.MOD_ID)
public class SushiGoCrafting extends ModuleController {

    public static final String MOD_ID = "sushigocrafting";

    public static final TitaniumTab TAB = new TitaniumTab(ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"));
    public static NetworkHandler NETWORK = new NetworkHandler(MOD_ID);
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    static {
        NeoForgeMod.enableMilkFluid();
        NETWORK.registerMessage("capability_sync", CapabilitySyncMessage.class);
    }

    public SushiGoCrafting(Dist dist, IEventBus modBus, ModContainer container) {
        super(container);
        SushiContent.Blocks.REGISTRY.register(modBus);
        SushiContent.Items.REGISTRY.register(modBus);
        SushiContent.TileEntities.REGISTRY.register(modBus);
        SushiContent.Effects.REGISTRY.register(modBus);
        SushiContent.EntityTypes.REGISTRY.register(modBus);
        SushiContent.LootSerializers.REGISTRY.register(modBus);
        SushiContent.RecipeSerializers.REGISTRY.register(modBus);
        SushiContent.RecipeTypes.REGISTRY.register(modBus);
        SushiDataComponent.REGISTRY.register(modBus);
        SushiContent.AttachmentTypes.REGISTRY.register(modBus);
        if (dist == Dist.CLIENT) {
            ClientProxy.register();
            EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> new ClientProxy().fmlClient(fmlClientSetupEvent)).subscribe();
        }
        EventManager.mod(FMLCommonSetupEvent.class).process(this::fmlCommon).subscribe();
        EventManager.mod(GatherDataEvent.class).process(this::dataGen).subscribe();
        NBTManager.getInstance().scanTileClassForAnnotations(RollerTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(RiceCookerTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(CuttingBoardTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(CoolerBoxTile.class);
        NBTManager.getInstance().scanTileClassForAnnotations(FermentationBarrelTile.class);
        EventManager.forge(PistonEvent.Pre.class).process(pre -> {
            if (pre.getLevel().getBlockState(pre.getPos().relative(pre.getDirection(), 2)).getBlock().equals(Blocks.IRON_BLOCK)) {
                NonNullList<ItemStack> list = NonNullList.create();
                var level = pre.getLevel();
                var aabb = new AABB(pre.getPos().relative(pre.getDirection(), 1));
                var entities = level.getEntitiesOfClass(ItemEntity.class, aabb, EntitySelector.ENTITY_STILL_ALIVE);
                for (ItemEntity entity : entities) {
                    if (entity.getItem().is(Items.DRIED_KELP_BLOCK)) {
                        list.add(new ItemStack(SushiContent.Items.NORI_SHEET.get(), (5 + pre.getLevel().getRandom().nextInt(4)) * entity.getItem().getCount()));
                        entity.remove(Entity.RemovalReason.KILLED);
                    }
                }
                if (!list.isEmpty()) {
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.playSeededSound(null, pre.getPos().getX(), pre.getPos().getY(), pre.getPos().getZ(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.75f, 1f, serverLevel.random.nextLong());
                    }
                    Containers.dropContents((Level) pre.getLevel(), pre.getFaceOffsetPos().offset(0, 0, 0), list);
                }
            }
        }).subscribe();
        EventManager.mod(EntityAttributeCreationEvent.class).process(entityAttributeCreationEvent -> {
            entityAttributeCreationEvent.put(SushiContent.EntityTypes.TUNA.get(), AbstractFish.createAttributes().build());
            entityAttributeCreationEvent.put(SushiContent.EntityTypes.SHRIMP.get(), AbstractFish.createAttributes().build());
        }).subscribe();
        EventManager.forge(LivingDropsEvent.class).filter(livingDropsEvent -> livingDropsEvent.getEntity() instanceof AbstractFish).process(livingDropsEvent -> {
            if (livingDropsEvent.getEntity().level().getRandom().nextInt(10) <= 2) {
                livingDropsEvent.getDrops().add(new ItemEntity(livingDropsEvent.getEntity().level(), livingDropsEvent.getEntity().getX(), livingDropsEvent.getEntity().getY(), livingDropsEvent.getEntity().getZ(), SushiContent.Items.TOBIKO.get().random(null, livingDropsEvent.getEntity().level())));
            }
        }).subscribe();
        RewardGiver giver = RewardManager.get().getGiver(UUID.fromString("d28b7061-fb92-4064-90fb-7e02b95a72a6"), "Buuz135");
        try {
            giver.addReward(new Reward(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "back"), new URL("https://raw.githubusercontent.com/Buuz135/Industrial-Foregoing/master/contributors.json"), () -> dist2 -> {

            }, new String[]{"salmon", "tuna"}));
        } catch (Exception e) {
            LOGGER.catching(e);
        }
        //EventManager.forge(ItemTooltipEvent.class).process(itemTooltipEvent -> {
        //    itemTooltipEvent.getToolTip().addAll(itemTooltipEvent.getItemStack().getItem().getTags().stream().map(resourceLocation -> new StringTextComponent(resourceLocation.toString())).collect(Collectors.toList()));
        //}).subscribe();
        EventManager.mod(RegisterSpawnPlacementsEvent.class).process(event -> {
            event.register(SushiContent.EntityTypes.SHRIMP.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
            event.register(SushiContent.EntityTypes.TUNA.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        }).subscribe();
        EventManager.mod(RegisterCapabilitiesEvent.class).process(event -> {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SushiContent.TileEntities.COOLER_BOX.value(), ActiveTile::getItemHandler);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SushiContent.TileEntities.CUTTING_BOARD.value(), ActiveTile::getItemHandler);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SushiContent.TileEntities.FERMENTATION_BARREL.value(), ActiveTile::getItemHandler);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SushiContent.TileEntities.RICE_COOKER.value(), ActiveTile::getItemHandler);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SushiContent.TileEntities.ROLLER.value(), ActiveTile::getItemHandler);

            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SushiContent.TileEntities.FERMENTATION_BARREL.value(), ActiveTile::getFluidHandler);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SushiContent.TileEntities.RICE_COOKER.value(), ActiveTile::getFluidHandler);
        }).subscribe();
    }

    @Override
    protected void initModules() {
        FoodAPI.get();
        FoodAPI.get().init();
        for (IFoodType value : FoodAPI.get().getFoodTypes()) {
            FoodHelper.generateFood(value).forEach(item -> {
                Supplier<Item> supplier = () -> {
                    FoodItem foodItem = new FoodItem(new Item.Properties(), item.getFoodType());
                    foodItem.getIngredientList().addAll(item.getFoodIngredients());
                    return foodItem;
                };
                FoodHelper.REGISTERED.computeIfAbsent(value.getName(), s -> new ArrayList<>()).add(SushiContent.item(FoodHelper.getName(item), supplier));
            });
        }

        addCreativeTab("main", () -> new ItemStack(FoodHelper.REGISTERED.values().stream().flatMap(Collection::stream).findFirst().get().get()), MOD_ID, TAB);
    }

    public void fmlCommon(FMLCommonSetupEvent event) {

    }

    public void dataGen(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new SushiModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new SushiBlockstateProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new SushiItemModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new SushiLangProvider(event.getGenerator(), MOD_ID, "en_us"));
        var provider = new SushiBlockTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        event.getGenerator().addProvider(true, provider);
        event.getGenerator().addProvider(true, new SushiItemTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), provider.contentsGetter(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(true, new SushiLootTableProvider(event.getGenerator(), event.getLookupProvider()));
        event.getGenerator().addProvider(true, new SushiRecipeProvider(event.getGenerator(), event.getLookupProvider()));
        event.getGenerator().addProvider(true, new SushiDataMapProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
    }


}
