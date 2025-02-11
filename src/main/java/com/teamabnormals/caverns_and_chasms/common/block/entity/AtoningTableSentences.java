package com.teamabnormals.caverns_and_chasms.common.block.entity;

import com.teamabnormals.caverns_and_chasms.client.particle.AtoningLetterParticle;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class AtoningTableSentences {
	public static final int[][] SENTENCES = {
			lettersToIntegers(new String[]{"o", "n", "e", "r", "i", "ng", "t", "o", "r", "u", "l", "e", "th", "e", "m", "a", "ll"}),
			lettersToIntegers(new String[]{"h", "i", "dd", "e", "n", "m", "e", "ss", "a", "g", "e"}),
			lettersToIntegers(new String[]{"th", "i", "s", "b", "i", "gg", "i", "t", "i", "b", "oy", "s", "a", "d", "i", "gg", "i", "t", "i", "d", "o", "g"}),
			lettersToIntegers(new String[]{"k", "a", "r", "e", "f", "u", "l", "w", "i", "th", "th", "e", "f", "l", "oa", "t", "i", "ng", "d", "a", "gg", "e", "r"}),
			lettersToIntegers(new String[]{"th", "i", "s", "p", "ea", "s", "e", "i", "s", "wh", "a", "t", "a", "ll", "t", "r", "ue", "w", "a", "rr", "i", "o", "r", "s", "s", "t", "r", "i", "v", "e", "f", "o", "r"})
	};

	private static int[] lettersToIntegers(String[] array) {
		int[] intArray = new int[array.length];
		for (int i = 0; i < array.length; ++i)
			intArray[i] = ArrayUtils.indexOf(AtoningLetterParticle.LETTERS, array[i]);
		return intArray;
	}

	public static int[] pickRandomSentence(RandomSource random) {
		return SENTENCES[random.nextInt(AtoningTableSentences.SENTENCES.length)];
	}
}