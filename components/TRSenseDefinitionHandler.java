package components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tudarmstadt.ukp.jwktl.api.entry.WikiString;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionarySense;
import de.tudarmstadt.ukp.jwktl.parser.components.BlockHandler;
import de.tudarmstadt.ukp.jwktl.parser.util.ParsingContext;
import de.tudarmstadt.ukp.jwktl.parser.util.StringUtils;

/**
 * 
 * TRSenseDefinitionHandler is a component to parse sense information of entries (the part starting with "Anlamlar") in TR wiktionary pages.
 * e.g. from the page "car"
 * {{Anlamlar}}
 * :[1] [[çağrı|Çağrı]], [[tellal]] ile duyurma
 * :[2] [[tehlike]] durumu
 *
 * @author mervess
 *
 */
public class TRSenseDefinitionHandler extends BlockHandler
{
	protected static final Pattern INDEX_PATTERN = Pattern.compile("^\\s*\\[([^\\[\\]]{0,20}?)\\](.*)$");
	protected Set<Integer> indexSet;
	
	protected Map<Integer, String> definitions;
	
	public TRSenseDefinitionHandler(String... labels)
	{
		super(labels);
	}

	public TRSenseDefinitionHandler()
	{
		super("Anlamlar");
	}
	
	@Override
	public boolean processHead(String text, ParsingContext context)
	{
		definitions = new HashMap<Integer, String>();
		return true;
	}
	
	@Override
	public boolean processBody(String textLine, final ParsingContext context)
	{
		String definition;
		textLine = textLine.trim();
		
		if (textLine.startsWith(":")
				&& !textLine.startsWith("::")) {
			
			definition = textLine.substring(1);
			Matcher matcher = INDEX_PATTERN.matcher(definition);
			
			if (matcher.find()) {
				String indexStr = matcher.group(1);
				definition = matcher.group(2).trim();
				indexSet = StringUtils.compileIndexSet(indexStr);
				for (Integer idx : indexSet) {
					appendDefinition(idx, definition);
				}
			} else {
				indexSet = null;
				appendDefinition(Integer.MAX_VALUE, definition.trim());
			}
		}
		
		return false;
	}
	
	protected void appendDefinition(final Integer idx, final String definition)
	{
		String def = definitions.get(idx);
		if (def == null) {
			definitions.put(idx, definition);
		} else {
			definitions.put(idx, def + "\n" + definition);
		}
	}

	public void fillContent(final ParsingContext context)
	{
		WiktionaryEntry posEntry = context.findEntry();
		for (Entry<Integer, String> definition : definitions.entrySet()) {
			if (definition.getValue().isEmpty())
				continue; 
			
			WiktionarySense sense = posEntry.createSense();
			if (definition.getKey() < Integer.MAX_VALUE) {
				sense.setMarker(Integer.toString(definition.getKey()));
			}
			sense.setGloss(new WikiString(definition.getValue()));
			posEntry.addSense(sense);
		}		
	}
}
