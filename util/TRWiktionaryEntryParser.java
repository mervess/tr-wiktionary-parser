package util;

import components.TREntryFactory;
import components.TREtymologyHandler;
import components.TRPartOfSpeechHandler;
import components.TRRelationHandler;
import components.TRSenseDefinitionHandler;
import components.TRSenseExampleHandler;
import components.TRTranslationHandler;
import components.TRTurkicLanguagesTranslationHandler;
import components.TRWordLanguageHandler;
import de.tudarmstadt.ukp.jwktl.api.RelationType;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionaryPage;
import de.tudarmstadt.ukp.jwktl.api.util.ILanguage;
import de.tudarmstadt.ukp.jwktl.api.util.Language;
import de.tudarmstadt.ukp.jwktl.parser.WiktionaryEntryParser;
import de.tudarmstadt.ukp.jwktl.parser.components.InterwikiLinkHandler;
import de.tudarmstadt.ukp.jwktl.parser.util.ParsingContext;

/**
 * 
 * The backbone class of the TR_WIKT parser.
 * 
 * @author mervess
 *
 */
public class TRWiktionaryEntryParser extends WiktionaryEntryParser
{
	public TRWiktionaryEntryParser()
	{
		this(Language.get("tur"), "BAKINIZ"); // e.g. "selam"
	}
	
	public TRWiktionaryEntryParser(ILanguage language, String redirectName)
	{
		super(language, redirectName);
		
		// The registered items below are components of the parser. You may add new components by registering them here.
		
		register(new TREtymologyHandler());
		register(new TRSenseDefinitionHandler());
		register(new TRSenseExampleHandler());
		
		/**
		 * String variables are all labels in TR wiktionary pages. 
		 * Sometimes they are changed by the users (e.g. "Tercümeler" popped up recently). When it happens, parsed data will be reduced; so be aware!
		 */
		
		register(new TRRelationHandler(RelationType.SYNONYM, "Eş Anlamlılar"));
		register(new TRRelationHandler(RelationType.ANTONYM, "Karşıt Anlamlılar"));
		register(new TRRelationHandler(RelationType.HYPERNYM, "Üst Kavramlar"));
		register(new TRRelationHandler(RelationType.HYPONYM, "Alt Kavramlar"));
		register(new TRRelationHandler(RelationType.COORDINATE_TERM, "Yan Kavramlar"));
		register(new TRRelationHandler(RelationType.CHARACTERISTIC_WORD_COMBINATION, "Sözcük Birliktelikleri"));
		register(new TRRelationHandler(RelationType.CHARACTERISTIC_WORD_COMBINATION, "Kelime birliktelikleri"));
		register(new TRRelationHandler(RelationType.DERIVED_TERM, "Türetilmiş Kavramlar"));
		register(new TRRelationHandler(RelationType.SEE_ALSO, "Benzer Sözcükler"));
		
		register(new TRTurkicLanguagesTranslationHandler());
		register(new TRTranslationHandler("Çeviriler", "Tercümeler"));
		register(new TRWordLanguageHandler());
		register(new TRPartOfSpeechHandler());
		register(new InterwikiLinkHandler("Kategori"));
	}

	@Override
	protected ParsingContext createParsingContext(final WiktionaryPage page)
	{
		return new ParsingContext(page, new TREntryFactory());
	}

	@Override
	protected boolean isStartOfBlock(String line)
	{
		return line.startsWith("=")
			|| line.startsWith("{{")
			|| line.startsWith("[[") 
			|| (line.startsWith("'''") && line.endsWith("'''"));
	}
}
