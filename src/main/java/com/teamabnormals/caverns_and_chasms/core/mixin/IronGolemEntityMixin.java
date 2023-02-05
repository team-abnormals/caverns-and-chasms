package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.ControllableGolem;
import com.teamabnormals.caverns_and_chasms.core.CCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Mixin(IronGolem.class)
public abstract class IronGolemEntityMixin extends AbstractGolem implements ControllableGolem {
	private static final EntityDataAccessor<Optional<UUID>> CONTROLLER_UUID = SynchedEntityData.defineId(IronGolem.class, EntityDataSerializers.OPTIONAL_UUID);

	private int forgetControllerTime;
	@Nullable
	private LivingEntity tuningForkTarget;
    @Nullable
    private BlockPos tuningForkPos;

	private IronGolemEntityMixin(EntityType<? extends AbstractGolem> entity, Level world) {
		super(entity, world);
	}

	@Shadow
	public abstract boolean isPlayerCreated();

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(CONTROLLER_UUID, Optional.empty());
    }

	@Inject(at = @At("HEAD"), method = "doPush")
	public void doPush(Entity entity, CallbackInfo ci) {
		if (entity instanceof Enemy && this.getRandom().nextInt(20) == 0) {
			this.setTarget((LivingEntity) entity);
		}
		super.doPush(entity);
	}

	@Inject(at = @At("RETURN"), method = "canAttackType", cancellable = true)
	public void canAttackType(EntityType<?> typeIn, CallbackInfoReturnable<Boolean> ci) {
		if (this.isPlayerCreated() && typeIn == EntityType.PLAYER || CCConfig.COMMON.creeperExplosionsDestroyBlocks.get() && typeIn == EntityType.CREEPER) {
			ci.setReturnValue(false);
		}
		ci.setReturnValue(super.canAttackType(typeIn));
	}

	@Override
	public boolean canBeControlled(Player controller) {
		return !((NeutralMob) this).isAngryAt(controller);
	}

	@Override
	public void onTuningForkControl(Player controller) {}

	@Override
	public boolean shouldMoveToTuningForkPos(BlockPos pos, Player controller) {
		return true;
	}

    @Override
    public boolean shouldAttackTuningForkTarget(LivingEntity target, Player controller) {
        return !(target instanceof Villager);
    }

    @Override
    public void setControllerUUID(UUID uuid) {
		this.entityData.set(CONTROLLER_UUID, Optional.ofNullable(uuid));
    }

    @Nullable
    @Override
    public UUID getControllerUUID() {
		return this.entityData.get(CONTROLLER_UUID).orElse((UUID) null);
    }

    @Override
    public void setForgetControllerTime(int time) {
        this.forgetControllerTime = time;
    }

    @Override
    public int getForgetControllerTime() {
        return this.forgetControllerTime;
    }

    @Override
    public void setTuningForkPos(BlockPos pos) {
        this.tuningForkPos = pos;
    }

    @Nullable
    @Override
    public BlockPos getTuningForkPos() {
        return this.tuningForkPos;
    }

	@Override
	public void setTuningForkTarget(LivingEntity target) {
		this.tuningForkTarget = target;
	}

	@Nullable
	@Override
	public LivingEntity getTuningForkTarget() {
		return this.tuningForkTarget;
	}
}
