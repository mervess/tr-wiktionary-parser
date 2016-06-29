package components;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.TR_Language;
import de.tudarmstadt.ukp.jwktl.api.PartOfSpeech;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.parser.components.BlockHandler;
import de.tudarmstadt.ukp.jwktl.parser.util.ParsingContext;

/**
 * TRPartOfSpeechHandler is a component to parse POS tags (as in Noun, Verb, Adjective etc.) in TR wiktionary pages.
 * e.g.
 * ==={{Söz türü|İsim|Türkçe}}===
 * 
 * @author mervess
 *
 */
public class TRPartOfSpeechHandler extends BlockHandler
{
	protected static final Pattern POS_PATTERN = Pattern.compile("\\{\\{Söz\\s*türü\\|([^\\|\\}]+)(?:\\|([^\\|\\}]+))?\\}\\}");
	
	protected TREntryFactory entryFactory = null;
	protected List<PartOfSpeech> posList = null;
	
	public TRPartOfSpeechHandler()
	{
		entryFactory = new TREntryFactory();
	}
	
	/** Check if the given text contains a part of speech header.
	 */
	public boolean canHandle(String blockHeader)
	{
		if (blockHeader == null || blockHeader.isEmpty()) {
			return false;
		}
		
		blockHeader = blockHeader.trim();
		if ((blockHeader.startsWith("===") || blockHeader.startsWith("'''"))
				&& blockHeader.length() > 7) {				
			Matcher matcher = POS_PATTERN.matcher(blockHeader);
			return matcher.find();
		} else {
			return false;
		}
	}
	
	/** Extract the part of speech tags and additional grammatical 
	 *  information. */
	@Override
	public boolean processHead(final String text, final ParsingContext context)
	{
		PartOfSpeech partOfSpeech = null;
		posList = new ArrayList<PartOfSpeech>();
		
		// Extract POS.
		String textLine = text.trim();
		if (textLine.isEmpty())
			return true;
		
		Matcher headBlockMatcher = POS_PATTERN.matcher(textLine);
		
		while (headBlockMatcher.find()) {
			
			if (context.getLanguage() == null
					&& headBlockMatcher.groupCount() >= 2
					&& headBlockMatcher.group(2) != null){
					
				context.setLanguage(TR_Language.findByName(headBlockMatcher.group(2)));
			}

			String posLabel = headBlockMatcher.group(1);
			PartOfSpeech pos = entryFactory.findPartOfSpeech(posLabel); 
			if (pos != null) {
				if (partOfSpeech == null) {
					partOfSpeech = pos;
					context.setPartOfSpeech(partOfSpeech);
				} else
					posList.add(pos);
			}
		}
		
		return true;
	}
	
	/** Set the part of pseech tags and additional grammatical information. */
	public void fillContent(final ParsingContext context)
	{	
		WiktionaryEntry entry = entryFactory.createEntry(context);
		
		for (int i=0; i<posList.size(); i++) {
			entry.addPartOfSpeech(posList.get(i));
		}
		
		context.getPage().addEntry(entry);
	}
}
