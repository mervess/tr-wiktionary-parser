package components;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.jwktl.api.IWikiString;
import de.tudarmstadt.ukp.jwktl.api.entry.WikiString;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionarySense;
import de.tudarmstadt.ukp.jwktl.parser.de.components.DESenseIndexedBlockHandler;

/**
 * TRSenseExampleHandler is a component to parse examples belonging to the entries (the part starting with "Örnekler") in TR wiktionary pages.
 * e.g. from the page "araba"
 * {{Örnekler}}
 * :[1] "İki '''araba''' saman", "bir '''araba''' kömür"
 * 
 * @author mervess
 *
 */
public class TRSenseExampleHandler extends DESenseIndexedBlockHandler<IWikiString>
{
	public TRSenseExampleHandler()
	{
		super("Örnekler");
	}

	protected List<IWikiString> extract(int index, final String text)
	{
		String example = text;
		example = example.replace('\n', ' ');
		example = example.replaceAll("'''|''", "");
		example = example.trim();
		if (example.isEmpty()) {
			return null;
		}
		
		List<IWikiString> result = new ArrayList<IWikiString>();
		result.add(new WikiString(example));
		return result;
	}

	protected void updatePosEntry(final WiktionaryEntry posEntry, final IWikiString example)
	{
		posEntry.getUnassignedSense().addExample(example);
	}
	
	protected void updateSense(final WiktionarySense sense, final IWikiString example)
	{
		sense.addExample(example);
	}
}
