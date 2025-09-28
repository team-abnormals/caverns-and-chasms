package com.teamabnormals.caverns_and_chasms.common.entity.animal.grazer;

import com.teamabnormals.caverns_and_chasms.core.other.CCEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.List;

public class GrazerPart extends PartEntity<AbstractGrazer> {
	protected final EntityDimensions dimensions;
	protected final double zOffset;
	protected final double yOffset;

	public GrazerPart(AbstractGrazer parent, float size, double zOffset, double yOffset) {
		super(parent);
		float f = size / 16F;
		this.dimensions = EntityDimensions.scalable(f, f);
		this.zOffset = zOffset / 16D;
		this.yOffset = yOffset / 16D;
		this.refreshDimensions();
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}

	public void updatePosition() {
		AbstractGrazer grazer = this.getParent();

		Vec3 oldpos = this.calculatePosition(grazer.xOld, grazer.yOld, grazer.zOld, grazer.xRotO, grazer.yRotO);
		this.xo = oldpos.x;
		this.yo = oldpos.y;
		this.zo = oldpos.z;
		this.xOld = oldpos.x;
		this.yOld = oldpos.y;
		this.zOld = oldpos.z;

		Vec3 newpos = this.calculatePosition(grazer.getX(), grazer.getY(), grazer.getZ(), grazer.getXRot(), grazer.getYRot());
		this.setPos(newpos.x, newpos.y, newpos.z);
	}

	public void pushEntities() {
		AbstractGrazer grazer = this.getParent();

		if (this.level().isClientSide()) {
			this.level().getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(), EntitySelector.pushableBy(grazer)).forEach(grazer::push);
		} else {
			List<Entity> list = this.level().getEntities(grazer, this.getBoundingBox(), EntitySelector.pushableBy(grazer));
			if (!list.isEmpty())
				for (Entity entity : list)
					grazer.push(entity);
		}
	}

	@Override
	public void push(double x, double y, double z) {
		this.getParent().push(x, y, z);
	}

	private Vec3 calculatePosition(double x, double y, double z, float xRot, float yRot) {
		float f = xRot * Mth.DEG_TO_RAD;
		float f1 = yRot * Mth.DEG_TO_RAD;
		Vec3 vec3 = new Vec3(0.0D, this.getYOffset(), this.getZOffset()).scale(this.getScale()).xRot(-f).yRot(-f1);
		Vec3 vec31 = new Vec3(0.0D, this.getParent().shellCenterY(1.0F) - this.getDimensions(Pose.STANDING).height * 0.5D, this.getParent().shellCenterZ(1.0F)).yRot(-f1);
		return vec3.add(vec31).add(x, y, z);
	}

	protected double getYOffset() {
		return this.yOffset;
	}

	protected double getZOffset() {
		return this.zOffset;
	}

	public boolean deflectsAttacks() {
		return true;
	}

	@Override
	public final InteractionResult interact(Player player, InteractionHand hand) {
		return this.getParent().interact(player, hand);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		AbstractGrazer grazer = this.getParent();
		if (this.deflectsAttacks()) {
			Entity directentity = source.getDirectEntity();
			if (directentity != null) {
				AABB aabb = this.getBoundingBox().inflate(0.3D);
				Vec3 attackerpos = directentity.getEyePosition();
				Vec3 partpos = new Vec3(this.getX(), this.getY(0.5D), this.getZ());

				Vec3 location = aabb.clip(attackerpos, attackerpos.add(directentity.getViewVector(1.0F).scale(partpos.subtract(attackerpos).length() + this.getDimensions(Pose.STANDING).height * 0.5D + 0.3D))).or(() -> aabb.clip(attackerpos, partpos)).orElse(partpos);
				Vec3 normal = grazer.calculateDeflectionNormal(location);

				CCEvents.playTinDeflectEffects(this.level(), location, normal, 0.8F, this.random);

				return false;
			}
		}
		return grazer.hurt(source, amount);
	}


	@Override
	public boolean isPickable() {
		return true;
	}

	@Nullable
	@Override
	public ItemStack getPickResult() {
		return this.getParent().getPickResult();
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.dimensions.scale(this.getScale());
	}

	protected float getScale() {
		return this.getParent().getScale();
	}

	@Override
	public boolean shouldBeSaved() {
		return false;
	}

	@Override
	public boolean is(Entity entity) {
		return this == entity || this.getParent() == entity;
	}
}
