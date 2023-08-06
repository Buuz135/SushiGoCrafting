package com.buuz135.sushigocrafting.entity;

import com.buuz135.sushigocrafting.entity.goal.DepositGoal;
import com.buuz135.sushigocrafting.entity.goal.WorkType;
import com.buuz135.sushigocrafting.entity.goal.chop.ChopPickupGoal;
import com.buuz135.sushigocrafting.entity.goal.chop.ChopWorkstationRefillGoal;
import com.buuz135.sushigocrafting.entity.goal.chop.ChopWorkstationWorkGoal;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class ItamaeCatEntity extends Cat {

    private static final EntityDataAccessor<BlockPos> TAKE_FROM = SynchedEntityData.defineId(ItamaeCatEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockPos> WORK_ON = SynchedEntityData.defineId(ItamaeCatEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockPos> DEPOSIT_TO = SynchedEntityData.defineId(ItamaeCatEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<ItemStack> HOLDING = SynchedEntityData.defineId(ItamaeCatEntity.class, EntityDataSerializers.ITEM_STACK);
    private static EntityDataSerializer<WorkType> WORK_TYPE_SER;
    private static final EntityDataAccessor<WorkType> WORK_TYPE = SynchedEntityData.defineId(ItamaeCatEntity.class, WORK_TYPE_SER);

    static {
        WORK_TYPE_SER = EntityDataSerializer.simpleEnum(WorkType.class);
        EntityDataSerializers.registerSerializer(WORK_TYPE_SER);
    }

    private WorkType workType;
    private BlockPos takeFromPosition;
    private BlockPos workOnPosition;
    private BlockPos depositToPosition;
    private ItemStack holdingStack;

    public ItamaeCatEntity(EntityType<? extends Cat> entityType, Level level) {
        super(entityType, level);
        this.workType = WorkType.FREE;
        this.takeFromPosition = BlockPos.ZERO;
        this.workOnPosition = BlockPos.ZERO;
        this.depositToPosition = BlockPos.ZERO;
        this.holdingStack = ItemStack.EMPTY;
    }

    public static void dropItemStack(Level p_18993_, double p_18994_, double p_18995_, double p_18996_, ItemStack p_18997_) {
        double d0 = (double) EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = p_18994_;
        double d4 = p_18995_;
        double d5 = p_18996_;

        while (!p_18997_.isEmpty()) {
            ItemEntity itementity = new ItemEntity(p_18993_, d3, d4, d5, p_18997_.split(p_18993_.random.nextInt(21) + 10));
            itementity.setPickUpDelay(Integer.MAX_VALUE);
            itementity.lifespan = 10;
            float f = 0.05F;
            itementity.setDeltaMovement(p_18993_.random.triangle(0.0D, 0.11485000171139836D), p_18993_.random.triangle(0.25D, 0.11485000171139836D), p_18993_.random.triangle(0.0D, 0.11485000171139836D));
            p_18993_.addFreshEntity(itementity);
        }

    }

    @Override
    public InteractionResult mobInteract(Player p_28153_, InteractionHand p_28154_) {
        //var all = FoodAPI.get().getFoodIngredient().stream().filter(iFoodIngredient -> !iFoodIngredient.isEmpty()).toList();
        //var foodIngredient = all.get(this.level.random.nextInt(all.size() - 1));
        //dropItemStack(p_28153_.getLevel(), this.getX(), this.getY() + 0.25, this.getZ(), new ItemStack(foodIngredient.getItem()));
        //return InteractionResult.SUCCESS;
        p_28153_.getItemInHand(p_28154_).interactLivingEntity(p_28153_, this, p_28154_);
        return super.mobInteract(p_28153_, p_28154_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new ChopPickupGoal(this));
        this.goalSelector.addGoal(5, new ChopWorkstationRefillGoal(this));
        this.goalSelector.addGoal(5, new ChopWorkstationWorkGoal(this));

        this.goalSelector.addGoal(5, new DepositGoal(this));
    }

    public WorkType getWorkType() {
        return workType;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public BlockPos getTakeFromPosition() {
        return takeFromPosition;
    }

    public boolean setTakeFromPosition(BlockPos takeFromPosition) {
        this.takeFromPosition = takeFromPosition;
        return true;
    }

    public BlockPos getWorkOnPosition() {
        return workOnPosition;
    }

    public boolean setWorkOnPosition(BlockPos workOnPosition) {
        this.workOnPosition = workOnPosition;
        if (this.level.getBlockState(workOnPosition).getBlock().equals(SushiContent.Blocks.CUTTING_BOARD.get())) {
            this.workOnPosition = workOnPosition;
            this.workType = WorkType.CHOP;
            return true;
        } else {
            this.workOnPosition = BlockPos.ZERO;
        }
        return false;
    }

    public BlockPos getDepositToPosition() {
        return depositToPosition;
    }

    public boolean setDepositToPosition(BlockPos depositToPosition) {
        this.depositToPosition = depositToPosition;
        return true;
    }

    public ItemStack getHoldingStack() {
        return getItemInHand(InteractionHand.MAIN_HAND);
    }

    public void setHoldingStack(ItemStack holdingStack) {
        setItemInHand(InteractionHand.MAIN_HAND, holdingStack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TAKE_FROM, BlockPos.ZERO);
        this.entityData.define(WORK_ON, BlockPos.ZERO);
        this.entityData.define(DEPOSIT_TO, BlockPos.ZERO);
        this.entityData.define(HOLDING, ItemStack.EMPTY);
        this.entityData.define(WORK_TYPE, WorkType.FREE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putLong("TAKE_FROM", this.takeFromPosition.asLong());
        compoundTag.putLong("WORK_ON", this.workOnPosition.asLong());
        compoundTag.putLong("DEPOSIT_TO", this.depositToPosition.asLong());
        compoundTag.put("HOLDING", this.holdingStack.serializeNBT());
        compoundTag.putString("WORK_TYPE", this.workType.toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.workType = WorkType.FREE;
        this.takeFromPosition = BlockPos.ZERO;
        this.workOnPosition = BlockPos.ZERO;
        this.depositToPosition = BlockPos.ZERO;
        this.holdingStack = ItemStack.EMPTY;
        if (compoundTag.contains("TAKE_FROM")) this.takeFromPosition = BlockPos.of(compoundTag.getLong("TAKE_FROM"));
        if (compoundTag.contains("WORK_ON")) this.workOnPosition = BlockPos.of(compoundTag.getLong("WORK_ON"));
        if (compoundTag.contains("DEPOSIT_TO")) this.depositToPosition = BlockPos.of(compoundTag.getLong("DEPOSIT_TO"));
        if (compoundTag.contains("HOLDING")) this.holdingStack = ItemStack.of(compoundTag.getCompound("HOLDING"));
        if (compoundTag.contains("WORK_TYPE")) this.workType = WorkType.valueOf(compoundTag.getString("WORK_TYPE"));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
