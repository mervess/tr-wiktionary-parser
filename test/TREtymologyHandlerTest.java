package test;

import components.TREtymologyHandler;

/**
 * 
 * Test case for {@link TREtymologyHandler}
 * 
 * @author mervess
 *
 */
public class TREtymologyHandlerTest implements ITRWiktionaryEntryParserTest
{
	public TREtymologyHandlerTest() { }

	@Override
	public void testLineByLine()
	{
		String[] etymologyItemsList = {":[1] {{köken|Arapça}} {{çeviri|ar|كلام}}", // from the page "kelam"
									   ":{{k|Arapça}} {{Arap yazısı|حِسَابٌ|حساب|tr=ħisa:b(un)}}", // from the page "hesap
									   ": {{k|Keltçe|dil=İngilizce}} ---> {{köken|Latince|dil=İngilizce}} ''carrum, carrus''", // from the page "car"
									   ":[1] {{k|Farsça}} {{çeviri|fa|تخته}}", // from the page "tahta",
									   ":[1] {{k|Eski Türkçe}}", // from the page "kır"
									   ":[1] {{k|Eski Türkçe|dil=Gagavuzca}} ata", // from the page "ata"
									   ":{{k|Arapça}} {{Arap yazısı|عربة|عَرَبَةٌ|tr=ʕaraba(tun)}}" // from the page "araba"
									   };
		TREtymologyHandler etymologyHandler = new TREtymologyHandler();
		
		for (String listItem: etymologyItemsList) {
			etymologyHandler.processHead(listItem, null);
			etymologyHandler.processBody(listItem, null);
			System.out.println(etymologyHandler.getEtymology());
		}
	}

	@Override
	public void testTheFile(String fileName) { }

	public static void main(String[] args)
	{
		TREtymologyHandlerTest etymologyHandlerTester = new TREtymologyHandlerTest();
		etymologyHandlerTester.testLineByLine();
	}
}
