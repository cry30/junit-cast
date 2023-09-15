package io.github.roycetech.junitcast.initializer;

import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import io.github.roycetech.junitcast.AbstractTransientValueTestCase;
import io.github.roycetech.junitcast.Parameter;
import io.github.roycetech.junitcast.ParameterGenerator;
import io.github.roycetech.junitcast.ResourceFixture;

/**
 * VariablesInitializer Test class.
 */
public class VariablesInitializerTest
		extends AbstractTransientValueTestCase<VariablesInitializer, String, String> {

	private final ResourceFixture resourceFixture = new ResourceFixture(null);
	private final ResourceFixture _resourceFixture = Mockito.spy(this.resourceFixture);

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public VariablesInitializerTest(final Parameter<String> pParameter) {
		super(pParameter);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		setMockSubject(new VariablesInitializer(this._resourceFixture));
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
		final ResourceBundle _resourceBundle = Mockito.mock(ResourceBundle.class);
		Mockito.doReturn(_resourceBundle).when(this._resourceFixture).getResourceBundle();

		for (final String scenario : getParameter().getScenario()) {
			Mockito.doReturn("With Common Var".equals(scenario)).when(_resourceBundle)
					.containsKey(ResourceFixture.ResourceKey.commonvar.name());

			switch (scenario) {
			case "With Common Var":
				Mockito.doReturn("dummy common").when(this._resourceFixture)
						.getResourceString(ResourceFixture.ResourceKey.commonvar.name());

				Mockito.doAnswer(new Answer<>() {
					@Override
					public Object answer(final InvocationOnMock invocation) throws Throwable
					{
						setResult("Fetches commonvar");
						return null;
					}
				}).when(this._resourceFixture).fetchVariables(-1,
						ResourceFixture.ResourceKey.commonvar.name(), ",", null);
				break;
			}

		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execute()
	{
		setResult("Does not fetch commonvar");
		getMockSubject().initialize();
	}
}