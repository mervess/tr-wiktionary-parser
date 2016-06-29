package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import de.tudarmstadt.ukp.jwktl.api.util.ILanguage;
import de.tudarmstadt.ukp.jwktl.api.util.Language;

/**
 * TR_Language is created to handle Turkish forms of the langauge names to parse the data correctly in Turkish wiktionary.
 * 
 * @author mervess
 *
 */
public class TR_Language
{
	private static Map<String, String> languageCodesMap = null;
	
	public TR_Language() {}

	public static ILanguage findByName(String languageName)
	{
		initialize();
		return languageName != null && languageCodesMap.get(languageName) != null
					? Language.findByCode(languageCodesMap.get(languageName)) 
					: null;
	}
	
	private synchronized static void initialize()
	{
		if (languageCodesMap == null) {
			languageCodesMap = new HashMap<String, String>();
			
			try {
				BufferedReader reader = new BufferedReader(new FileReader(new File("resources/tr/tr_language_codes.txt")));
				
				String line;
				while ((line = reader.readLine()) != null) {
					int tabIndex = line.indexOf("\t");
					if (tabIndex != -1) {
						languageCodesMap.put(line.substring(tabIndex+1), line.substring(0, tabIndex)); // key = language-name(TR), value = language-code(ISO 639-1)
					}
				}
				
				reader.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
	}
}
