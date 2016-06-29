package test;

import java.io.IOException;
import java.util.List;

import components.TRSenseExampleHandler;

import de.tudarmstadt.ukp.jwktl.api.IWikiString;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;

/**
 * 
 * Test case for {@link TRSenseExampleHandler}
 * 
 * @author mervess
 *
 */
public class TRSenseExampleHandlerTest extends TRWiktionaryEntryParserTest
									   implements ITRWiktionaryEntryParserTest
{
	public TRSenseExampleHandlerTest() {}
	
	@Override
	public void testLineByLine() {}

	@Override
	public void testTheFile(String fileName)
	{
		try {
			IWiktionaryPage page = parse(fileName);
			@SuppressWarnings("unchecked")
			List<IWiktionaryEntry> entries = (List<IWiktionaryEntry>) page.getEntries();
			for (int i=0; i<entries.size(); i++) {
				IWiktionaryEntry entry = entries.get(i);
				for (IWikiString example : entry.getExamples()) {
					System.out.println(example.getText());
					System.out.println("*****************************************");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args)
	{
		TRSenseExampleHandlerTest senseExampleHandlerTest = new TRSenseExampleHandlerTest();
		senseExampleHandlerTest.testTheFile(senseExampleHandlerTest.TEST_PATH + "araba.txt");
	}
}
