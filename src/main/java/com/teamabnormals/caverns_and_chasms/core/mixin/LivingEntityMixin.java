package com.teamabnormals.caverns_and_chasms.core.mixin;

import com.google.common.collect.Lists;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager.DataEntry;
import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.interfaces.RatHolder;
import com.teamabnormals.caverns_and_chasms.core.other.CCDataProcessors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements RatHolder {

	@Shadow public abstract boolean isSleeping();

	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	public List<AttachedRat> getAttachedRats() {
		return ((IDataManager) this).getValue(CCDataProcessors.ATTACHED_RATS);
	}

	private void syncAttachedRats() {
		((IDataManager) this).getDataMap().computeIfAbsent(CCDataProcessors.ATTACHED_RATS, DataEntry::new).markDirty();
	}

	@Override
	public void attachRat(Rat rat) {
		CompoundTag compound = new CompoundTag();
		compound.putString("id", rat.getEncodeId());
		rat.saveWithoutId(compound);

		List<AttachedRat> attachedrats = this.getAttachedRats();

		List<Integer> availableslots = Lists.newArrayList(-1, 0, 1);
		for (AttachedRat attachedrat : attachedrats)
			availableslots.remove(Integer.valueOf(Math.round(attachedrat.getFirstPersonPos())));
		float firstpersonpos = (availableslots.isEmpty() ? this.random.nextInt(3) - 1 : availableslots.get(this.random.nextInt(availableslots.size()))) + (this.random.nextFloat() - 0.5F) * 0.8F;

		AttachedRat attachedrat = new AttachedRat(compound, this.random.nextFloat() * 360.0F, (0.25F + this.random.nextFloat() * Math.max(this.getEyeHeight() - 0.5F, 0.0F)) / this.getBbHeight(), firstpersonpos);
		attachedrat.initialize((LivingEntity) (Object) this);

		attachedrats.add(attachedrat);
		this.syncAttachedRats();

		rat.discard();
	}

	@Override
	public Rat detachRat(AttachedRat attachedRat) {
		this.getAttachedRats().remove(attachedRat);
		this.syncAttachedRats();
		CompoundTag compound = attachedRat.getEntityData();
		if (!compound.isEmpty()) {
			Entity entity = EntityType.create(compound, this.level()).orElse(null);
			if (entity instanceof Rat removedrat) {
				removedrat.setPos(this.getX(), this.getY() + 0.7D, this.getZ());
				((ServerLevel) this.level()).addWithUUID(removedrat);
				return removedrat;
			}
		}
		return null;
	}

	@Override
	public List<Rat> detachAllRats() {
		List<Rat> removedrats = Lists.newArrayList();
		for (AttachedRat attachedrat : this.getAttachedRats()) {
			CompoundTag compound = attachedrat.getEntityData();
			if (!compound.isEmpty()) {
				Entity entity = EntityType.create(compound, this.level()).orElse(null);
				if (entity instanceof Rat rat) {
					rat.setPos(this.getX(), this.getY() + 0.7D, this.getZ());
					((ServerLevel) this.level()).addWithUUID(rat);
					removedrats.add(rat);
				}
			}
		}
		this.getAttachedRats().clear();
		this.syncAttachedRats();
		return removedrats;
	}

	@Override
	public void tickRats() {
		LivingEntity livingentity = (LivingEntity) (Object) this;
		List<AttachedRat> attachedrats = this.getAttachedRats();
		AttachedRat[] ratstotick = this.getAttachedRats().toArray(new AttachedRat[attachedrats.size()]);

		if (!livingentity.level().isClientSide) {
			boolean trydetachingrats = livingentity instanceof Player && (this.fallDistance > 0.5F || this.isInWater() || ((Player) livingentity).getAbilities().flying || this.isInPowderSnow);

			for (AttachedRat attachedrat : ratstotick) {
				if (trydetachingrats && attachedrat.getAttachTime() + 20L < livingentity.level().getGameTime()) {
					Rat rat = this.detachRat(attachedrat);
					rat.setTarget(livingentity);
				}

				attachedrat.setBiteTimer(attachedrat.getBiteTimer() - 1);
				if (attachedrat.getBiteTimer() <= 0) {
					attachedrat.setBiteTimer(20 + livingentity.getRandom().nextInt(10));
					livingentity.hurt(livingentity.level().damageSources().generic(), attachedrat.getAttackDamage());
				}
			}
		}
	}

	@Override
	public int getMaxRats() {
		return this.getBbHeight() >= 0.8F ? (int) (3.5F * this.getBbWidth() * this.getBbHeight()) : 0;
	}

	@Override
	public boolean canHoldMoreRats() {
		return !this.isInWater() && this.getAttachedRats().size() < this.getMaxRats();
	}
}