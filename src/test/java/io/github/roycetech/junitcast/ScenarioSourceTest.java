package io.github.roycetech.junitcast;

import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

import io.github.roycetech.junitcast.example.PositivityTest;
import io.github.roycetech.junitcast.util.StringUtilTest;

/**
 * ScenarioSource Test class.
 */
public class ScenarioSourceTest
		extends AbstractTransientValueTestCase<ScenarioSource<String>, String, Object> {

	enum Dummy {
		One
	}

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public ScenarioSourceTest(final Parameter<String> pParameter) {
		super(pParameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		setMockSubject(new ScenarioSource<>(this));
	}

	/**
	 * <pre>
	 * Test data generator.
	 * This method is called by the JUnit parameterized test runner and
	 * returns a Collection of Arrays.  For each Array in the Collection,
	 * each array element corresponds to a parameter in the constructor.
	 * </pre>
	 */
	@Parameters(name = "{0}")
	public static Collection<Object[]> generateData()
	{
		final String currentClassName = new Object() {
		}.getClass().getEnclosingClass().getName();
		return new ParameterGenerator<String>().genVarData(currentClassName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void prepare()
	{
		for (final String scenarioToken : getParameter().getScenario()) {
			if ("findVariableEnum".equals(getParameter().getCaseDesc())) {
				if ("Not Found".equals(scenarioToken)) {
					setTransientValue(0, new PositivityTest(null));

				} else {
					setTransientValue(0, new StringUtilTest(null));
				}

			} else if ("checkValidTestCase".equals(getParameter().getCaseDesc())) {
				if ("empty".equals(scenarioToken)) {
					setTransientValue(0, new Enum[0]);

				} else if ("null_param".equals(scenarioToken)) {
					setTransientValue(0, null);

				} else {
					setTransientValue(0, new Enum[] { Dummy.One });
				}

			} else { // createNewCase
				Object valueToPass;
				if ("Case Parser".equals(scenarioToken)) {
					valueToPass = new CaseParser() {
						@Override
						public <E extends Enum<E>> Object parse(final E kaso)
						{
							/* Used the actual test token here to simplify. */
							return "Does Parse";
						}
					};

				} else {
					valueToPass = "Does Not Parse";
				}
				setTransientValue(0, valueToPass);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void execute()
	{
		if ("findVariableEnum".equals(getParameter().getCaseDesc())) {
			setResult(getMockSubject().findVariableEnum(getTransientValue(0)) == null ? null
					: "not-null");

		} else if ("checkValidTestCase".equals(getParameter().getCaseDesc())) {
			try {
//				System.out.println(String.format("Test: %s", getTransientValue(0)));
				getMockSubject().checkValidTestCase((Enum[]) getTransientValue(0));
				setResult("Good");

			} catch (final Exception e) {
				setResult("Error");
			}

		} else { // createNewCase
			final String testKey = "create-key";
			final CaseObserver<?> caseObserver = getMockSubject().createNewCase(null, testKey,
					getTransientValue(0));
			caseObserver.prepareCase(0, null);
			setResult(getMockSubject().getTestCaseTransientValue(testKey));
		}
	}
}
