package components;

import de.tudarmstadt.ukp.jwktl.api.RelationType;
import de.tudarmstadt.ukp.jwktl.parser.de.components.DERelationHandler;

/**
 * 
 * TRRelationHandler is a component to parse data about relations in TR wiktionary pages.
 * Relation information varies under many labels for instance {Üst, Alt, Yan} Kavramlar, Sözcük Birliktelikleri etc.; so it is handled from the main class.
 * 
 * @author mervess
 *
 */
public class TRRelationHandler extends DERelationHandler
{
	public TRRelationHandler(final RelationType relationType, final String... labels)
	{
		super(relationType, labels);
	}
}
