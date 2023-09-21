package io.github.roycetech.junitcast.example;

import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import io.github.roycetech.junitcast.AbstractTransientValueTestCase;
import io.github.roycetech.junitcast.MockitoHelper;
import io.github.roycetech.junitcast.Parameter;
import io.github.roycetech.junitcast.ParameterGenerator;
import io.github.roycetech.junitcast.example.MultiType.Position;

/**
 * Test class for MultiType.
 */
public class MultiTypeTest extends
	AbstractTransientValueTestCase<MultiType, Object, Object> {


	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public MultiTypeTest(final Parameter<Object> pParameter)
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
		final String posStr = getParameter()
			.getScenario()
			.get(Argument.Position.ordinal())
			.toString();
		final Position position = Position.valueOf(posStr);
		final Double grade = (Double) getParameter().getScenario().get(
			Argument.Grade.ordinal());

		setTransientValue(Argument.Position.ordinal(), position);
		setTransientValue(Argument.Grade.ordinal(), grade);

		Mockito.doAnswer(new Answer<Object>() {
				@Override
				public Object answer(final InvocationOnMock invocation)
					throws Throwable
				{
					setResult("ACCEPT");
					return null;
				}
			})
			.when(getMockSubject())
			.recruit();

		Mockito.doAnswer(new Answer<Object>() {
				@Override
				public Object answer(final InvocationOnMock invocation)
					throws Throwable
				{
					setResult("DECLINE");
					return null;
				}
			})
			.when(getMockSubject())
			.reject();


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute()
	{
		final MultiType.Position position = (Position) getTransientValue(Argument.Position
			.ordinal());
		final Double grade = (Double) getTransientValue(Argument.Grade
			.ordinal());
		getMockSubject().applyForJob(position, grade);
	}

	/**
	 *
	 */
	enum Argument {
		/**
		 *
		 */
		Position, Grade
	}
}
