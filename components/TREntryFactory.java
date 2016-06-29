package components;

import java.util.HashMap;
import java.util.Map;

import de.tudarmstadt.ukp.jwktl.api.PartOfSpeech;
import de.tudarmstadt.ukp.jwktl.parser.entry.EntryFactory;

/**
 * TREntryFactory carries all the necessary information to parse POS tags in TR wiktionary pages.
 * 
 * @author mervess
 *
 */
public class TREntryFactory extends EntryFactory
{
	public TREntryFactory()
	{
		super();
	}
	
	@Override
	public PartOfSpeech findPartOfSpeech(String name)
	{
		return PartOfSpeech.findByName(name, posMap); 
	}
	
	protected static final Map<String, PartOfSpeech> posMap;
	static {
		posMap = new HashMap<String, PartOfSpeech>();
		
		posMap.put("AD", PartOfSpeech.NOUN);
		posMap.put("İSIM", PartOfSpeech.NOUN); // e.g. "sarı"
		posMap.put("AD_(ÇEKILMIŞ)", PartOfSpeech.NOUN);
		
		posMap.put("ÖNAD", PartOfSpeech.ADJECTIVE); // e.g. "çirkin"
		posMap.put("ÖN_AD", PartOfSpeech.ADJECTIVE); // e.g. "vahşi"
		posMap.put("ÖN_AD_(ÇEKILMIŞ)", PartOfSpeech.ADJECTIVE);
		posMap.put("SIFAT", PartOfSpeech.ADJECTIVE); // "sarı, çetin"
		
		posMap.put("ZARF", PartOfSpeech.ADVERB); // e.g. "zamanla"
		posMap.put("BELIRTEÇ", PartOfSpeech.ADVERB); // e.g. "hızlı"
		posMap.put("ARA_SÖZ", PartOfSpeech.ADVERB); // e.g. должно быть
		
		posMap.put("FIIL", PartOfSpeech.VERB); //?
		posMap.put("EYLEM", PartOfSpeech.VERB); // e.g. "koşmak"
		posMap.put("EYLEM_(ÇEKILMIŞ)", PartOfSpeech.VERB); // e.g. acemileşecek
		posMap.put("FIIL_(ÇEKILMIŞ)", PartOfSpeech.VERB); // e.g. gitmez
		posMap.put("EMIR", PartOfSpeech.VERB); // e.g. "kır"
		posMap.put("YARDIMCI_EYLEM", PartOfSpeech.AUXILIARY_VERB); // e.g. "eylemek"
		
		posMap.put("ÖZEL_AD", PartOfSpeech.PROPER_NOUN);
		posMap.put("SOYADI", PartOfSpeech.LAST_NAME); 
		
		posMap.put("HARF", PartOfSpeech.LETTER);
		posMap.put("RAKAM", PartOfSpeech.NUMERAL); // e.g. "on, altı, yirmi dokuz"
		posMap.put("SAYI", PartOfSpeech.NUMERAL); // e.g. "3210, bin"
		posMap.put("SIRAL_SAYI", PartOfSpeech.NUMERAL); // e.g. ikinci
		posMap.put("SIMGE", PartOfSpeech.SYMBOL); // e.g. "π"
		
		posMap.put("EK", PartOfSpeech.AFFIX); // e.g. "-cı, -kun"
		posMap.put("ÖNEK", PartOfSpeech.PREFIX); // e.g. ""
		posMap.put("ÖN_EK", PartOfSpeech.PREFIX);
		posMap.put("SONEK", PartOfSpeech.SUFFIX); // e.g. "-zade"
		posMap.put("SON_EK", PartOfSpeech.SUFFIX); 
		posMap.put("EK_EYLEM", PartOfSpeech.SUFFIX); // e.g. -imek, -ise
		
		posMap.put("DEYIM", PartOfSpeech.IDIOM); // e.g. "akıl vermek"
		posMap.put("ATASÖZÜ", PartOfSpeech.PROVERB);
		
		posMap.put("KISALTMA", PartOfSpeech.ABBREVIATION);
		posMap.put("KISALTILMIŞ_SÖZCÜK", PartOfSpeech.ABBREVIATION);
		
		posMap.put("ÜNLEM", PartOfSpeech.INTERJECTION); // e.g. "a, hadi"
		posMap.put("SÖZCE", PartOfSpeech.INTERJECTION); // e.g. afiyet olsun
		posMap.put("BAĞLAÇ", PartOfSpeech.CONJUNCTION);
		
		posMap.put("EDAT", PartOfSpeech.POSTPOSITION);
		posMap.put("İLGEÇ", PartOfSpeech.POSTPOSITION); 
		
		posMap.put("ZAMIR", PartOfSpeech.PRONOUN);
		posMap.put("ADIL", PartOfSpeech.PRONOUN);
		posMap.put("ADIL_(ÇEKILMIŞ)", PartOfSpeech.PRONOUN);
		
		posMap.put("TANIMLIK", PartOfSpeech.ARTICLE); // e.g. "das, der, la, los"
	}
}
