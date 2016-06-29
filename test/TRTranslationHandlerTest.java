package test;

import java.io.IOException;
import java.util.List;

import components.TRTranslationHandler;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryTranslation;

/**
 * 
 * Test case for {@link TRTranslationHandler}
 * 
 * @author mervess
 *
 */
public class TRTranslationHandlerTest extends TRWiktionaryEntryParserTest
									  implements ITRWiktionaryEntryParserTest
{
	@Override
	public void testLineByLine()
	{
		TRTranslationHandler translHandler = new TRTranslationHandler();
		/**
		 * :{{nb}}: [1] {{çeviri|nb|arabisk}}
		 * :{{no}}: {{ç|no|client|m}}
		 */
		String text = ":{{nb}}: {{çeviri|nb|arabisk}}";
		translHandler.processHead(text, null);
		translHandler.processBody(text, null);
	}

	@Override
	public void testTheFile(String fileName)
	{
		try {
			IWiktionaryPage page = parse(fileName);
			/**
			 * entries for the word "ama" are like :
			 * ==={{Söztürü|Ad|Baskça}}===
			 * ==={{Söztürü|Bağlaç|Türkçe}}===
			 * ==={{Söztürü|Bağlaç|Somalice}}===
			 */
			@SuppressWarnings("unchecked")
			List<IWiktionaryEntry> entries = (List<IWiktionaryEntry>) page.getEntries();
			List<IWiktionaryTranslation> translations;
			int translationCount = 0;
			for (int i=0; i<entries.size(); i++) {
				/**
				 * for each entry there might be some translations or not.
				 */
				translations = entries.get(i).getTranslations();
				if (!translations.isEmpty()) {
					translationCount += translations.size();
					System.out.println(translations);
				}
			}
			System.out.println("translationCount : " + translationCount);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		TRTranslationHandlerTest translHandlerTest = new TRTranslationHandlerTest();
		translHandlerTest.testLineByLine();
		translHandlerTest.testTheFile(translHandlerTest.TEST_PATH + "yürümek.txt");
	}
}
