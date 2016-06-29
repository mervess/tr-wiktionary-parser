package components;

import java.util.regex.Matcher;

import de.tudarmstadt.ukp.jwktl.parser.util.ParsingContext;

/**
 * This is a translation handler component covering translations from only Turkic languages.
 * e.g. from the page "araba"
 * {{Türk Dilleri}}
 * {{Üst}}
 * *{{alt}}: {{ç|alt|}}
 * *{{az}}: [1] {{ç|az|araba}}; [2] {{ç|az|avtomobil}}...
 * 
 * @author mervess
 *
 */
public class TRTurkicLanguagesTranslationHandler extends TRTranslationHandler
{
	public TRTurkicLanguagesTranslationHandler()
	{
		super("Türk Dilleri");
	}

	@Override
	public boolean processBody(String text, final ParsingContext context)
	{
		text = text.trim();
		
		if (text.startsWith("{{Üst}}")
				|| text.startsWith("{{Orta}}")) { // if there are translation lines after delimiter...
			return true;
		}
		
		if (text.startsWith("{{Alt}}")) {
			return false;
		}
		
		String modifiedText = checkForSenseIdentification(text);
		if (modifiedText != null) {
			text = modifiedText;
		}
		
		Matcher matcher = TRANSLATION_PATTERN.matcher(text);
		if (matcher.find()) {
			String language = matcher.group(1);
			/**
			 * which refers to "Eski Türkçe", an unclear language to point according to literature, e.g. :
			 * *{{etr}}: [1] {{çeviri|tr|çıgıl tıgıl}},[2] {{çeviri|tr|keleçü}}
			 */
			if (!language.equals("etr")) {
				return processBodyForTurkicLanguages(text, context);
			} 
		} 

		return true;
	}
}
