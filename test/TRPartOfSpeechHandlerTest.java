package test;

import components.TRPartOfSpeechHandler;
import components.TRSenseDefinitionHandler;

/**
 * 
 * Test case for {@link TRPartOfSpeechHandler}
 * 
 * @author mervess
 *
 */
public class TRPartOfSpeechHandlerTest extends TRWiktionaryEntryParserTest
{
	public TRPartOfSpeechHandlerTest() {}

	private static void testSensePartOneByOne()
	{
		TRSenseDefinitionHandler handler = new TRSenseDefinitionHandler();
		handler.processHead("", null);
		handler.processBody(":[1] {{t|renk}} [[beyaz|beyazla]] az miktarda [[siyah]] karışmasından oluşan renk {{renk|#7F7F7F}}", null);
		handler.processBody(":[2] [[şehir]] ve [[kasaba|kasabaların]] dışında kalan, çoğu [[boş]] ve [[geniş]] yer", null);
		handler.processBody(":[1] [[kırmak]] fiilinin [[emir kipi]]", null);
	}
	
	public static void main(String[] args)
	{
		testSensePartOneByOne();
	}
}
