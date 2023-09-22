package io.github.roycetech.junitcast.example;

import java.util.Collection;
import java.util.List;

import io.github.roycetech.junitcast.AbstractTransientValueTestCase;
import io.github.roycetech.junitcast.MockitoHelper;
import io.github.roycetech.junitcast.Parameter;
import io.github.roycetech.junitcast.ParameterGenerator;

import org.junit.runners.Parameterized.Parameters;

/**
 * RockPaperScissors Test class.
 */
public class RockPaperScissorsTest extends

	AbstractTransientValueTestCase<RockPaperScissors, String, String> {
	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public RockPaperScissorsTest(final Parameter<String> pParameter)
	{
		super(pParameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		new MockitoHelper().setupTargetObject(this, constructorParams);
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
		setTransientValue(0, getParameter().getScenario().get(0));
		setTransientValue(1, getParameter().getScenario().get(1));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute()
	{
		setResult(getMockSubject().battle(getTransientValue(0), getTransientValue(1)));
	}

}