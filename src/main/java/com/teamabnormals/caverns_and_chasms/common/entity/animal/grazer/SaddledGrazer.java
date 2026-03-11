package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import com.teamabnormals.caverns_and_chasms.common.network.C2SGrazerJumpMessage;
import com.teamabnormals.caverns_and_chasms.common.network.S2COpenStorageDuctMessage;
import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

public class SaddledGrazer extends AbstractGrazer implements PlayerRideableJumping {
	private float playerJumpPendingScale;

	public SaddledGrazer(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	// TODO
	// Don't run or be stupid when being ridden
	// For some reason grazers can open their mouth when being stupid, but only on client side
	// Make rider unable to hit their grazer

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!this.isVehicle() && !this.isBaby() && !player.isSecondaryUseActive()) {
			if (!this.level().isClientSide)
				player.startRiding(this);
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
		return CCEntityTypes.SADDLED_GRAZER.get().create(level);
	}

	@Override
	public LivingEntity getControllingPassenger() {
		Entity entity = this.getFirstPassenger();
		if (entity instanceof Mob || entity instanceof Player) {
			return (LivingEntity) entity;
		} else {
			return null;
		}
	}

	@Override
	public void travelRidden(Player player, Vec3 movement) {
		if (this.canMove()) {
			super.travelRidden(player, movement);
		} else {
			this.travel(movement);
		}
	}

	@Override
	protected void tickRidden(Player player, Vec3 input) {
		super.tickRidden(player, input);
		this.setYRot(player.getYRot());
		this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
		if (this.isControlledByLocalInstance()) {
			if (this.onGround()) {
				if (this.playerJumpPendingScale > 0.0F && this.isIdleState(this.getState())) {
					this.executeRidersJump(this.playerJumpPendingScale, input);
				}

				this.playerJumpPendingScale = 0.0F;
			}
		}
	}

	protected void executeRidersJump(float jumpScale, Vec3 input) {
		/*
		this.setState(GrazerState.BOUNCING);
		this.bounceHeight = 0.8D * jumpScale;
		*/
		CavernsAndChasms.CHANNEL.sendToServer(new C2SGrazerJumpMessage(0.8D * jumpScale));
	}

	@Override
	protected Vec3 getRiddenInput(Player player, Vec3 movement) {
		float x = player.xxa * 0.5F;
		float z = player.zza;
		if (z <= 0.0F) {
			z *= 0.25F;
		}

		return new Vec3(x, 0.0D, z);
	}

	@Override
	protected float getRiddenSpeed(Player player) {
		return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
	}

	@Override
	public void onPlayerJump(int charge) {
		if (charge < 0) {
			charge = 0;
		}

		if (charge >= 90) {
			this.playerJumpPendingScale = 1.0F;
		} else {
			this.playerJumpPendingScale = 0.4F + 0.4F * (float) charge / 90.0F;
		}
	}

	@Override
	public boolean canJump() {
		return true;
	}

	@Override
	public void handleStartJump(int p_21695_) {

	}

	@Override
	public void handleStopJump() {

	}
}