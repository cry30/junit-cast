/**
 *
 */
package junitcast.example;

import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

import junitcast.AbstractTransientValueTestCase;
import junitcast.Parameter;
import junitcast.ParameterGenerator;

/**
 * Test class for Positivity.
 */
public class PositivityUsingFixedAndPairingTest
		extends AbstractTransientValueTestCase<Positivity, Integer, Integer> {

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public PositivityUsingFixedAndPairingTest(final Parameter<Integer> pParameter) {
		super(pParameter);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		setMockSubject(new Positivity());
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
		for (final Integer scenarioToken : getParameter().getScenario()) {
			setTransientValue(0, scenarioToken);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execute()
	{
		setResult(String.valueOf(getMockSubject().isPositive(getTransientValue(0))));
	}

}