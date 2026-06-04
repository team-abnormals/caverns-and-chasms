package com.teamabnormals.caverns_and_chasms.client.gui.screens.inventory;

import com.teamabnormals.caverns_and_chasms.core.CavernsAndChasms;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.ArrayUtils;

public class AtoningTableEnchantmentNames {
	private static final ResourceLocation KOUKERI_FONT = CavernsAndChasms.location("koukeri");
	private static final Style ROOT_STYLE = Style.EMPTY.withFont(KOUKERI_FONT);
	public static final char[] LETTERS = new char[]{
			'i',      'u', 'í', 'ú', 'ï',      'ŭ',
			'e', 'ə', 'o', 'é', 'ó', 'ë', 'ö', 'ŏ',
			'a', 'ʌ',           'á', 'ä',      'ă',
			'm', 'n',      'ŋ',
			'p', 't', 'ʦ', 'ʧ', 'k', 'ʔ', '⋅',
			'b', 'd', 'ʣ', 'ʤ', 'g',
			'f', 'θ', 's', 'ʃ', 'x', 'h',
			'v', 'ð', 'z', 'ʒ', 'ɣ',
			'W', 'l',      'r', 'j',
			'w'
	};

	private static final AtoningTableEnchantmentNames INSTANCE = new AtoningTableEnchantmentNames();

	private final RandomSource random = RandomSource.create();
	private final String[] words = new String[]{"fäv", "mäm", "mimik", "ətŏn", "maʤik", "djúrəbilətï", "fərbidən", "ov", "ðə", "dárknəs", "lät", "jŭs", "ënʃənt", "spinəl", "spirit", "ikspírïəns", "tŏm", "goləm", "θót", "hŏkəs⋅pŏkəs", "aləkəzam", "mabï", "instrʌkʃəns", "ʤem⋅stŏn", "kŏl", "sʌʧ", "ʧʌŋgəs", "pʌzliŋ", "ïθər", "ïon", "sʌbstəns", "əbskjúrd", "dimenʃən", "bïiŋ", "läf", "deθ", "kʌnʤər", "ʧárm", "bəʤŭkï", "digətï", "dog", "kərs", "zərkŏnïə", "sərkəs", "păər", "árkën", "lätniŋ", "Wizdəm", "Wizərd", "spel⋅buk", "slab⋅fiʃ", "sïkrit", "Wïkən", "streŋθən", "grëzər", "levitët", "θnïd", "astrəl", "homʌŋkjələs", "jŭnikórn", "orəkəl", "pŏtənt", "bland", "epik", "sʌn", "mŭn", "ʃïld", "viʒən", "gwonəm", "mórʃŭ", "hárkinïən", "oŋkləd", "gandálf", "frŏdŏ", "bilbŏ", "sárumán", "săron", "ʃámən", "fərst", "sekənd", "θərd", "ógment", "abnórməl", "órb", "gʌnʤ", "and", "bikəz", "ðat", "hav", "Wiðin", "bï", "əplä", "tŭ", "ór", "kwesʧən", "ansər", "ridəl", "mastər", "stroŋ⋅hŏld", "Wərdz", "transend", "laŋgwiʤ", "băndərï", "paθ", "mirər", "tranzmjŭt", "stŏn", "ólwëz", "nevər", "neðər", "end", "ŏvər⋅Wərld", "ʌndər⋅Wərld", "Wərld", "disk", "riŋ", "skwér", "sfír", "planit", "səlestïəl", "hevənz", "ätəm", "ármər", "sórd", "fórʤ", "imposibəl", "prətekʃən", "tŭl", "buk", "spel", "këv", "dragən", "vestiʤ", "dʌnʤən", "pərʧans", "infinit", "täm", "spës", "fäər", "Wótər", "ərθ", "ér", "dimäz", "destinï", "fórʧŭn", "gŏld", "silvər", "pŏst", "mórtəm", "piʦə", "skelətən", "mistikəl", "noliʤ", "drŭid", "ʤávə", "pëʤ", "fänd", "árkëik", "sŏl", "əfekt", "hárt", "ilŭʒən", "vël", "nëʧər", "män", "kraft", "Wiʃï⋅Woʃï", "ad", "rimŭv", "split", "pondər", "bikóz", "talisman", "imbjŭ", "wävərn", "säklops", "monəliθ", "task", "ekskalibər", "mjolnír", "rŭnik", "iliʤər", "ʌndərstand", "nʌʔʌ", "damiʤ", "brëk", "ŏkï⋅dŏkï", "kasəl", "hjŭmanitï", "träaŋgəl", "ʧaptər", "hwispər", "hwimzikəl", "aʣ", "tərkwöz", "ïʤis", "hŏmər", "márʤ", "bárt", "lïsə", "magï", "padiŋtən", "lŭnər", "sŏlər", "hʌndrid", "nekrənomikon", "grimwá", "eləment", "säəns", "biʤŭəld"};

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