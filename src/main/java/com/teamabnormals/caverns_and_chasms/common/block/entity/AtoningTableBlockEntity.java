package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory.AtoningTableEnchantmentNames;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlockEntityTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCParticleTypes;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AtoningTableBlockEntity extends BlockEntity implements Nameable {
	public int time;
	public float flip;
	public float oFlip;
	public float flipT;
	public float flipA;
	public float open;
	public float oOpen;
	public float rot;
	public float oRot;
	public float tRot;
	private int[] word;
	private int letterIndex;
	private int letterAngle;
	private static final RandomSource RANDOM = RandomSource.create();
	private Component name;

	public AtoningTableBlockEntity(BlockPos pos, BlockState state) {
		super(CCBlockEntityTypes.ATONING_TABLE.get(), pos, state);
		this.word = AtoningTableEnchantmentNames.getInstance().getRandomNameAsLetterIds(RANDOM);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (this.hasCustomName()) {
			tag.putString("CustomName", Component.Serializer.toJson(this.name));
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("CustomName", 8)) {
			this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
		}
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, AtoningTableBlockEntity blockEntity) {
		if (level.getGameTime() % 60 == 0)
			level.addParticle(CCParticleTypes.ATONING_DAGGER.get(), pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);

		float enchPower = 0;
		for (BlockPos offset : EnchantmentTableBlock.BOOKSHELF_OFFSETS)
			if (EnchantmentTableBlock.isValidBookShelf(level, pos, offset))
				enchPower += level.getBlockState(pos.offset(offset)).getEnchantPowerBonus(level, pos.offset(offset));

		if (enchPower > 0) {
			int letters = Math.min((int) enchPower, 15) + 2;

			if (level.getGameTime() % (160 / letters) == 0) {
				double d0 = Math.PI * 2.0D * blockEntity.letterAngle / letters;
				level.addParticle(CCParticleTypes.ATONING_LETTER.get(), pos.getX() + 0.5D + Math.cos(d0) * 1.1D, pos.getY(), pos.getZ() + 0.5D + Math.sin(d0) * 1.1D, d0 - Math.PI / 2.0F, blockEntity.word[blockEntity.letterIndex], 0.0D);

				++blockEntity.letterAngle;
				if (blockEntity.letterAngle >= letters)
					blockEntity.letterAngle = 0;

				++blockEntity.letterIndex;
				if (blockEntity.letterIndex >= blockEntity.word.length) {
					blockEntity.letterIndex = 0;
					blockEntity.word = AtoningTableEnchantmentNames.getInstance().getRandomNameAsLetterIds(RANDOM);
				}
			}
		}

		if (level.getGameTime() % 240 == 0)
			level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, CCSoundEvents.ATONING_TABLE_WHISPERS.get(), SoundSource.BLOCKS, 0.25F, 1.0F, false);

		blockEntity.oOpen = blockEntity.open;
		blockEntity.oRot = blockEntity.rot;
		Player player = level.getNearestPlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 3.0D, false);
		if (player != null) {
			double d0 = player.getX() - ((double) pos.getX() + 0.5D);
			double d1 = player.getZ() - ((double) pos.getZ() + 0.5D);
			blockEntity.tRot = (float) Mth.atan2(d1, d0);
			blockEntity.open += 0.1F;
			if (blockEntity.open < 0.5F || RANDOM.nextInt(40) == 0) {
				float f1 = blockEntity.flipT;

				do {
					blockEntity.flipT += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
				} while (f1 == blockEntity.flipT);
			}
		} else {
			blockEntity.tRot += 0.02F;
			blockEntity.open -= 0.1F;
		}

		while (blockEntity.rot >= (float) Math.PI) {
			blockEntity.rot -= ((float) Math.PI * 2F);
		}

		while (blockEntity.rot < -(float) Math.PI) {
			blockEntity.rot += ((float) Math.PI * 2F);
		}

		while (blockEntity.tRot >= (float) Math.PI) {
			blockEntity.tRot -= ((float) Math.PI * 2F);
		}

		while (blockEntity.tRot < -(float) Math.PI) {
			blockEntity.tRot += ((float) Math.PI * 2F);
		}

		float f2;
		for (f2 = blockEntity.tRot - blockEntity.rot; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
		}

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		blockEntity.rot += f2 * 0.4F;
		blockEntity.open = Mth.clamp(blockEntity.open, 0.0F, 1.0F);
		++blockEntity.time;
		blockEntity.oFlip = blockEntity.flip;
		float f = (blockEntity.flipT - blockEntity.flip) * 0.4F;
		float f3 = 0.2F;
		f = Mth.clamp(f, -f3, f3);
		blockEntity.flipA += (f - blockEntity.flipA) * 0.9F;
		blockEntity.flip += blockEntity.flipA;
	}

	@Override
	public Component getName() {
		return this.name != null ? this.name : Component.translatable("container.caverns_and_chasms.atone");
	}

	public void setCustomName(@Nullable Component component) {
		this.name = component;
	}

	@Nullable
	@Override
	public Component getCustomName() {
		return this.name;
	}
}