package util;

import java.io.File;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEdition;

/**
 * 
 * @author mervess
 *
 */
public class DataAccessor
{
	public DataAccessor() {}

	public static IWiktionaryEdition getDB(String dbPath)
	{
		return JWKTL.openEdition(new File(dbPath));
	}
}
