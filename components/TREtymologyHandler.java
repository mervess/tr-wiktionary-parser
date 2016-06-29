package components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tudarmstadt.ukp.jwktl.api.entry.WikiString;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.parser.components.BlockHandler;
import de.tudarmstadt.ukp.jwktl.parser.util.ParsingContext;

/**
 * 
 * TREtymologyHandler is a component to parse etymology information (the part starting with "Köken") from TR wiktionary pages.
 * e.g. from the page "car"
 * {{Köken}}
 * :{{k|Türkçe}}
 * 
 * @author mervess
 *
 */
public class TREtymologyHandler extends BlockHandler
{
	private StringBuilder etymology;
	private static final Pattern ETYMOLOGY_PATTERN = 
			Pattern.compile("\\{\\{\\s*(?:k|köken)\\s*\\|\\s*([^}]+?)\\s*\\}\\}"); 
	
	public TREtymologyHandler()
	{
		super("Köken");
	}
	
	public TREtymologyHandler(final String... labels)
	{
		super(labels);
	}

	@Override
	public boolean processHead(final String textLine, final ParsingContext context)
	{	
		etymology = new StringBuilder();
		return super.processHead(textLine, context);
	}

	@Override
	public boolean processBody(String textLine, final ParsingContext context)
	{
		textLine = textLine.trim();
		
		Matcher matcher = ETYMOLOGY_PATTERN.matcher(textLine);
		if (matcher.find()) {
			String etymologyText = matcher.group(1);
			if (etymologyText != null 
					&& !etymologyText.isEmpty()) {
				etymology.append(etymologyText);
			}
		} else if (!textLine.isEmpty()) {		
			etymology.append(textLine);
		}
		return false;
	}

	public void fillContent(final ParsingContext context)
	{		
		if (etymology.length() > 0) {
			WiktionaryEntry posEntry = context.findEntry();
			posEntry.setWordEtymology(new WikiString(etymology.toString()));
		}
	}
	
	public String getEtymology()
	{
		return etymology.toString();
	}
}
