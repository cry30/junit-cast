package junitcast.example;

import java.util.Collection;
import java.util.List;

import junitcast.AbstractTransientValueTestCase;
import junitcast.Parameter;
import junitcast.ParameterGenerator;

import org.junit.runners.Parameterized.Parameters;

/**
 * Template Test class.
 */
public class TemplateTest extends
		AbstractTransientValueTestCase<YourClass, Integer, Integer> {

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public TemplateTest(final Parameter<Integer> pParameter) {
		super(pParameter);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		setMockSubject(new YourClass());
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

	/** {@inheritDoc} */
	@Override
	protected void prepare()
	{
		for (final Integer scenarioToken : getParameter().getScenario()) {
			setTransientValue(0, scenarioToken);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execute()
	{
		try {
			setResult(String.valueOf(getMockSubject().isPrimeNumber(
				getTransientValue(0))));
		} catch (final IllegalArgumentException iae) {
			setResult("ERROR");
		}
	}

}