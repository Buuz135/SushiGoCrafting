package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WorkStickItem extends Item {
    public WorkStickItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand p_41401_) {
        if (entity instanceof ItamaeCatEntity catEntity && stack.getOrCreateTag().contains("Step") && stack.getOrCreateTag().contains("Target")) {
            int step = stack.getOrCreateTag().getInt("Step");
            if (step == 0 && catEntity.setWorkOnPosition(BlockPos.of(stack.getOrCreateTag().getLong("Target")))) {
                stack.getOrCreateTag().putInt("Step", 1);
            }
            if (step == 1 && catEntity.setTakeFromPosition(BlockPos.of(stack.getOrCreateTag().getLong("Target")))) {
                stack.getOrCreateTag().putInt("Step", 2);
            }
            if (step == 2 && catEntity.setDepositToPosition(BlockPos.of(stack.getOrCreateTag().getLong("Target")))) {
                stack.getOrCreateTag().putInt("Step", 0);
            }
        }
        return super.interactLivingEntity(stack, player, entity, p_41401_);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (!useOnContext.getItemInHand().getOrCreateTag().contains("Step")) {
            useOnContext.getItemInHand().getOrCreateTag().putInt("Step", 0);
        }
        useOnContext.getItemInHand().getOrCreateTag().putLong("Target", useOnContext.getClickedPos().asLong());
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        if (stack.getOrCreateTag().contains("Step")) {
            components.add(Component.literal("Step: " + stack.getOrCreateTag().getInt("Step")));
        }
        if (stack.getOrCreateTag().contains("Target")) {
            components.add(Component.literal("Target: " + BlockPos.of(stack.getOrCreateTag().getLong("Target")).toShortString()));
        }
        super.appendHoverText(stack, p_41422_, components, p_41424_);
    }

}
