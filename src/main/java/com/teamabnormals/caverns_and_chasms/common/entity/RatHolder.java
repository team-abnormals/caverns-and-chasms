package com.teamabnormals.caverns_and_chasms.common.entity;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.List;

public interface RatHolder {
	List<AttachedRat> getAttachedRats();

	void attachRat(Rat rat);

	Rat detachRat(AttachedRat attachedrat);

	List<Rat> detachAllRats();

	void tickRats();

	int getMaxRats();

	boolean canHoldMoreRats();

	class AttachedRat {
		private final CompoundTag entityData;
		private final float angle;
		private final float posY;
		private final float attackDamage;
		private int biteTimer;

		public AttachedRat(CompoundTag entityData, float angle, float posY) {
			this.entityData = entityData;
			this.angle = angle;
			this.posY = posY;
			this.attackDamage = calculateAttackAttribute(entityData);
		}

		public void initialize(LivingEntity host) {
			this.biteTimer = host.getRandom().nextInt(30);
		}

		public void tick(LivingEntity host) {
			if (!host.level().isClientSide) {
				if (this.biteTimer-- <= 0) {
					this.biteTimer = 20 + host.getRandom().nextInt(10);
					host.hurt(host.level().damageSources().genericKill(), this.attackDamage);
				}
			}
		}

		public CompoundTag getEntityData() {
			return this.entityData;
		}

		public float getAngle() {
			return this.angle;
		}

		public float getPosY() {
			return this.posY;
		}

		public CompoundTag save() {
			CompoundTag compoundtag = new CompoundTag();
			compoundtag.put("EntityData", this.entityData);
			compoundtag.putFloat("Angle", this.angle);
			compoundtag.putFloat("PosY", this.posY);
			return compoundtag;
		}

		public static AttachedRat load(CompoundTag compoundtag) {
			return new AttachedRat((CompoundTag) compoundtag.get("EntityData"), compoundtag.getFloat("Angle"), compoundtag.getFloat("PosY"));
		}

		private static float calculateAttackAttribute(CompoundTag entityData) {
			if (entityData.contains("Attributes", 9)) {
				ListTag attributes = entityData.getList("Attributes", 10);

				for (int i = 0; i < attributes.size(); ++i) {
					CompoundTag attribute = attributes.getCompound(i);
					if (attribute.getString("Name").equals("generic.attack_damage")) {
						double basevalue = attribute.getDouble("Base");
						double addition = 0.0D;
						double multiplybase = 0.0D;
						double multiplytotal = 1.0D;

						if (attribute.contains("Modifiers", 9)) {
							ListTag modifiers = attribute.getList("Modifiers", 10);

							for (int j = 0; j < modifiers.size(); ++j) {
								CompoundTag modifier = modifiers.getCompound(j);
								AttributeModifier.Operation operation = AttributeModifier.Operation.fromValue(modifier.getInt("Operation"));
								switch (operation) {
									case ADDITION -> addition += modifier.getDouble("Amount");
									case MULTIPLY_BASE -> multiplybase += modifier.getDouble("Amount");
									case MULTIPLY_TOTAL -> multiplytotal *= (1.0D + modifier.getDouble("Amount"));
								}
							}
						}

						double value = basevalue + addition;
						value += value * multiplybase;
						value *= multiplytotal;

						RangedAttribute rangedattribute = (RangedAttribute) Attributes.ATTACK_DAMAGE;
						return (float) (Double.isNaN(value) ? rangedattribute.getMinValue() : Mth.clamp(value, rangedattribute.getMinValue(), rangedattribute.getMaxValue()));
					}
				}
			}

			return (float) DefaultAttributes.getSupplier(CCEntityTypes.RAT.get()).getBaseValue(Attributes.ATTACK_DAMAGE);
		}
	}
}