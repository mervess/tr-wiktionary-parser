package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import util.TRWiktionaryEntryParser;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionaryPage;
import de.tudarmstadt.ukp.jwktl.parser.WiktionaryEntryParser;

public class TRWiktionaryEntryParserTest
{
	protected final String TEST_PATH = "resources/tr/";
	
	public TRWiktionaryEntryParserTest() {}

	protected IWiktionaryPage parse(final String fileName) throws IOException
	{
		StringBuilder text = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(fileName)), "UTF-8"));
		String line;
		while ((line = reader.readLine()) != null)
			text.append(line).append("\n");
		reader.close();
		WiktionaryPage result = new WiktionaryPage();
		WiktionaryEntryParser parser = new TRWiktionaryEntryParser();
		parser.parse(result, text.toString());
		return result;
	}
}
