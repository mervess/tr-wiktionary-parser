package test;

import java.io.IOException;
import java.util.List;

import components.TRTurkicLanguagesTranslationHandler;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryTranslation;

/**
 * 
 * Test case for {@link TRTurkicLanguagesTranslationHandler}
 * 
 * @author mervess
 *
 */
public class TRTurkicLanguagesTranslationHandlerTest extends TRWiktionaryEntryParserTest
													 implements ITRWiktionaryEntryParserTest
{
	@Override
	public void testLineByLine()
	{
		TRTurkicLanguagesTranslationHandler turkicLangsHandler = new TRTurkicLanguagesTranslationHandler();
		String text = "*{{az}}: [1] {{çeviri|az|ana}}, {{çeviri|az|ciji}} (lehçe), {{çeviri|az|nənə}} (lehçe)";
		turkicLangsHandler.processHead(text, null);
		turkicLangsHandler.processBody(text, null);
	}

	@Override
	public void testTheFile(String fileName)
	{
		try {
			IWiktionaryPage page = parse(fileName);
			@SuppressWarnings("unchecked")
			List<IWiktionaryEntry> entries = (List<IWiktionaryEntry>) page.getEntries();
			List<IWiktionaryTranslation> translations;
			for (int i=0; i<entries.size(); i++) {
				translations = entries.get(i).getTranslations();
				if (!translations.isEmpty()) {
					System.out.println(translations);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		TRTurkicLanguagesTranslationHandlerTest turkicLangsHandlerTest = new TRTurkicLanguagesTranslationHandlerTest();
		turkicLangsHandlerTest.testLineByLine();
		turkicLangsHandlerTest.testTheFile(turkicLangsHandlerTest.TEST_PATH + "araba.txt");
	}
}
