package util;

import java.io.File;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import de.tudarmstadt.ukp.jwktl.parser.IWritableWiktionaryEdition;
import de.tudarmstadt.ukp.jwktl.parser.WiktionaryArticleParser;
import de.tudarmstadt.ukp.jwktl.parser.WiktionaryDumpParser;
import de.tudarmstadt.ukp.jwktl.parser.WritableBerkeleyDBWiktionaryEdition;

/**
 * 
 * @author mervess
 *
 */
public class TR_JWKTL extends JWKTL
{
	public TR_JWKTL()
	{
		super();
	}
	
	public static void parseWiktionaryDump(final File dumpFile, final File targetDirectory, 
										   boolean overwriteExisting)
	{
		IWritableWiktionaryEdition wiktionaryDB = new WritableBerkeleyDBWiktionaryEdition(
																targetDirectory, overwriteExisting);
		WiktionaryDumpParser parser = new WiktionaryDumpParser();
		parser.register(new WiktionaryArticleParser(wiktionaryDB, new TRWiktionaryEntryParser()));
		parser.parse(dumpFile);
	}
}
