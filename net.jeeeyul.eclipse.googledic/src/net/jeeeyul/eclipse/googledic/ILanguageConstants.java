package net.jeeeyul.eclipse.googledic;

import java.util.Arrays;
import java.util.List;

public interface ILanguageConstants {
	public static final String AUTO_DETECT = "";
	public static final String ARABIC = "ar";
	public static final String BULGARIAN = "bg";
	public static final String CATALAN = "ca";
	public static final String CHINESE = "zh";
	public static final String CHINESE_SIMPLIFIED = "zh-CN";
	public static final String CHINESE_TRADITIONAL = "zh-TW";
	public static final String CROATIAN = "hr";
	public static final String CZECH = "cs";
	public static final String DANISH = "da";
	public static final String DUTCH = "nl";
	public static final String ENGLISH = "en";
	public static final String FILIPINO = "tl";
	public static final String FINNISH = "fi";
	public static final String FRENCH = "fr";
	public static final String GALACIAN = "gl";
	public static final String GERMAN = "de";
	public static final String GREEK = "el";
	public static final String HEBREW = "iw";
	public static final String HINDI = "hi";
	public static final String HUNGARIAN = "hu";
	public static final String INDONESIAN = "id";
	public static final String ITALIAN = "it";
	public static final String JAPANESE = "ja";
	public static final String KOREAN = "ko";
	public static final String LATVIAN = "lv";
	public static final String LITHUANIAN = "lt";
	public static final String MALTESE = "mt";
	public static final String NORWEGIAN = "no";
	public static final String POLISH = "pl";
	public static final String PORTUGESE = "pt";
	public static final String ROMANIAN = "ro";
	public static final String RUSSIAN = "ru";
	public static final String SERBIAN = "sr";
	public static final String SLOVAK = "sk";
	public static final String SLOVENIAN = "sl";
	public static final String SPANISH = "es";
	public static final String SWEDISH = "sv";
	public static final String THAI = "th";
	public static final String TURKISH = "tr";
	public static final String UKRANIAN = "uk";
	public static final String VIETNAMESE = "vi";

	public static final String[] LANGUAGE_CODES = new String[] { AUTO_DETECT, ARABIC, BULGARIAN, CATALAN, CHINESE,
			CHINESE_SIMPLIFIED, CHINESE_TRADITIONAL, CROATIAN, CZECH, DANISH, DUTCH, ENGLISH, FILIPINO, FINNISH,
			FRENCH, GALACIAN, GERMAN, GREEK, HEBREW, HINDI, HUNGARIAN, INDONESIAN, ITALIAN, JAPANESE, KOREAN, LATVIAN,
			LITHUANIAN, MALTESE, NORWEGIAN, POLISH, PORTUGESE, ROMANIAN, RUSSIAN, SERBIAN, SLOVAK, SLOVENIAN, SPANISH,
			SWEDISH, THAI, TURKISH, UKRANIAN, VIETNAMESE };

	public static final String[] LANGUAGE_DISPLAY_NAMES = { "Auto Detect", "Arabic", "Bulgarian", "Catalan", "Chinese",
			"Chinese(S)", "Chinese(T)", "Croatian", "Czech", "Danish", "Dutch", "English", "Filipino", "Finnish",
			"French", "Galacian", "German", "Greek", "Hebrew", "Hindi", "Hungarian", "Indonesian", "Italian",
			"Japanese", "Korean", "Latvian", "Lithuanian", "Maltese", "Norwegian", "Polish", "Portugese", "Romanian",
			"Russian", "Serbian", "Slovak", "Slovenian", "Spanish", "Swedish", "Thai", "Turkish", "Ukranian",
			"Vietnamese" };

	public static final List<String> LANGUAGE_CODE_LIST = Arrays.asList(LANGUAGE_CODES);
	public static final List<String> LANGUAGE_DISPLAY_NAME_LIST = Arrays.asList(LANGUAGE_DISPLAY_NAMES);
}
