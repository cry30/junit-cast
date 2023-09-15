package io.github.roycetech.junitcast.initializer;

import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import io.github.roycetech.junitcast.AbstractTransientValueTestCase;
import io.github.roycetech.junitcast.Parameter;
import io.github.roycetech.junitcast.ParameterGenerator;
import io.github.roycetech.junitcast.ResourceFixture;
import io.github.roycetech.junitcast.initializer.CasesInitializer;

/**
 * CasesInitializer Test class.
 */
public class CasesInitializerTest
		extends AbstractTransientValueTestCase<CasesInitializer, String, String> {

	private final ResourceFixture resourceFixture = new ResourceFixture(null);
	private final ResourceFixture _resourceFixture = Mockito.spy(this.resourceFixture);

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public CasesInitializerTest(final Parameter<String> pParameter) {
		super(pParameter);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		setMockSubject(new CasesInitializer(this._resourceFixture));
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

			Mockito.doReturn(!"Without Debug Index".equals(scenario)).when(_resourceBundle)
					.containsKey(ResourceFixture.ResourceKey.debug_index.name());

			switch (scenario) {
			case "Without Debug Index":
				break;

			case "With Debug Index":
				Mockito.doReturn("1").when(getMockSubject().getResourceFixture())
						.getResourceString(ArgumentMatchers.anyString());
				break;

			case "Invalid Debug Index":
				Mockito.doReturn("xxx").when(getMockSubject().getResourceFixture())
						.getResourceString(ArgumentMatchers.anyString());
				break;
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execute()
	{
		getMockSubject().initialize();
		if (getMockSubject().getResourceFixture().getDebugStart() == 0) {
			setResult("DEBUG START 0");
		} else {
			setResult("DEBUG START NON-ZERO");
		}
	}
}