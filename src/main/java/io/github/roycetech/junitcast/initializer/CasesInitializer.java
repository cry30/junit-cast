/**
 *
 */
package io.github.roycetech.junitcast.initializer;

import io.github.roycetech.junitcast.ResourceFixture;
import io.github.roycetech.junitcast.ResourceFixture.ResourceKey;

/**
 * This class initializes the casesdesc parameter from the resource fixture.
 * This was refactored out of ResourceFixture.
 */
public class CasesInitializer implements ResourceParameterInitializer {

	private final ResourceFixture resourceFixture;

	/**
	 * Constructs a new CasesInitializer instance.
	 *
	 * @param resourceFixture The ResourceFixture instance to initialize case
	 *                        description and starting index from.
	 */
	public CasesInitializer(final ResourceFixture resourceFixture)
	{
		this.resourceFixture = resourceFixture;
	}

	/**
	 * Initialize cases from casedesc key in property file.
	 */
	@Override
	public void initialize()
	{
		initialzeDebugIndex();
		initializeCases();
	}

	/**
	 * Initializes the debug index property of the resource fixture.
	 */
	private void initialzeDebugIndex()
	{
		if (getResourceFixture().getResourceBundle().containsKey(ResourceKey.debug_index.name())) {
			final String debugStartStr = getResourceFixture()
				.getResourceString(ResourceKey.debug_index.name()).trim();
			try {
				getResourceFixture().setDebugStart(Integer.parseInt(debugStartStr));
			} catch (final NumberFormatException e) {
				getResourceFixture().setDebugStart(0);
			}
		} else {
			getResourceFixture().setDebugStart(0);
		}
	}

	/**
	 * Initializes the cases list of the resource fixture.
	 */
	private void initializeCases()
	{
		int caseIndex = getResourceFixture().getDebugStart();
		/*
		 * 10 is a safe value, used during test where the stubbing can make the loop go
		 * on forever so this limit serves as a safety off switch.
		 */
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
