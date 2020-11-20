package com.minecraftabnormals.caverns_and_chasms.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class MimeEntity extends MonsterEntity {
	public double prevChasingPosX;
	public double prevChasingPosY;
	public double prevChasingPosZ;
	public double chasingPosX;
	public double chasingPosY;
	public double chasingPosZ;
	public float prevCameraYaw;
	public float cameraYaw;

	public MimeEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}

	private void updateCape() {
		this.prevChasingPosX = this.chasingPosX;
		this.prevChasingPosY = this.chasingPosY;
		this.prevChasingPosZ = this.chasingPosZ;
		double d0 = this.getPosX() - this.chasingPosX;
		double d1 = this.getPosY() - this.chasingPosY;
		double d2 = this.getPosZ() - this.chasingPosZ;
		if (d0 > 10.0D) {
			this.chasingPosX = this.getPosX();
//            this.prevChasingPosX = this.chasingPosX;
		}

		if (d2 > 10.0D) {
			this.chasingPosZ = this.getPosZ();
//            this.prevChasingPosZ = this.chasingPosZ;
		}

		if (d1 > 10.0D) {
			this.chasingPosY = this.getPosY();
//            this.prevChasingPosY = this.chasingPosY;
		}

		if (d0 < -10.0D) {
			this.chasingPosX = this.getPosX();
//            this.prevChasingPosX = this.chasingPosX;
		}

		if (d2 < -10.0D) {
			this.chasingPosZ = this.getPosZ();
//            this.prevChasingPosZ = this.chasingPosZ;
		}

		if (d1 < -10.0D) {
			this.chasingPosY = this.getPosY();
//            this.prevChasingPosY = this.chasingPosY;
		}

		this.chasingPosX += d0 * 0.25D;
		this.chasingPosZ += d2 * 0.25D;
		this.chasingPosY += d1 * 0.25D;
	}

	@Override
	public void tick() {
		super.tick();
		this.updateCape();
	}
}
