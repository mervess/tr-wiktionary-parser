package test;

import java.util.Iterator;
import java.util.List;

import util.DataAccessor;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEdition;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;
import de.tudarmstadt.ukp.jwktl.api.IWiktionarySense;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryTranslation;
import de.tudarmstadt.ukp.jwktl.api.RelationType;
import de.tudarmstadt.ukp.jwktl.api.util.Language;

/**
 * 
 * DataAccessorTest provides sample methods about the usage of the library. 
 * {@link DataAccessor}
 * 
 * @author mervess
 *
 */
public class DataAccessorTest
{
	public static void evaluateData(String dbPath)
	{
		IWiktionaryEdition wiktionaryEdition = DataAccessor.getDB(dbPath); // e.g. "db/tr/"
		
		int pageCount = 0;
		int entryCount = 0;
		int senseCount = 0;
		int translationCount = 0;
		
		for (IWiktionaryPage page : wiktionaryEdition.getAllPages()) {
			for (IWiktionaryEntry entry : page.getEntries()) {
				senseCount += entry.getSenseCount();
				if (!entry.getTranslations().isEmpty()) {
					translationCount += entry.getTranslations().size();
				}
				entryCount++;
			}
			pageCount++;
		}
		
		System.out.println("Pages: " + pageCount);
		System.out.println("Entries: " + entryCount);
		System.out.println("Senses: " + senseCount);
		System.out.println("TranslationCount: " + translationCount);
		
		wiktionaryEdition.close();
	}
	
	public static void evaluateDataForOnlyTRWords(String dbPath)
	{
		IWiktionaryEdition wiktionaryEdition = DataAccessor.getDB(dbPath);
		
		int pageCount = 0;
		int entryCount = 0;
		int senseCount = 0;
		int translationCount = 0;
		
		for (IWiktionaryPage page : wiktionaryEdition.getAllPages()) {
			for (IWiktionaryEntry entry : page.getEntries()) {
				if (entry.getWordLanguage() != null
						&& entry.getWordLanguage().equals(Language.get("tur"))) {
					senseCount += entry.getSenseCount();
					Iterator<? extends IWiktionarySense> senseIter = entry.getSenses().iterator();
					while (senseIter.hasNext()) {
						List<IWiktionaryTranslation> listTranslations = senseIter.next().getTranslations();
						if (listTranslations != null) {
							translationCount += listTranslations.size();
						}
					}
					entryCount++;
				}
			}
			pageCount++;
		}
		
		System.out.println("Pages: " + pageCount);
		System.out.println("Entries: " + entryCount);
		System.out.println("Senses: " + senseCount);
		System.out.println("TranslationCount: " + translationCount);
		
		wiktionaryEdition.close();
	}
	
	public static void evaluateDataByUsingOneWord(String dbPath, String word)
	{
		IWiktionaryEdition wkt = DataAccessor.getDB(dbPath);
		IWiktionaryPage page = wkt.getPageForWord(word);
		
		System.out.println(page.getTitle());
		
		int entryCount = 0;
		int senseCount = 0;
		int translationCount = 0;
		
		@SuppressWarnings("unchecked")
		List<IWiktionaryEntry> entries = (List<IWiktionaryEntry>) page.getEntries();
		
		for (int i=0; i<entries.size(); i++) {
			System.out.println("Word language -> "+entries.get(i).getWordLanguage());
			System.out.println("Etymology -> "+entries.get(i).getWordEtymology());
			
			if (!entries.get(i).getRelations(RelationType.SYNONYM).isEmpty()) {
				System.err.println("Synonym test -> "+entries.get(i).getRelations(RelationType.SYNONYM).get(0).getTarget());
			}
			senseCount += entries.get(i).getSenseCount();
			
			// some trials
			System.out.println(i+"#"+entries.get(i).getWord());
			System.out.println(entries.get(i).getWordLanguage() != null ? entries.get(i).getWordLanguage().getCode() + "\t" + entries.get(i).getWord() : "");
			System.out.println(entries.get(i).getPartOfSpeech().toString() + "-> "
									+ entries.get(i).getTranslations()); // translations by entry
			
			Iterator<? extends IWiktionarySense> senseIter = entries.get(i).getSenses().iterator();
			while (senseIter.hasNext()) {
				IWiktionarySense sense = senseIter.next();
				System.out.println(sense.getIndex());
				List<IWiktionaryTranslation> listTranslations = sense.getTranslations();
				if (listTranslations != null) {
					translationCount += listTranslations.size();
					System.out.println(listTranslations); // we can get words' translations depending on senses with this way
				}
			}
			entryCount++;
		}
		
		System.out.println("Entries: " + entryCount);
		System.out.println("Senses: " + senseCount);
		System.out.println("TranslationCount: " + translationCount);
		
		wkt.close();
	}
	
	public static void getStatistics(String dbPath)
	{
		IWiktionaryEdition wiktionaryEdition = DataAccessor.getDB(dbPath);
		
		int allPages = 0, pagesWithNoEntries = 0, turWord = 0;
		int entryCount = 0, senseCount = 0;
		int translation = 0, lackOfTranslation = 0;
		int synonym = 0, antonym = 0, hypernym = 0, hyponym = 0, coTerm = 0, charWordComb = 0, derivedTerm = 0;
		
		for (IWiktionaryPage page : wiktionaryEdition.getAllPages()) {
			if (page.getEntries().isEmpty()) {
				pagesWithNoEntries++;
			}
			
			for (IWiktionaryEntry entry : page.getEntries()) {
				if (entry.getWordLanguage() != null) {
					senseCount += entry.getSenseCount();
					if (!entry.getTranslations().isEmpty()) {
						translation += entry.getTranslations().size();
					} else {
						lackOfTranslation++;
					}
					
					if (entry.getWordLanguage().equals(Language.get("tur"))) {
						turWord++;
					}
					
					synonym += entry.getRelations(RelationType.SYNONYM).size();
					antonym += entry.getRelations(RelationType.ANTONYM).size();
					hypernym += entry.getRelations(RelationType.HYPERNYM).size();
					hyponym += entry.getRelations(RelationType.HYPONYM).size();
					coTerm += entry.getRelations(RelationType.COORDINATE_TERM).size();
					charWordComb += entry.getRelations(RelationType.CHARACTERISTIC_WORD_COMBINATION).size();
					derivedTerm += entry.getRelations(RelationType.DERIVED_TERM).size();
					
					entryCount++;
				}
			} //-- end of entries loop
			
			allPages++;
		}
		wiktionaryEdition.close();
		
		System.out.println("Total pages: " +allPages);
		System.out.println("pagesWithNoEntries: " + pagesWithNoEntries);
		System.out.println("Entries: " + entryCount);
		System.out.println("Senses: " + senseCount);
		System.out.println("TranslationCount: " + translation);
		System.out.println("Lack of Translations: " + lackOfTranslation);
		System.out.println("turWord: " + turWord);
		
		System.out.println("synonym: " +synonym);
		System.out.println("antonym: " +antonym);
		System.out.println("hypernym: " +hypernym);
		System.out.println("hyponym: " +hyponym);
		System.out.println("coTerm: " + coTerm);
		System.out.println("charWordComb: " +charWordComb);
		System.out.println("derivedTerm: " +derivedTerm);
	}
}
