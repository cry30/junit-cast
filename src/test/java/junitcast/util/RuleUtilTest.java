package junitcast.util;

import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

import junitcast.AbstractTransientValueTestCase;
import junitcast.JUnitCastException;
import junitcast.Parameter;
import junitcast.ParameterGenerator;

/**
 * RuleUtil Test class.
 */
public class RuleUtilTest extends AbstractTransientValueTestCase<RuleUtil, String, String> {

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public RuleUtilTest(final Parameter<String> pParameter) {
		super(pParameter);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		setMockSubject(new RuleUtil());
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
		return new ParameterGenerator<String>().genFixedData(currentClassName);
	}

	/** {@inheritDoc} */
	@Override
	protected void prepare()
	{
		for (final String scenarioToken : getParameter().getScenario()) {
			String parameter;
			if (scenarioToken == null) {
				parameter = null;

			} else {
				switch (scenarioToken) {
				case "no colon":
					parameter = "no colon is invalid";
					break;

				case "ends with comma":
					parameter = "ends with ,";
					break;

				case "colon at the start":
					parameter = ":true | false";
					break;

				case "colon at the end":
					parameter = "Outcome Only:";
					break;

				case "too many colons":
					parameter = "one:two:three";
					break;

				case "duplicated":
					parameter = "one:uno|isa~one:eins~two:dos";
					break;

				default:
					parameter = "outcome:true | false";
					break;

				}
			}

			setTransientValue(0, parameter);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execute()
	{
		try {
			String.valueOf(RuleUtil.parseRuleDefinition(getTransientValue(0)));
			setResult("Valid");

		} catch (final JUnitCastException jce) {
			setResult("Error");
		}
	}

}