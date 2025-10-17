package com.teamabnormals.caverns_and_chasms.core.interfaces;

import com.teamabnormals.caverns_and_chasms.common.entity.animal.Rat;
import com.teamabnormals.caverns_and_chasms.core.registry.CCEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraftforge.registries.ForgeRegistries;

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
		private final float firstPersonPos;
		private final float attackDamage;
		private int biteTimer;
		private int animOffset;
		private long attachTime;

		public AttachedRat(CompoundTag entityData, float angle, float posY, float firstPersonPos) {
			this.entityData = entityData;
			this.angle = angle;
			this.posY = posY;
			this.firstPersonPos = firstPersonPos;
			this.attackDamage = (float) getAttributeValue(entityData, Attributes.ATTACK_DAMAGE);
		}

		public void initialize(LivingEntity host) {
			this.biteTimer = host.getRandom().nextInt(30);
			this.animOffset = host.getRandom().nextInt(100);
			this.attachTime = host.level().getGameTime();
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

		public float getFirstPersonPos() {
			return this.firstPersonPos;
		}

		public float getAttackDamage() {
			return this.attackDamage;
		}

		public long getAttachTime() {
			return this.attachTime;
		}

		public int getBiteTimer() {
			return this.biteTimer;
		}

		public void setBiteTimer(int time) {
			this.biteTimer = time;
		}

		public int getAnimOffset() {
			return this.animOffset;
		}

		public CompoundTag save() {
			CompoundTag compoundtag = new CompoundTag();
			compoundtag.put("EntityData", this.entityData);
			compoundtag.putFloat("Angle", this.angle);
			compoundtag.putFloat("PosY", this.posY);
			compoundtag.putFloat("FirstPersonPos", this.firstPersonPos);
			return compoundtag;
		}

		public static AttachedRat load(CompoundTag compoundtag) {
			return new AttachedRat((CompoundTag) compoundtag.get("EntityData"), compoundtag.getFloat("Angle"), compoundtag.getFloat("PosY"), compoundtag.getFloat("FirstPersonPos"));
		}

		public static double getAttributeValue(CompoundTag entityData, Attribute attribute) {
			if (entityData.contains("Attributes", 9)) {
				ListTag attributes = entityData.getList("Attributes", 10);

				for (int i = 0; i < attributes.size(); ++i) {
					CompoundTag attributetag = attributes.getCompound(i);
					if (attributetag.getString("Name").equals(ForgeRegistries.ATTRIBUTES.getKey(attribute).toString())) {
						double basevalue = attributetag.getDouble("Base");
						double addition = 0.0D;
						double multiplybase = 0.0D;
						double multiplytotal = 1.0D;

						if (attributetag.contains("Modifiers", 9)) {
							ListTag modifiers = attributetag.getList("Modifiers", 10);

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

						if (attribute instanceof RangedAttribute rangedattribute)
							return (Double.isNaN(value) ? rangedattribute.getMinValue() : Mth.clamp(value, rangedattribute.getMinValue(), rangedattribute.getMaxValue()));
						else
							return value;
					}
				}
			}

			return DefaultAttributes.getSupplier(CCEntityTypes.RAT.get()).getBaseValue(attribute);
		}
	}
}