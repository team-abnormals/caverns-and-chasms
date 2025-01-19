package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AtonementTableEnchantmentNames {
	private static final ResourceLocation MIMIC_FONT = new ResourceLocation(CavernsAndChasms.MOD_ID, "mimic");
	private static final Style ROOT_STYLE = Style.EMPTY.withFont(MIMIC_FONT);
	private static final AtonementTableEnchantmentNames INSTANCE = new AtonementTableEnchantmentNames();
	private final RandomSource random = RandomSource.create();
	private final String[] words = new String[]{"five", "f\u014fr", "ri\u01e5t", "left", "kaverns", "kasms", "mime", "mimik", "luKi", "fortnite", "abnormal", "spinel", "p\u00e9per", "moja\u014bk", "xaK", "\u0161oDi", "\u010dimp", "te\u014bgwar", "goLum", "sirkus", "mabi", "diGiti", "dog", "stupid", "\u010du\u014bgus", "baj\u00f3kie", "w\u014f", "h\u014fse", "walter", "jeSe", "duK", "monoli\u0167", "\u0167ra\u0161er", "suMer", "winter", "spri\u014b", "\u0103tumn", "nor\u0167", "s\u014f\u0167", "\u00e8st", "west", "troLje", "eGlort", "xolesome", "sadkat", "bejeweled", "epilogue", "giga\u010dad", "fren\u010d", "\u010deS", "lazer", "koPer", "golem", "t\u00f3lboks", "silver", "sa\u014bg\u00fcne", "nekromium", "tin", "brazier", "ore", "piKakse", "te\u0167er", "rewind", "zirkonia", "\u00ebnst\u00ebn", "\u01b2ortig\u0103nt", "gman", "ridikul\u014fs", "ties", "miksel", "toDler", "baL", "me\u0167od", "paDi\u014bton", "dinos\u0103r", "ni\u01e5t", "d\u00e4", "sun", "m\u00f3n", "po\u0161", "haMer", "elitra", "miRor", "skibidi", "t\u00f6let", "riZ", "sigma", "morshu", "b\u00e8tboks", "\u01b2aL\u00eb", "kave", "klimb", "li\u01e5t", "dark", "frajile", "ansient", "siti", "warden", "en\u010dant", "atone", "damaje", "durabiliti", "w\u00e8pon", "t\u00f3l", "armor", "item", "power", "speL", "foL\u014f", "r\u00e8d", "kwikli", "sl\u014fli", "potion", "majik", "magikal", "br\u00e8k", "eksperiense", "lapis", "stone", "onse", "twise", "\u0167rise", "tri", "s\u00e9", "paje", "kover", "step", "kn\u014f", "alw\u00e4s", "never", "bo\u0167", "onli", "wriTen", "\u01b2\u00f6d", "piCa", "yo\u0161i", "yaPi\u014b", "\u017e\u00f3\u0161", "puZle", "s\u014fl", "mind", "\u01b2\u00f6ni\u010d", "e\u0167er", "gunje", "lunar", "solar", "bedroK", "java", "kaos", "dar\u0167", "\u01b2ader", "grum", "bunguloj", "intrinsik", "hiDen", "enerji", "ar\u01e5", "y\u00f6\u014bk", "unstable", "y\u00e9ld", "ya\ua741t", "lo\ua741", "understandi\u014b", "gost", "abstrakt", "konkrete", "\u0161r\u014fd", "lejendari", "\u0167ose", "dr\u00e8m", "awake", "herobrine", "s\u00e4", "pint", "bloke", "briqi\u0161", "\u00f6", "bruv", "kepler", "\u010dase", "\u010danje", "br\u00e8ki\u014b", "bad", "g\u00f3d", "mantle", "kosmos", "merasmus", "abraka", "dabra", "hokus", "pokus", "alakazam", "epik", "bakon", "for", "g\u00fcs", "yo\u01e5", "gr\u00e8t", "teRible", "unikorn"};

	private AtonementTableEnchantmentNames() {
	}

	public static AtonementTableEnchantmentNames getInstance() {
		return INSTANCE;
	}

	public FormattedText getRandomName(Font font, int p_98739_) {
		StringBuilder stringbuilder = new StringBuilder();
		int i = this.random.nextInt(2) + 3;

		for(int j = 0; j < i; ++j) {
			if (j != 0) {
				stringbuilder.append(" ");
			}

			stringbuilder.append(Util.getRandom(this.words, this.random));
		}

		return font.getSplitter().headByWidth(Component.literal(stringbuilder.toString()).withStyle(ROOT_STYLE), p_98739_, Style.EMPTY);
	}

	public void initSeed(long seed) {
		this.random.setSeed(seed);
	}
}