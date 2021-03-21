package com.buuz135.sushigocrafting.entity;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ShrimpEntity extends AbstractGroupFishEntity implements IRideable, IEquipable {

    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(ShrimpEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> BOOST_TIME = EntityDataManager.createKey(ShrimpEntity.class, DataSerializers.VARINT);

    private final BoostHelper field_234214_bx_ = new BoostHelper(this.dataManager, BOOST_TIME, SADDLED);

    public ShrimpEntity(EntityType<? extends AbstractGroupFishEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SADDLED, false);
        this.dataManager.register(BOOST_TIME, 0);
    }

    @Override
    protected ItemStack getFishBucket() {
        return new ItemStack(SushiContent.Items.SHRIMP_BUCKET.get());
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }

    @Override
    public boolean func_230264_L__() {
        return this.isAlive();
    }

    @Override
    public void func_230266_a_(@Nullable SoundCategory p_230266_1_) {
        this.field_234214_bx_.setSaddledFromBoolean(true);
        if (p_230266_1_ != null) {
            this.world.playMovingSound((PlayerEntity) null, this, SoundEvents.ENTITY_PIG_SADDLE, p_230266_1_, 0.5F, 1.0F);
        }
    }

    @Override
    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        if (this.isHorseSaddled() && !this.isBeingRidden() && !playerIn.isSecondaryUseActive()) {
            if (!this.world.isRemote) {
                playerIn.startRiding(this);
            }
            return ActionResultType.func_233537_a_(this.world.isRemote);
        } else {
            ActionResultType actionresulttype = super.getEntityInteractionResult(playerIn, hand);
            if (!actionresulttype.isSuccessOrConsume()) {
                ItemStack itemstack = playerIn.getHeldItem(hand);
                return itemstack.getItem() == Items.SADDLE ? itemstack.interactWithEntity(playerIn, this, hand) : ActionResultType.PASS;
            } else {
                return actionresulttype;
            }
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        if (BOOST_TIME.equals(key) && this.world.isRemote) {
            this.field_234214_bx_.updateData();
        }

        super.notifyDataManagerChange(key);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        this.field_234214_bx_.setSaddledToNBT(compound);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.field_234214_bx_.setSaddledFromNBT(compound);
    }

    protected void dropInventory() {
        super.dropInventory();
        if (this.isHorseSaddled()) {
            this.entityDropItem(Items.SADDLE);
        }
    }

    @Override
    public boolean isHorseSaddled() {
        return this.field_234214_bx_.getSaddled();
    }

    @Override
    public void travel(Vector3d travelVector) {
        Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (this.isBeingRidden() && this.canBeSteered() && entity instanceof PlayerEntity) {
            if (isInWater()) this.setMotion(this.getMotion().add(0, -entity.getPitchYaw().x / 500, 0));
            //this.ride(this, this.field_234214_bx_, new Vector3d(0,-entity.getPitchYaw().x,0));
        }
        this.ride(this, this.field_234214_bx_, travelVector);
    }

    @Override
    public float getMountedSpeed() {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public void travelTowards(Vector3d travelVec) {
        super.travel(travelVec);
    }

    @Override
    public boolean boost() {
        return this.field_234214_bx_.boost(this.getRNG());
    }

    @Override
    public Vector3d getDismountPosition(LivingEntity livingEntity) {
        Direction direction = this.getAdjustedHorizontalFacing();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountPosition(livingEntity);
        } else {
            int[][] aint = TransportationHelper.func_234632_a_(direction);
            BlockPos blockpos = this.getPosition();
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

            for (Pose pose : livingEntity.getAvailablePoses()) {
                AxisAlignedBB axisalignedbb = livingEntity.getPoseAABB(pose);

                for (int[] aint1 : aint) {
                    blockpos$mutable.setPos(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
                    double d0 = this.world.func_242403_h(blockpos$mutable);
                    if (TransportationHelper.func_234630_a_(d0)) {
                        Vector3d vector3d = Vector3d.copyCenteredWithVerticalOffset(blockpos$mutable, d0);
                        if (TransportationHelper.func_234631_a_(this.world, livingEntity, axisalignedbb.offset(vector3d))) {
                            livingEntity.setPose(pose);
                            return vector3d;
                        }
                    }
                }
            }

            return super.getDismountPosition(livingEntity);
        }
    }

    @Override
    public boolean canBeSteered() {
        Entity entity = this.getControllingPassenger();
        if (!(entity instanceof PlayerEntity)) {
            return false;
        } else {
            PlayerEntity playerentity = (PlayerEntity) entity;
            return playerentity.getHeldItemMainhand().getItem() == Items.CARROT_ON_A_STICK || playerentity.getHeldItemOffhand().getItem() == Items.CARROT_ON_A_STICK;
        }
    }

    @Override
    public boolean canPassengerSteer() {
        return super.canPassengerSteer();
    }

    @Override
    public boolean canBeRiddenInWater() {
        return true;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98f;
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.getHeight() * 0.1D;
    }
}
