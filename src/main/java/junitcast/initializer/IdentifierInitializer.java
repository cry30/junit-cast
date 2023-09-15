/**
 *
 */
package junitcast.initializer;

import java.util.Arrays;

import com.github.roycetech.ruleengine.utils.StringUtil;

import junitcast.ResourceFixture;
import junitcast.ResourceFixture.ResourceKey;

/**
 * This class initializes the caseId parameter from the resource fixture. This
 * was refactored out of ResourceFixture.
 */
public class IdentifierInitializer implements ResourceParameterInitializer {

	private final ResourceFixture resourceFixture;

	public IdentifierInitializer(final ResourceFixture resourceFixture) {
		this.resourceFixture = resourceFixture;
	}

	/** Initializes the case ID list. */
	@Override
	public void initialize()
	{
		for (int i = 0; i < getResourceFixture().getCaseList().size(); i++) {
			final int actualIdx = i + getResourceFixture().getDebugStart();
			final String key = ResourceKey.caseId.name() + actualIdx;
			if (getResourceFixture().getResourceBundle().containsKey(key)) {
				final String raw = getResourceFixture().getResourceString(key);
				getResourceFixture().getAttrList()
						.add(Arrays.asList(StringUtil.trimArray(raw.split(","))));
			} else {
				final String caseId = getResourceFixture().getCaseList()
						.toArray(new String[getResourceFixture().getCaseList().size()])[i];
				getResourceFixture().getAttrList().add(Arrays.asList(new String[] { caseId }));
			}
		}
	}

	/**
	 * Returns the bound resourceFixture for test-ability.
	 *
	 * @return the bound resourceFixture instance.
	 */
	/* default */ ResourceFixture getResourceFixture()
	{
		return this.resourceFixture;
	}
}
