package components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.TR_Language;
import de.tudarmstadt.ukp.jwktl.api.util.ILanguage;
import de.tudarmstadt.ukp.jwktl.parser.components.BlockHandler;
import de.tudarmstadt.ukp.jwktl.parser.util.ParsingContext;

/**
 * Parser component to find out words' languages.
 * 
 * @author mervess
 *
 */
public class TRWordLanguageHandler extends BlockHandler
{
	/** 
	 * language regular expression pattern
	 * e.g. =={{Dil|Türkçe}}==
	 */
	private static final Pattern LANGUAGE_PATTERN = Pattern.compile("^==\\s*\\{\\{\\s*Dil\\s*\\|\\s*([^}]+?)\\s*\\}\\}");
	
	protected ILanguage language;
	
	public TRWordLanguageHandler() {}
	
	public TRWordLanguageHandler(final String... labels)
	{
		super(labels);
	}
	
	/** Determine if the text line contains the language pattern. If the 
	 *  language pattern is found, the entry's word and its language will 
	 *  be extracted from the text line. */
	public boolean canHandle(final String blockHeader)
	{
		if (blockHeader != null) {
			language = null;
			Matcher matcher = LANGUAGE_PATTERN.matcher(blockHeader);
			if (!matcher.find()) {
				return false;
			}
			
			language = TR_Language.findByName(matcher.group(1));
			return true;
		} else {
			return false;
		}
	}
		
	/** Store the word and its language in the parsing context. */
	public void fillContent(final ParsingContext context)
	{
		context.setLanguage(language);
	}
}
