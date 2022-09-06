package com.buuz135.sushigocrafting.entity;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ShrimpEntity extends AbstractSchoolingFish implements ItemSteerable, Saddleable {

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(ShrimpEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BOOST_TIME = SynchedEntityData.defineId(ShrimpEntity.class, EntityDataSerializers.INT);

    private final ItemBasedSteering steering = new ItemBasedSteering(this.entityData, BOOST_TIME, SADDLED);

    public ShrimpEntity(EntityType<? extends AbstractSchoolingFish> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SADDLED, false);
        this.entityData.define(BOOST_TIME, 0);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(SushiContent.Items.SHRIMP_BUCKET.get());
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource p_230266_1_) {
        this.steering.setSaddle(true);
        if (p_230266_1_ != null) {
            this.level.playSound(null, this, SoundEvents.PIG_SADDLE, p_230266_1_, 0.5F, 1.0F);
        }
    }

    @Override
    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        if (this.isSaddled() && !this.isVehicle() && !playerIn.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                playerIn.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            InteractionResult actionresulttype = super.mobInteract(playerIn, hand);
            if (!actionresulttype.consumesAction()) {
                ItemStack itemstack = playerIn.getItemInHand(hand);
                return itemstack.getItem() == Items.SADDLE ? itemstack.interactLivingEntity(playerIn, this, hand) : InteractionResult.PASS;
            } else {
                return actionresulttype;
            }
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (BOOST_TIME.equals(key) && this.level.isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(key);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.steering.addAdditionalSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.steering.readAdditionalSaveData(compound);
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
    }

    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    @Override
    public void travel(Vec3 travelVector) {
        Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (this.isVehicle() && this.canBeControlledByRider() && entity instanceof Player) {
            if (isInWater())
                this.setDeltaMovement(this.getDeltaMovement().add(0, -entity.getRotationVector().x / 500, 0));
            //this.ride(this, this.steering, new Vector3d(0,-entity.getPitchYaw().x,0));
        }
        this.travel(this, this.steering, travelVector);
    }

    @Override
    public float getSteeringSpeed() {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public void travelWithInput(Vec3 travelVec) {
        super.travel(travelVec);
    }

    @Override
    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(livingEntity);
        } else {
            int[][] aint = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

            for (Pose pose : livingEntity.getDismountPoses()) {
                AABB axisalignedbb = livingEntity.getLocalBoundsForPose(pose);

                for (int[] aint1 : aint) {
                    blockpos$mutable.set(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
                    double d0 = this.level.getBlockFloorHeight(blockpos$mutable);
                    if (DismountHelper.isBlockFloorValid(d0)) {
                        Vec3 vector3d = Vec3.upFromBottomCenterOf(blockpos$mutable, d0);
                        if (DismountHelper.canDismountTo(this.level, livingEntity, axisalignedbb.move(vector3d))) {
                            livingEntity.setPose(pose);
                            return vector3d;
                        }
                    }
                }
            }

            return super.getDismountLocationForPassenger(livingEntity);
        }
    }
    
    public boolean canBeControlledByRider() {
        Entity entity = this.getControllingPassenger();
        if (!(entity instanceof Player)) {
            return false;
        } else {
            Player playerentity = (Player) entity;
            return playerentity.getMainHandItem().getItem() == SushiContent.Items.SEAWEED_ON_A_STICK.get() || playerentity.getOffhandItem().getItem() == SushiContent.Items.SEAWEED_ON_A_STICK.get();
        }
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return super.isControlledByLocalInstance();
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98f;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getBbHeight() * 0.1D;
    }
}
