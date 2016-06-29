package test;

import components.TRWordLanguageHandler;

/**
 * 
 * Test case for {@link TRWordLanguageHandler}
 * 
 * @author mervess
 *
 */
public class TRWordLanguageHandlerTest
{
	public TRWordLanguageHandlerTest() {}
	
	public static void testWordLanguageHandler(String line)
	{
		TRWordLanguageHandler languageHandler = new TRWordLanguageHandler();
		System.out.println(languageHandler.canHandle(line));
	}

	public static void main(String[] args)
	{
		testWordLanguageHandler("=={{Dil|Türkçe}}==");
		testWordLanguageHandler("=={{Dil|İngilizce}}==");
		testWordLanguageHandler("=={{Dil|Malayca}}==");
		testWordLanguageHandler("== {{ Dil | Azerice }} ?=fdgdfg==");
	}
}
