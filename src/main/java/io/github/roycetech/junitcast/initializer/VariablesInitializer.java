/**
 *
 */
package io.github.roycetech.junitcast.initializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.github.roycetech.junitcast.ResourceFixture;
import io.github.roycetech.junitcast.ResourceFixture.ResourceKey;

/**
 * This class initializes the var parameter from the resource fixture. This was
 * refactored out of ResourceFixture.
 */
public class VariablesInitializer implements ResourceParameterInitializer {

	private final ResourceFixture resourceFixture;

	public VariablesInitializer(final ResourceFixture resourceFixture) {
		this.resourceFixture = resourceFixture;
	}

	/**
	 * Initialize the subject variables definition.
	 */
	@Override
	public void initialize()
	{
		List<List<Object>> commonVars;
		if (getResourceFixture().getResourceBundle()
				.containsKey(ResourceFixture.ResourceKey.commonvar.name())) {
			commonVars = getResourceFixture().fetchVariables(-1,
					ResourceFixture.ResourceKey.commonvar.name(), ",", null);
		} else {
			commonVars = new ArrayList<>();
		}

		for (int i = 0; i < getResourceFixture().getCaseList().size(); i++) {
			final int actualIdx = i + getResourceFixture().getDebugStart();
			final String varkey = ResourceFixture.ResourceKey.var.name() + actualIdx;
			final String convertkey = ResourceKey.converter.name() + actualIdx;

			String converters = null; // NOPMD: null default, conditionally redefine.
			if (getResourceFixture().getResourceBundle().containsKey(convertkey)) {
				converters = getResourceFixture().getResourceString(convertkey);
			}

			getResourceFixture().getRuleTokenConverter().add(new HashMap<>());
			final List<List<Object>> caseVariables = getResourceFixture().fetchVariables(i, varkey,
					",", converters);
			final Set<List<Object>> specificVars = new LinkedHashSet<>();
			caseVariables.addAll(commonVars);
			caseVariables.addAll(specificVars);
			getResourceFixture().getCaseVarList().add(caseVariables);
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
