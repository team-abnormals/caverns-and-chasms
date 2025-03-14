package com.teamabnormals.caverns_and_chasms.common.block.entity;

import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.ArrayUtils;

public class AtoningTableSentences {
	public static final String[] LETTERS = {
			"a", "e", "i", "o", "u", "ee", "oo", "ea", "oa",
			"ay", "ey", "oy", "uy", "aw", "ew", "iw", "ow", "ae", "ie", "oe", "ue", "ao", "eo", "io", "uo",
			"ts", "tts", "dz", "ddz", "ch", "cch", "j", "jj", "f", "ff", "h", "hh", "k", "kk", "g", "gg",
			"kh", "kkh", "gh", "ggh", "l", "ll", "m", "mm", "n", "nn", "ng", "nng", "p", "pp", "b", "bb",
			"r", "rr", "s", "ss", "z", "zz", "sh", "ssh", "zh", "zzh", "t", "tt", "d", "dd", "th", "tth",
			"dh", "ddh", "v", "vv", "big_v", "wh", "wwh", "w", "ww", "y", "yy", "'", "''"
	};

	public static final int[][] SENTENCES = {
			lettersToIntegers(new String[]{"o", "n", "e", "r", "i", "ng", "t", "o", "r", "u", "l", "e", "th", "e", "m", "a", "ll"}),
			lettersToIntegers(new String[]{"h", "i", "dd", "e", "n", "m", "e", "ss", "a", "g", "e"}),
			lettersToIntegers(new String[]{"th", "i", "s", "b", "i", "gg", "i", "t", "i", "b", "oy", "s", "a", "d", "i", "gg", "i", "t", "i", "d", "o", "g"}),
			lettersToIntegers(new String[]{"k", "a", "r", "e", "f", "u", "l", "w", "i", "th", "th", "e", "f", "l", "oa", "t", "i", "ng", "d", "a", "gg", "e", "r"}),
			lettersToIntegers(new String[]{"th", "i", "s", "p", "ea", "s", "e", "i", "s", "wh", "a", "t", "a", "ll", "t", "r", "ue", "w", "a", "rr", "i", "o", "r", "s", "s", "t", "r", "i", "v", "e", "f", "o", "r"}),
			lettersToIntegers(new String[]{"s", "o", "m", "e", "th", "i", "ng", "s", "e", "k", "r", "e", "t", "s", "t", "ee", "r", "s", "u", "s", "b", "o", "th", "w", "e", "sh", "a", "ll", "n", "o", "t", "n", "a", "m", "e", "i", "t"}),
			lettersToIntegers(new String[]{"b", "i", "g", "ch", "u", "ng", "g", "u", "s", "oo", "h", "n", "a", "n", "a"}),
			lettersToIntegers(new String[]{"ch", "a", "n", "t", "ch", "a", "n", "t", "ch", "a", "n", "t"})
	};

	private static int[] lettersToIntegers(String[] array) {
		int[] intArray = new int[array.length];
		for (int i = 0; i < array.length; ++i)
			intArray[i] = ArrayUtils.indexOf(LETTERS, array[i]);
		return intArray;
	}

	public static int[] pickRandomSentence(RandomSource random) {
		return SENTENCES[random.nextInt(SENTENCES.length)];
	}
}