package junitcast.initializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import junitcast.AbstractTransientValueTestCase;
import junitcast.Parameter;
import junitcast.ParameterGenerator;
import junitcast.ResourceFixture;

/**
 * VariablesInitializer Test class.
 */
public class ExemptInitializerTest
		extends AbstractTransientValueTestCase<ExemptInitializer, String, String> {

	private final ResourceFixture resourceFixture = new ResourceFixture(null);
	private final ResourceFixture _resourceFixture = Mockito.spy(this.resourceFixture);

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public ExemptInitializerTest(final Parameter<String> pParameter) {
		super(pParameter);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		setMockSubject(new ExemptInitializer(this._resourceFixture));
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
		Mockito.doReturn(new HashSet<String>(Set.of("Case 1"))).when(this._resourceFixture)
				.getCaseList();

		for (final String scenario : getParameter().getScenario()) {
			switch (scenario) {
			case "With Common Exempt":
				Mockito.doReturn(true).when(_resourceBundle)
						.containsKey(ResourceFixture.ResourceKey.commonexempt.name());
				Mockito.doReturn("Common Exempt Sample").when(this._resourceFixture)
						.getResourceString(ResourceFixture.ResourceKey.commonexempt.name());
				break;

			case "With Exempt":
				Mockito.doReturn(true).when(_resourceBundle)
						.containsKey(ResourceFixture.ResourceKey.exempt.name() + "0");
				Mockito.doReturn("Exempt Sample").when(this._resourceFixture)
						.getResourceString(ResourceFixture.ResourceKey.exempt.name() + "0");
				break;
			}

		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execute()
	{
		getMockSubject().initialize();
		final String actual = getMockSubject().getResourceFixture().getCaseExemptMap().get(0);
		if (actual == null) {
			setResult("None");
		} else {
			setResult(actual);
		}
	}
}