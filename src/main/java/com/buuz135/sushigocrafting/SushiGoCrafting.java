package com.buuz135.sushigocrafting;

import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.cap.ISushiWeightDiscovery;
import com.buuz135.sushigocrafting.cap.SushiDiscoveryProvider;
import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.buuz135.sushigocrafting.client.ClientProxy;
import com.buuz135.sushigocrafting.datagen.*;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.network.CapabilitySyncMessage;
import com.buuz135.sushigocrafting.proxy.SushiCompostables;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.*;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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

    public static final TitaniumTab TAB = new TitaniumTab(new ResourceLocation(MOD_ID, "main"));
    public static NetworkHandler NETWORK = new NetworkHandler(MOD_ID);
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    static {
        ForgeMod.enableMilkFluid();
        NETWORK.registerMessage(CapabilitySyncMessage.class);
    }

    public SushiGoCrafting() {
        SushiContent.Blocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.Items.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.TileEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.Effects.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.EntityTypes.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.LootSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.RecipeSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        SushiContent.RecipeTypes.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientProxy::register);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> new ClientProxy().fmlClient(fmlClientSetupEvent)).subscribe());
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
        registerCapability();
        SpawnPlacements.register(SushiContent.EntityTypes.SHRIMP.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkMobSpawnRules);
        SpawnPlacements.register(SushiContent.EntityTypes.TUNA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractFish::checkMobSpawnRules);
        SushiCompostables.init();
    }

    public void dataGen(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new SushiModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true,new SushiBlockstateProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true,new SushiItemModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(true,new SushiLangProvider(event.getGenerator(), MOD_ID, "en_us"));
        var provider = new SushiBlockTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        event.getGenerator().addProvider(true,provider);
        event.getGenerator().addProvider(true, new SushiItemTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), provider.contentsGetter(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(true,new SushiSerializableProvider(event.getGenerator(), MOD_ID));
        event.getGenerator().addProvider(true,new SushiLootTableProvider(event.getGenerator()));
        event.getGenerator().addProvider(true,new SushiRecipeProvider(event.getGenerator()));
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
                clone.getEntity().getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(future -> {
                    future.deserializeNBT(original.serializeNBT());
                    future.requestUpdate((ServerPlayer) clone.getEntity(), ItemStack.EMPTY);
                });
            });
        }).subscribe();
        EventManager.forge(PlayerEvent.PlayerLoggedInEvent.class)
                .filter(playerLoggedInEvent -> playerLoggedInEvent.getEntity() instanceof ServerPlayer)
                .process(playerLoggedInEvent -> playerLoggedInEvent.getEntity().getCapability(SushiWeightDiscoveryCapability.CAPABILITY)
                        .ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.requestUpdate((ServerPlayer) playerLoggedInEvent.getEntity(), ItemStack.EMPTY))).subscribe();
    }

}
