package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SushiDataComponent {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SushiGoCrafting.MOD_ID);


    public static final Supplier<DataComponentType<Integer>> AMOUNT = register("amount", () -> 0, op -> op.persistent(Codec.INT));
    public static final Supplier<DataComponentType<List<Integer>>> FOOD_WEIGHTS = register("food_weights", ArrayList::new, op -> op.persistent(Codec.INT.listOf()));
    public static final Supplier<DataComponentType<CompoundTag>> FOOD_SPICES = register("food_spices", CompoundTag::new, op -> op.persistent(CompoundTag.CODEC));
    public static final Supplier<DataComponentType<CompoundTag>> TILE = register("tile", CompoundTag::new, op -> op.persistent(CompoundTag.CODEC));


    private static <T> ComponentSupplier<T> register(String name, Supplier<T> defaultVal, UnaryOperator<DataComponentType.Builder<T>> op) {
        var registered = REGISTRY.register(name, () -> op.apply(DataComponentType.builder()).build());
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
            return stack.getOrDefault(type, defaultSupplier.get());
        }

        @Override
        public DataComponentType<T> get() {
            return type.get();
        }
    }
}
