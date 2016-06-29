package test;

import java.io.IOException;
import java.util.List;

import components.TRSenseDefinitionHandler;

import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;

/**
 * Test case for {@link TRSenseDefinitionHandler}.
 * 
 * @author mervess
 *
 */
public class TRSenseDefinitionHandlerTest extends TRWiktionaryEntryParserTest
										  implements ITRWiktionaryEntryParserTest
{
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
				System.out.println("entry-"+i+" : "+entry);
				System.out.println("senseCount : " + entry.getSenseCount());
				System.out.println("senses : "+entry.getSenses());
				System.out.println("*****************************************");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		TRSenseDefinitionHandlerTest senseDefinitionHandlerTest 
										= new TRSenseDefinitionHandlerTest();
		senseDefinitionHandlerTest.testTheFile(senseDefinitionHandlerTest.TEST_PATH + "söz.txt");
	}
}
