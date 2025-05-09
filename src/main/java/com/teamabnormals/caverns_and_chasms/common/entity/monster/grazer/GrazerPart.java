package com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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

public class GrazerPart extends PartEntity<Grazer> {
	private final boolean shell;
	private final EntityDimensions size;
	private final double halfSize;
	private final double zOffset;
	private final double yOffset;

	public GrazerPart(Grazer parent, boolean shell, float size, double zOffset, double yOffset) {
		super(parent);
		this.shell = shell;
		float f = size / 16F;
		this.size = EntityDimensions.scalable(f, f);
		this.halfSize = f * 0.5D;
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

	public boolean isShell() {
		return this.shell;
	}

	public void updatePosition() {
		Grazer grazer = this.getParent();

		Vec3 oldpos = calculatePosition(grazer.xOld, grazer.yOld, grazer.zOld, grazer.getRollAnim(0.0F), grazer.yRotO);
		this.xo = oldpos.x;
		this.yo = oldpos.y;
		this.zo = oldpos.z;
		this.xOld = oldpos.x;
		this.yOld = oldpos.y;
		this.zOld = oldpos.z;

		Vec3 newpos = calculatePosition(grazer.getX(), grazer.getY(), grazer.getZ(), grazer.getRollAngle(), grazer.getYRot());
		this.setPos(newpos.x, newpos.y, newpos.z);
	}

	public void pushEntities() {
		Grazer grazer = this.getParent();

		if (this.level().isClientSide()) {
			this.level().getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(), EntitySelector.pushableBy(grazer)).forEach(grazer::push);
		} else {
			List<Entity> list = this.level().getEntities(grazer, this.getBoundingBox(), EntitySelector.pushableBy(grazer));
			if (!list.isEmpty())
				for (Entity entity : list)
					grazer.push(entity);
		}
	}

	private Vec3 calculatePosition(double x, double y, double z, float xRot, float yRot) {
		float f = xRot * Mth.DEG_TO_RAD;
		float f1 = yRot * Mth.DEG_TO_RAD;
		Vec3 vec3 = new Vec3(0.0D, this.yOffset, this.zOffset).xRot(f).yRot(-f1);
		Vec3 vec31 = new Vec3(0.0D, this.getParent().shellCenterY() - this.halfSize, this.getParent().shellCenterZ()).yRot(-f1);
		return vec3.add(vec31).add(x, y, z);
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
	public boolean hurt(DamageSource source, float amount) {
		if (this.shell) {
			Entity attacker = source.getDirectEntity();
			if (attacker != null) {
				Grazer grazer = this.getParent();
				AABB aabb = this.getBoundingBox().inflate(0.3D);
				Vec3 attackerpos = attacker.getEyePosition();
				Vec3 partpos = new Vec3(this.getX(), this.getY(0.5D), this.getZ());

				Vec3 location = aabb.clip(attackerpos, attackerpos.add(attacker.getViewVector(1.0F).scale(partpos.subtract(attackerpos).length() + this.halfSize + 0.3D))).or(() -> aabb.clip(attackerpos, partpos)).orElse(partpos);
				Vec3 normal = grazer.calculateDeflectionNormal(location);

				float pitch = 0.8F;
				this.level().playSound(null, location.x, location.y, location.z, CCSoundEvents.TIN_DEFLECT.get(), SoundSource.BLOCKS, Math.min(0.2F + pitch * 0.7F, 1.0F), Math.min(0.5F + pitch * 0.8F, 1.8F));

				for (int i = 0; i < 3; ++i) {
					double d1 = normal.x * 0.1D + this.random.nextGaussian() * 0.05D;
					double d2 = normal.y * 0.1D + this.random.nextGaussian() * 0.05D;
					double d3 = normal.z * 0.1D + this.random.nextGaussian() * 0.05D;
					this.level().addParticle(CCParticleTypes.SPARK.get(), location.x, location.y, location.z, d1, d2, d3);
				}
			}
			return false;
		} else {
			return !this.isInvulnerableTo(source) && this.getParent().hurt(source, amount);
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.size;
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
