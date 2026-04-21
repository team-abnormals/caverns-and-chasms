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
import org.apache.commons.lang3.ArrayUtils;

public class AtoningTableEnchantmentNames {
	private static final ResourceLocation KOUKERI_FONT = CavernsAndChasms.location("koukeri");
	private static final Style ROOT_STYLE = Style.EMPTY.withFont(KOUKERI_FONT);
	public static final char[] LETTERS = new char[]{
			'i', 'u', 'ï', 'ŭ', '⋅',
			'e', 'ə', 'o', 'ó', 'ë', 'ö', 'ŏ',
			'a', 'ʌ', 'á', 'ä', 'ă',
			'm', 'n', 'ŋ',
			'p', 'b', 't', 'd', 'k', 'g', 'ʔ',
			'ʦ', 'ʣ', 'ʧ', 'ʤ',
			'f', 'v', 'θ', 'ð', 's', 'z', 'ʃ', 'ʒ', 'x', 'ɣ', 'h',
			'l', 'r', 'j', 'W', 'w'
	};

	private static final AtoningTableEnchantmentNames INSTANCE = new AtoningTableEnchantmentNames();

	private final RandomSource random = RandomSource.create();
	private final String[] words = new String[]{"fäv", "mäm", "mimik", "ətŏn", "maʤik", "djurəbilətï", "fərbidən", "ov", "ðï", "dárknəs", "lät", "jŭs", "ënʃənt", "spinəl", "spirit", "ikspirïəns", "tŏm", "goləm", "θót", "hŏkəs⋅pŏkəs", "aləkəzam", "mabï", "instrʌkʃəns", "ʤem⋅stŏn", "kŏl", "sʌʧ", "ʧʌŋgəs", "pʌzliŋ", "ïθər", "ïon", "sʌbstəns", "əbskjurd", "dimenʃən", "bïiŋ", "läf", "deθ", "kʌnʤər", "ʧárm", "bəʤŭkï", "digətï", "dog", "kərs", "zərkŏnïə", "sərkəs", "păər", "árkën", "lätniŋ", "Wizdəm", "Wizərd", "spel⋅buk", "slab⋅fiʃ", "sïkrit", "Wïkən", "streŋθən", "grëzər", "levitët", "θnïd", "astrəl", "homʌŋkjuləs", "jŭnikorn", "órəkəl", "pŏtənt", "bland", "epik", "sʌn", "mŭn", "ʃïld", "viʒən", "gwonəm", "morʃŭ", "hárkinïən", "oŋkləd", "gandálf", "frŏdŏ", "bilbŏ", "sárumán", "săron", "ʃámən", "fərst", "sekənd", "θərd", "ógment", "abnorməl", "orb", "gʌnʤ", "and", "bikəz", "ðat", "hav", "Wiðin", "bï", "əplä", "tŭ", "or", "kwesʧən", "ansər", "ridəl", "mastər", "stroŋ⋅hŏld", "Wərdz", "transend", "laŋgwiʤ", "băndərï", "paθ", "mirər", "tranzmjŭt", "stŏn", "ólwëz", "nevər", "neðər", "end", "ŏvər⋅Wərld", "ʌndər⋅Wərld", "Wərld", "disk", "riŋ", "skwer", "sfiər", "planit", "səlestïəl", "hevənz", "ätəm", "ármər", "sord", "forʤ", "imposibəl", "prətekʃən", "tŭl", "buk", "spel", "këv", "dragən", "vestiʤ", "dʌnʤən", "pərʧans", "infinit", "täm", "spës", "fäər", "Wótər", "ərθ", "eər", "dimäz", "destinï", "forʧŭn", "gŏld", "silvər", "pŏst", "mortəm", "piʦə", "skelətən", "mistikəl", "noliʤ", "drŭid", "ʤávə", "pëʤ", "fänd", "árkëik", "sŏl", "əfekt", "hárt", "ilŭʒən", "vël", "nëʧər", "män", "kraft", "Wiʃï⋅Woʃï", "ad", "rimŭv", "split", "pondər", "bikóz", "talisman", "imbjŭ", "wävərn", "säklops", "monəliθ", "task", "eksalibər", "mjolnir", "rŭnik", "iliʤər", "ʌndərstand", "nʌʔʌ", "damiʤ", "brëk"};

	public static AtoningTableEnchantmentNames getInstance() {
		return INSTANCE;
	}

	public FormattedText getRandomName(Font font, int p_98739_) {
		StringBuilder stringbuilder = new StringBuilder();
		int i = this.random.nextInt(2) + 3;

		for (int j = 0; j < i; ++j) {
			if (j != 0) {
				stringbuilder.append(" ");
			}

			stringbuilder.append(Util.getRandom(this.words, this.random));
		}

		return font.getSplitter().headByWidth(Component.literal(stringbuilder.toString()).withStyle(ROOT_STYLE), p_98739_, Style.EMPTY);
	}

	public int[] getRandomNameAsLetterIds(RandomSource random) {
		String name = Util.getRandom(this.words, random);
		int[] array = new int[name.length()];
		for (int i = 0; i < name.length(); ++i) {
			array[i] = ArrayUtils.indexOf(LETTERS, name.charAt(i));
		}
		return array;
	}

	public void initSeed(long seed) {
		this.random.setSeed(seed);
	}
}