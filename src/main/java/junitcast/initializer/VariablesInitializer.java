/**
 * 
 */
package junitcast.initializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junitcast.ResourceFixture;
import junitcast.ResourceFixture.ResourceKey;

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
		if (this.resourceFixture.getResourceBundle()
				.containsKey(ResourceFixture.ResourceKey.commonvar.name())) {
			commonVars = getResourceFixture().fetchVariables(-1,
					ResourceFixture.ResourceKey.commonvar.name(), ",", null);
		} else {
			commonVars = new ArrayList<>();
		}

		for (int i = 0; i < this.resourceFixture.getCaseList().size(); i++) {
			final int actualIdx = i + getResourceFixture().getDebugStart();
			final String varkey = ResourceFixture.ResourceKey.var.name() + actualIdx;
			final String convertkey = ResourceKey.converter.name() + actualIdx;

			String converters = null; // NOPMD: null default, conditionally redefine.
			if (getResourceFixture().getResourceBundle().containsKey(convertkey)) {
				converters = this.resourceFixture.getResourceString(convertkey);
			}

			this.resourceFixture.getRuleTokenConverter().add(new HashMap<>());
			final List<List<Object>> caseVariables = this.resourceFixture.fetchVariables(i, varkey,
					",", converters);
			final Set<List<Object>> specificVars = new LinkedHashSet<>();
			caseVariables.addAll(commonVars);
			caseVariables.addAll(specificVars);
			this.resourceFixture.getCaseVarList().add(caseVariables);
		}
	}

	/**
	 * Returns the bound resourceFixture for testability.
	 * 
	 * @return the bound resourceFixture instance.
	 */
	/* default */ ResourceFixture getResourceFixture()
	{
		return this.resourceFixture;
	}
}
