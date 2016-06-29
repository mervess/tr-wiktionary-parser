package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tudarmstadt.ukp.jwktl.api.IWiktionaryTranslation;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionarySense;
import de.tudarmstadt.ukp.jwktl.api.entry.WiktionaryTranslation;
import de.tudarmstadt.ukp.jwktl.api.util.ILanguage;
import de.tudarmstadt.ukp.jwktl.api.util.Language;
import de.tudarmstadt.ukp.jwktl.parser.components.BlockHandler;
import de.tudarmstadt.ukp.jwktl.parser.util.ParsingContext;
import de.tudarmstadt.ukp.jwktl.parser.util.StringUtils;

/**
 * 
 * TRTranslationHandler is a component to parse translations of the entries (the part starting with "Çeviriler|Tercümeler") in TR wiktionary pages.
 * e.g. from the page "araba"
 * {{Tercümeler}}
 * {{Üst}}
 * *{{af}}: [2] {{ç|af|motor}}; [3] {{ç|af|wa}}...
 * 
 * @author mervess
 *
 */
public class TRTranslationHandler extends BlockHandler
{
	/**
	 * Old Form :
	 * protected static final Pattern TRANSLATION_PATTERN = Pattern.compile(
			"^\\*\\s*" + "\\{\\{([^}]*).*?\\}\\}" + "[: ;]*" 
			+ "\\[([^\\[\\]]+?)\\]" + "\\s*" + "(.*?)" 
			+ "(?:\\{\\{(çeviri.*?)\\}\\}|\\[\\[(.*?)\\]\\])" + "(.*)$");
	 */
	protected static final Pattern TRANSLATION_PATTERN = Pattern.compile(
			"\\{\\{([^}]*).*?\\}\\}" + "[: ;]*" 
			+ "\\[([^\\[\\]]+?)\\]" + "\\s*" + "(.*?)" 
			+ "(?:\\{\\{((?:çeviri|ç).*?)\\}\\}|\\[\\[(.*?)\\]\\])" + "(.*)$");
	protected static final Pattern ADDITIONAL_TRANSLATION_PATTERN = Pattern.compile("^\\s*"
			+ "(?:\\[([^\\[\\]]+?)\\])?" + "\\s*" + "(.*?)"
			+ "(?:\\{\\{((?:çeviri|ç).*?)\\}\\}|\\[\\[(.*?)\\]\\])" + "(.*)$"); 
	protected static final Pattern NEXT_TRANSLATION_PATTERN = Pattern.compile(
			"^(.*?)(?:[,;]+|(\\{\\{(?:çeviri|ç)\\S*\\|))(.*)$");
	protected static final Pattern PREPARATION_PATTERN = Pattern.compile("(\\s\\[([^\\[\\]]+?)\\]\\s)");
	
	protected Map<Integer, List<IWiktionaryTranslation>> sensNum2trans;
	
	public TRTranslationHandler()
	{
		super("Çeviriler");
	}
	
	public TRTranslationHandler(String... labels)
	{
		super(labels);
	}
	
	@Override
	public boolean processHead(String text, ParsingContext context)
	{	
		sensNum2trans = new HashMap<Integer, List<IWiktionaryTranslation>>();
		return true;
	}
	
	@Override
	public boolean processBody(String text, final ParsingContext context)
	{
		text = text.trim();
		
		if (text.startsWith("{{Üst}}")
				|| text.startsWith("{{Orta}}")) { // if there are translation lines after delimiter...
			return true;
		}
		
		if (text.startsWith("{{Alt}}")) {
			return false;
		}
		
		String modifiedText = checkForSenseIdentification(text);
		return processBodyForTurkicLanguages(modifiedText != null ? modifiedText : text, context);
	}
	
	protected boolean processBodyForTurkicLanguages(String text, final ParsingContext contex)
	{
		text = PREPARATION_PATTERN.matcher(text).replaceAll(";$1");
		Matcher matcher = TRANSLATION_PATTERN.matcher(text);
		
		if (matcher.find()) {
			boolean usesTemplate = true;
			String language = matcher.group(1);
			String index = matcher.group(2);
			String prefix = matcher.group(3);
			String translation = matcher.group(4);
			if (translation == null) {
				translation = matcher.group(5);
				usesTemplate = false;
			}
			String additionalInformation = matcher.group(6);
						
			String remainingText;
			do {
				// Check for additional translations.
				remainingText = null;
				matcher = NEXT_TRANSLATION_PATTERN.matcher(additionalInformation);
				if (matcher.find()) { 
					additionalInformation = matcher.group(1).trim();
					remainingText = matcher.group(2);
					
					if (remainingText == null) {
						remainingText = matcher.group(3);
					} else {
						remainingText += matcher.group(3);
					}
					
					if (additionalInformation.endsWith(":")) {
						remainingText = additionalInformation + remainingText;
						additionalInformation = "";
					}
				}			
				
				// Compile index set.
				Set<Integer> indexSet = StringUtils.compileIndexSet(index); // holds the senses, e.g. 1, 2...

				// Prepare the translation.
				if (language != null) {
					int i = language.indexOf('|');
					if (i > 0) {
						language = language.substring(0, i);
					}
					language = language.trim();
				}
				ILanguage translatedLang = Language.findByCode(language);
				if (translatedLang == null) {
					translatedLang = Language.findByName(language);
				}
				
				String[] fields = null;
				String translationText = translation;
				if (usesTemplate) {
					fields = StringUtils.split(translation, '|');
					if ( (fields.length == 3 || fields.length == 4)
							&& !fields[2].trim().isEmpty()) {
						translationText = fields[2].trim();
					} else {
						translationText = null;
					}
				} else {
					int i = translation.indexOf('|'); // [[...|...]]
					if (i >= 0) {
						translationText = translation.substring(i + 1);
					}
				}
				
				if (translationText != null && !translationText.isEmpty()) {
					if (!usesTemplate && !additionalInformation.contains("{")) {
						translationText = cleanText(translationText + additionalInformation);
						additionalInformation = "";
					}

					WiktionaryTranslation trans = new WiktionaryTranslation(translatedLang, translationText);

					additionalInformation = cleanText(prefix + " " + additionalInformation);
					trans.setAdditionalInformation(additionalInformation);

					// Save the translation
					for (Integer i : indexSet) {
						List<IWiktionaryTranslation> translations = sensNum2trans.get(i);
						if (translations == null) {
							translations = new ArrayList<IWiktionaryTranslation>();
							sensNum2trans.put(i, translations);
						}
						translations.add(trans);
					}
				}
				
				// Perform another iteration if there is any remaining text.
				if (remainingText != null) {
					do {
						matcher = ADDITIONAL_TRANSLATION_PATTERN.matcher(remainingText);
						if (matcher.find()) {
							usesTemplate = true;
							String indexTmp = matcher.group(1);
							
							if (indexTmp != null) {
								index = indexTmp; // otherwise, use the index of the previous translation! 
							}
							
							prefix = matcher.group(2);
							translation = matcher.group(3);
							
							if (translation == null) {
								translation = matcher.group(4);
								usesTemplate = false;
							}
							additionalInformation = matcher.group(5);
							break;
						} else {
							matcher = NEXT_TRANSLATION_PATTERN.matcher(remainingText);
							String remainingTextOld = remainingText;
							if (matcher.find()) {
								remainingText = matcher.group(2) + matcher.group(3);
								if (remainingTextOld.equals(remainingText)) {
									System.out.println("TEXT --> "+text); // that remarks a wrongly entered translation input.
									return true;
								}
							} else {
								remainingText = null;
							}
						}
					} while (remainingText != null);
				}
			} while (remainingText != null);
		} 
		return true;
	}
	
	/** 
	 * This part is added to catch translations without sense definition identification ( [x] ).
	 * e.g. *{{crh}}: {{çeviri|crh|ana}}, {{çeviri|crh|anniy}}
	 */
	protected String checkForSenseIdentification(String text)
	{
		if (!TRANSLATION_PATTERN.matcher(text).find()) {
			int indexOfTranslationPart = text.indexOf("{{ç");
			if (indexOfTranslationPart != -1) {
				StringBuilder stringBuilderText = new StringBuilder(0);
				stringBuilderText.append(text.substring(0, indexOfTranslationPart));
				stringBuilderText.append("[1] ");
				stringBuilderText.append(text.substring(indexOfTranslationPart));
				return stringBuilderText.toString();
			}
		}
		return null;
	}
	
	protected String cleanText(final String text)
	{
		String result = text;
		result = result.replace("[[", "");
		result = result.replace("]]", "");
		result = result.replace("'''", "");
		result = result.replace("''", "");
		int startIdx = 0;
		while (startIdx < result.length() && ":() ".indexOf(result.charAt(startIdx)) >= 0) {
			startIdx++;
		}
		
		int endIdx = result.length();
		
		while (endIdx > startIdx && ":() ".indexOf(result.charAt(endIdx - 1)) >= 0) {
			endIdx--;
		}
		
		if (endIdx >= 0 && startIdx < endIdx) {
			result = result.substring(startIdx, endIdx);
		} else {
			result = "";
		}
		
		return result;
	}

	/**
	 * Set translations to wordEntry object
	 * If no sense mapping is found, they are set as unclassified translations.
	 */
	public void fillContent(final ParsingContext context)
	{
		WiktionaryEntry posEntry = context.findEntry();
		if (posEntry != null) {
			for (Entry<Integer, List<IWiktionaryTranslation>> trans : sensNum2trans.entrySet()) {
				WiktionarySense sense = null;
				
				if (trans.getKey() >= 0) {
					sense = posEntry.findSenseByMarker(Integer.toString(trans.getKey()));				
				}
				
				if (sense == null) {
					sense = posEntry.getUnassignedSense();
				}
				
				for (IWiktionaryTranslation translation : trans.getValue()) {
					sense.addTranslation(translation);		
				}
			}
		}
	}
}
