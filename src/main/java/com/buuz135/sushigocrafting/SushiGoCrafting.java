package com.buuz135.sushigocrafting;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CombineAmountItemRecipe;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> {
            RenderTypeLookup.setRenderLayer(SushiContent.Blocks.RICE_CROP.get(), RenderType.getCutout());
        }).subscribe();
        EventManager.modGeneric(RegistryEvent.Register.class, IRecipeSerializer.class).process(register -> ((RegistryEvent.Register) register).getRegistry().register(CombineAmountItemRecipe.SERIALIZER.setRegistryName(new ResourceLocation(MOD_ID, "amount_combine_recipe")))).subscribe();
    }


}
