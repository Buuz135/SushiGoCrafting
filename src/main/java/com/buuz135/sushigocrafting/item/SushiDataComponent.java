package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SushiDataComponent {
    public static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, SushiGoCrafting.MOD_ID);


    public static final ComponentSupplier<Integer> AMOUNT = register("amount", () -> 0, op -> op.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final ComponentSupplier<List<Integer>> FOOD_WEIGHTS = register("food_weights", ArrayList::new, op -> op.persistent(Codec.INT.listOf()).networkSynchronized(ByteBufCodecs.INT.apply(ByteBufCodecs.list())));
    public static final ComponentSupplier<CompoundTag> FOOD_SPICES = register("food_spices", CompoundTag::new, op -> op.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG));
    public static final ComponentSupplier<CompoundTag> TILE = register("tile", CompoundTag::new, op -> op.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG));


    private static <T> ComponentSupplier<T> register(String name, Supplier<T> defaultVal, UnaryOperator<DataComponentType.Builder<T>> op) {
        DeferredHolder<DataComponentType<?>, DataComponentType<T>> registered = REGISTRY.registerComponentType(name, builder -> op.apply(builder));
        return new ComponentSupplier<>(registered, defaultVal);
    }

    public static class ComponentSupplier<T> implements Supplier<DataComponentType<T>> {
        private final Supplier<DataComponentType<T>> type;
        private final Supplier<T> defaultSupplier;

        public ComponentSupplier(Supplier<DataComponentType<T>> type, Supplier<T> defaultSupplier) {
            this.type = type;
            this.defaultSupplier = Suppliers.memoize(defaultSupplier::get);
        }

        public T get(ItemStack stack) {
            return stack.getOrDefault(type.get(), defaultSupplier.get());
        }

        @Override
        public DataComponentType<T> get() {
            return type.get();
        }
    }
}
