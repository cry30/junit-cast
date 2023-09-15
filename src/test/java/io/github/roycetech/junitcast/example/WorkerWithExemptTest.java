package io.github.roycetech.junitcast.example;

import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import io.github.roycetech.junitcast.AbstractTestCase;
import io.github.roycetech.junitcast.MockitoHelper;
import io.github.roycetech.junitcast.Parameter;
import io.github.roycetech.junitcast.ParameterGenerator;

/**
 * Example class demonstrating the use of exempt in the configuration file.
 *
 * @author Royce Remulla
 */
public class WorkerWithExemptTest extends AbstractTestCase<Worker, String> {

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public WorkerWithExemptTest(final Parameter<String> pParameter) {
		super(pParameter);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		new MockitoHelper().setupTargetObject(this, constructorParams);
	}

	/** */
	enum Variable {
		/** */
		Is_Holiday, Regular_Day, Sunday, Monday, Tuesday, Wednesday, Thursday,
		/** */
		Friday, Saturday
	}

	/** Outputs. */
	enum Result {

		/** */
		Rest,
		/** */
		Go_to_work;

		/** */
		public String value()
		{
			return super.name().replaceAll("_", " ");
		}
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
		for (final String scenarioToken : getParameter().getScenario()) {

			final Variable variable = Variable.valueOf(scenarioToken.replaceAll(" ", "_"));

			switch (variable) {
			case Is_Holiday:
				Mockito.doReturn(true).when(getMockSubject()).isHoliday();
				break;

			case Regular_Day:
				Mockito.doReturn(false).when(getMockSubject()).isHoliday();
				break;

			default:
				final Worker.Day day = Worker.Day.valueOf(scenarioToken);
				Mockito.doReturn(day).when(getMockSubject()).getDayOfTheWeek();
				break;
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execute()
	{
		if (getMockSubject().hasWork(null)) {
			setResult(Result.Go_to_work.value());
		} else {
			setResult(Result.Rest.value());
		}
	}

}