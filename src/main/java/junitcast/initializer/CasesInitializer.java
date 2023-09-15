/**
 *
 */
package junitcast.initializer;

import junitcast.ResourceFixture;
import junitcast.ResourceFixture.ResourceKey;

/**
 * This class initializes the var parameter from the resource fixture. This was
 * refactored out of ResourceFixture.
 */
public class CasesInitializer implements ResourceParameterInitializer {

	private final ResourceFixture resourceFixture;

	public CasesInitializer(final ResourceFixture resourceFixture) {
		this.resourceFixture = resourceFixture;
	}

	/**
	 * Initialize cases from casedesc key in property file.
	 */
	@Override
	public void initialize()
	{
		if (getResourceFixture().getResourceBundle().containsKey(ResourceKey.debug_index.name())) {
			final String debugStartStr = getResourceFixture()
					.getResourceString(ResourceKey.debug_index.name()).trim();
			try {
				getResourceFixture().setDebugStart(Integer.valueOf(debugStartStr));
			} catch (final NumberFormatException e) {
				getResourceFixture().setDebugStart(0);
			}
		} else {
			getResourceFixture().setDebugStart(0);
		}

		int caseIndex = getResourceFixture().getDebugStart();
		while (caseIndex < 10) {
			final String key = ResourceKey.casedesc.name() + caseIndex++;
			if (getResourceFixture().getResourceBundle().containsKey(key)) {
				final String kaso = getResourceFixture().getResourceString(key);
				getResourceFixture().getCaseList().add(kaso.trim());
			} else {
				break;
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
