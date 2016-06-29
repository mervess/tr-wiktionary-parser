package util;

import java.io.File;

import de.tudarmstadt.ukp.jwktl.JWKTL;

/**
 * 
 * DataParser is a class to create the wiktionary berkeley db to be able to parse the data after all.
 * 
 * @author mervess
 *
 */
public class DataParser
{
	/**
	 * 
	 * @param filePath -> resource file path to read tha data
	 * @param dbPath   -> database folder path to locate the relevant database (suggested path db/{langKey}/)
	 * @param langKey  -> tr for Turkish, de for German, ru for Russian, en for English
	 */
	public static void parseData(String filePath, String dbPath, String langKey)
	{
		File dumpFile = new File(filePath); 
		File outputDirectory = new File(dbPath);

		if (langKey.equals("tr")) {
			TR_JWKTL.parseWiktionaryDump(dumpFile, outputDirectory, true);
		} else {
			JWKTL.parseWiktionaryDump(dumpFile, outputDirectory, true);
		}
	}
}
