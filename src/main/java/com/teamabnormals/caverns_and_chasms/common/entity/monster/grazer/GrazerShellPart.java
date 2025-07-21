package com.teamabnormals.caverns_and_chasms.common.entity.monster.grazer;

import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GrazerShellPart extends GrazerPart {
	public GrazerShellPart(Grazer parent, float size, double zOffset, double yOffset) {
		super(parent, size, zOffset, yOffset);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		Grazer grazer = this.getParent();
		Entity directentity = source.getDirectEntity();
		if (directentity != null) {
			AABB aabb = this.getBoundingBox().inflate(0.3D);
			Vec3 attackerpos = directentity.getEyePosition();
			Vec3 partpos = new Vec3(this.getX(), this.getY(0.5D), this.getZ());

			Vec3 location = aabb.clip(attackerpos, attackerpos.add(directentity.getViewVector(1.0F).scale(partpos.subtract(attackerpos).length() + this.getDimensions(Pose.STANDING).height * 0.5D + 0.3D))).or(() -> aabb.clip(attackerpos, partpos)).orElse(partpos);
			Vec3 normal = grazer.calculateDeflectionNormal(location);

			float pitch = 0.8F;
			this.level().playSound(null, location.x, location.y, location.z, CCSoundEvents.TIN_DEFLECT.get(), SoundSource.BLOCKS, Math.min(0.2F + pitch * 0.7F, 1.0F), Math.min(0.5F + pitch * 0.8F, 1.8F));

			for (int i = 0; i < 3; ++i) {
				double d1 = normal.x * 0.1D + this.random.nextGaussian() * 0.05D;
				double d2 = normal.y * 0.1D + this.random.nextGaussian() * 0.05D;
				double d3 = normal.z * 0.1D + this.random.nextGaussian() * 0.05D;
				this.level().addParticle(CCParticleTypes.SPARK.get(), location.x, location.y, location.z, d1, d2, d3);
			}
			return false;
		} else {
			return grazer.hurt(source, amount);
		}
	}
}