package junitcast;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Template Test class.
 */
public class ResourceFixtureTest
		extends AbstractTransientValueTestCase<ResourceFixture, String, String> {

	/** Reference to the instance-shared resource bundle mock. */
	private ResourceBundle _resourceBundle;

	/**
	 * @param pParameter Data Transfer Object Parameter in Parameterized test.
	 */
	public ResourceFixtureTest(final Parameter<String> pParameter) {
		super(pParameter);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupTargetObject(final List<Object> constructorParams)
	{
		new MockitoHelper().setupTargetObject(this, Arrays.asList((String) null));
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
		this._resourceBundle = Mockito.mock(ResourceBundle.class);
		Mockito.doReturn(this._resourceBundle).when(getMockSubject()).getResourceBundle();

		for (final String scenarioToken : getParameter().getScenario()) {
			switch (getParameter().getCaseDesc()) {
			case "fetchVariables":
				prepareFetchVariables(scenarioToken);
				break;

			case "getConverter":
				prepareGetConverter(scenarioToken);
				break;
			}
		}
	}

	private void prepareFetchVariables(final String scenario)
	{
		final String testKey = "fetchTestKey";
		Mockito.doReturn("Key Found".equals(scenario)).when(this._resourceBundle)
				.containsKey(testKey);

		if ("Key Found".equals(scenario)) {
			Mockito.doReturn("Test Value").when(getMockSubject()).getResourceString(testKey);
			Mockito.doReturn(Arrays.asList(Arrays.asList("non-empty"))).when(getMockSubject())
					.extractCombinations(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(),
							ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
		}
	}

	private void prepareGetConverter(final String scenario)
	{
		if ("Non-Converter Class".equals(scenario)) {
			setTransientValue(0, "java.lang.String");
		} else if ("Converter Class".equals(scenario)) {
			setTransientValue(0, "com.github.roycetech.ruleengine.converter.StringConverter");
		} else {
			setTransientValue(0, "java.util.Unicorn");
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void execute()
	{
		switch (getParameter().getCaseDesc()) {
		case "fetchVariables":
			testFetchVariables();
			break;

		case "getConverter":
			testGetConverter();
			break;

		}
	}

	private void testFetchVariables()
	{
		final List<List<Object>> fetchResult = getMockSubject().fetchVariables(0, "fetchTestKey",
				"separator", "converters");
		setResult(fetchResult.size() == 0 ? "Empty Array" : "Non-Empty Array");
	}

	private void testGetConverter()
	{
		try {
			getMockSubject().getConverter(getTransientValue(0));
			setResult("Pass");
		} catch (final ResourceFixtureException jce) {
			setResult("Error");
		}
	}

}