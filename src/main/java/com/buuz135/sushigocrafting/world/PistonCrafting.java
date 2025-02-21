package com.buuz135.sushigocrafting.world;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.level.PistonEvent;
import static com.buuz135.sushigocrafting.SushiGoCrafting.MOD_ID;

public class PistonCrafting {

    public PistonCrafting(){
        EventManager.forge(PistonEvent.Pre.class).process(pre -> {
            BlockPos targetPos = pre.getPos().relative(pre.getDirection(), 2);
            BlockState targetBlockState = pre.getLevel().getBlockState(targetPos);

            if(targetBlockState.is(SushiContent.Tags.PRESSING_BASE)){
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
    }
}
