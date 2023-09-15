/**
 *
 */
package junitcast.initializer;

import com.github.roycetech.ruleengine.utils.StringUtil;

import junitcast.ResourceFixture;
import junitcast.ResourceFixture.ResourceKey;

/**
 * This class initializes the var parameter from the resource fixture. This was
 * refactored out of ResourceFixture.
 */
public class ExemptInitializer implements ResourceParameterInitializer {

	private final ResourceFixture resourceFixture;

	public ExemptInitializer(final ResourceFixture resourceFixture) {
		this.resourceFixture = resourceFixture;
	}

	/**
	 * Initialize the subject variables definition.
	 */
	@Override
	public void initialize()
	{
		String commonExempt = null; // NOPMD: null default, conditionally redefine.
		if (getResourceFixture().getResourceBundle().containsKey(ResourceKey.commonexempt.name())) {
			commonExempt = getResourceFixture().getResourceString(ResourceKey.commonexempt.name());
		}

		for (int i = 0; i < getResourceFixture().getCaseList().size(); i++) {
			final StringBuilder exemptRule = new StringBuilder();
			final int actualIdx = i + getResourceFixture().getDebugStart();

			final String key = String.valueOf(ResourceKey.exempt) + actualIdx;
			String caseExempt = null; // NOPMD: null default, conditionally redefine.
			if (getResourceFixture().getResourceBundle().containsKey(key)) {
				caseExempt = getResourceFixture().getResourceString(key);
			}

			if (StringUtil.hasValue(commonExempt) && StringUtil.hasValue(caseExempt)) {
				exemptRule.append(String.format("(%s)|%s", commonExempt, caseExempt));
			} else if (StringUtil.hasValue(caseExempt)) {
				exemptRule.append(caseExempt);
			} else if (StringUtil.hasValue(commonExempt)) {
				exemptRule.append(commonExempt);
			}

			if (StringUtil.hasValue(exemptRule.toString())) {
				getResourceFixture().getCaseExemptMap().put(i, exemptRule.toString());
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
