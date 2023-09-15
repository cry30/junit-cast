package io.github.roycetech.junitcast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import com.github.roycetech.ruleengine.Rule;
import com.github.roycetech.ruleengine.RuleEvaluator;
import com.github.roycetech.ruleengine.RuleProcessor;
import com.google.common.collect.Lists;

/**
 * Implementation for junit4 parameterized test generateData static method.
 *
 * @param <T> scenario element type.
 */
public class ParameterGenerator<T> {

	/**
	 * Generate parameters from set of variables. This will calculate every possible
	 * combination minus any defined exemption.
	 *
	 * @param resourceUri resource bundle URI.
	 */
	public Collection<Object[]> genVarData(final String resourceUri)
	{
		final ResourceFixture resFixFactory = new ResourceFixture(resourceUri);
		@SuppressWarnings("unchecked")
		final List<CaseFixture<T>> fixtures = (List<CaseFixture<T>>) resFixFactory.getFixtures();
		return generateData(fixtures);
	}

	/**
	 * Generate parameters from a fixed set of test data.
	 *
	 * @param resourceUri resource bundle URI.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Object[]> genFixedData(final String resourceUri)
	{
		final ResourceFixture resFixFactory = new ResourceFixture(resourceUri);
		return generateData((List<CaseFixture<T>>) resFixFactory.getFixtures(), false);
	}

	/**
	 * @param fixTureList list of test cases.
	 */
	public Collection<Object[]> generateData(final List<CaseFixture<T>> fixTureList)
	{
		return generateData(fixTureList, true);
	}

	/**
	 * @param fixTureList list of test cases.
	 * @param isComputed  false when data is fixed list other wise it is the
	 *                    combination of all variables.
	 */
	public Collection<Object[]> generateData(final List<CaseFixture<T>> fixTureList,
			final boolean isComputed)
	{
		final List<Object[]> retval = new ArrayList<>();
		for (final CaseFixture<T> caseFixture : fixTureList) {
			if (isComputed) {
				addCase(retval, caseFixture);
			} else {
				addFixedCase(retval, caseFixture);
			}
		}

		if (isComputed) {
			Collections.sort(retval, new Comparator<>() {
				@Override
				@SuppressWarnings({ "PMD.UseVarargs", "unchecked" })
				public int compare(final Object[] paramArr1, final Object[] paramArr2)
				{
					final Parameter<T> param1 = (Parameter<T>) paramArr1[0];
					final Parameter<T> param2 = (Parameter<T>) paramArr2[0];
					return param1.toString().compareTo(param2.toString());
				}
			});
		}
		return retval;
	}

	/**
	 * Add a case in a parameterized test case.
	 *
	 * @param paramCollection the List of Object array in parameterized test.
	 * @param caseFixture     the case fixture.
	 */
	private void addCase(final List<Object[]> paramCollection, final CaseFixture<T> caseFixture)
	{
		final List<List<T>> combinations = Lists.cartesianProduct(caseFixture.getVariables());
		for (final List<T> scenario : combinations) {
			if (isValidCase(scenario, caseFixture)) {
				final String result = validateRule(scenario, caseFixture);
				paramCollection.add(new Object[] { new Parameter<>(caseFixture.getCaseDesc(),
						scenario, result, caseFixture.getCaseId()) });
			}

		}
	}

	/**
	 * Add a case in a parameterized test case.
	 *
	 * @param paramCollection the List of Object array in parameterized test.
	 * @param caseFixture     the case fixture.
	 */
	@SuppressWarnings("unchecked")
	private void addFixedCase(final List<Object[]> paramCollection,
			final CaseFixture<T> caseFixture)
	{
		for (final List<T> scenario : caseFixture.getVariables()) {
			@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
			final List<?> scenList = new ArrayList<>(scenario);

			final String matched = caseFixture.getRuleOutcome((List<String>) scenario);

			String result;
			if (matched == null) {

//				assert !caseFixture.getPairMap().isEmpty()
//						: "Unmatched result is applicable only to binary result";

				result = caseFixture.getPairMap().values().iterator().next();
			} else {
				result = matched;
			}
			Assert.assertNotNull("Scenario must fall into a unique rule output/clause: " + scenario
					+ " did not match", result);

			paramCollection.add(new Object[] { new Parameter<>(caseFixture.getCaseDesc(),
					(List<T>) scenList, result, caseFixture.getCaseId()) });
		}
	}

	/**
	 * Assertion exception when rule failed. Otherwise the single rule that
	 * succeeded is returned.
	 *
	 * @param scenario current Test scenario.
	 * @param fixture  test fixture.
	 */
	public String validateRule(final List<T> scenario, final CaseFixture<T> fixture)
	{
		final Rule rule = fixture.getRule();
		String retval = null; // NOPMD: null default, conditionally redefine.
		@SuppressWarnings("unchecked")
		final List<Object> scenarioObjects = (List<Object>) scenario;
		final Boolean[] ruleResult = new RuleProcessor(rule, fixture.getRuleConverter())
				.evaluate(scenarioObjects);
		final List<String> outcomes = new ArrayList<>(rule.getOutcomes());
		final boolean singleResult = rule.getOutcomes().size() == 1;
		if (singleResult) {
			final boolean nextResult = ruleResult[0];
			final String action = outcomes.get(0);
			return getBinaryOutput(action, fixture, nextResult);
		}

		final Set<String> matchedOutputs = new HashSet<>();
		int matchCount = 0; // NOPMD: counter, redefined inside loop.
		for (int i = 0; i < ruleResult.length; i++) {
			if (ruleResult[i]) {
				matchCount++; // NOPMD: counter, redefined inside loop.
				retval = outcomes.get(i);
				matchedOutputs.add(retval);
			}
		}

		Assert.assertEquals("Scenario must fall into a unique rule output/clause: " + scenario
				+ ", matched: " + matchedOutputs, 1, matchCount);
		return retval;
	}

	/**
	 *
	 * @param ruleOutput output default output specified by the binary rule.
	 * @param fixture    test case fixture.
	 * @param expected   expected output.
	 */
	/* default */ String getBinaryOutput(final String ruleOutput, final CaseFixture<T> fixture,
			final boolean expected)
	{
		final boolean isNegative = fixture.getPairMap().containsKey(ruleOutput);
		if (isNegative) {
			if (expected) {
				return ruleOutput;
			}

			return getOpposite(ruleOutput, fixture);
		}

		if (expected) {
			return ruleOutput;
		}

		return getOpposite(ruleOutput, fixture);
	}

	/**
	 * @param ruleOutput output default output specified by the binary rule.
	 * @param fixture    test case fixture.
	 */
	/* default */ String getOpposite(final String ruleOutput, final CaseFixture<T> fixture)
	{
		if (fixture.getPairMap().get(ruleOutput) == null) {
			return fixture.getReversePairMap().get(ruleOutput);
		}

		return fixture.getPairMap().get(ruleOutput);
	}

	/**
	 * Custom invalid, and for UI null facility status is unexpected.
	 *
	 * @param scenario current Test scenario.
	 * @param fixture  test case fixture.
	 */
	private boolean isValidCase(final List<T> scenario, final CaseFixture<T> fixture)
	{
		if (fixture.getExemptRule() == null) {
			return true;
		}

		final String exemptRule = fixture.getExemptRule();
		final RuleEvaluator ruleEvaluator = new RuleEvaluator(fixture.getRuleConverter());
		ruleEvaluator.parse(exemptRule);
		return !ruleEvaluator.evaluate((List<Object>) scenario);
	}
}
