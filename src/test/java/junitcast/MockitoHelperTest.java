package junitcast;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/** JUnit test for MockitoHelper. */
public class MockitoHelperTest {

	/** Test the constructor not found scenario. */
	@Test(expected = JUnitCastException.class)
	public void testSetupTargetObject_constructorNotFound()
	{
		/** Anonymous class as the test data for the sut. */
		class UnicornTest extends AbstractTestCase<Object, Object> {

			/** Default constructor. */
			protected UnicornTest(final Parameter<Object> pParameter) {
				super(pParameter);
			}

			@Override
			protected void setupTargetObject(final List<Object> constructorParams)
			{
				/** No implementation */
			}

			@Override
			protected void prepare()
			{
				/* No implementation */
			}

			@Override
			protected void execute()
			{
				/* No implementation */
			}
		}
		final MockitoHelper sut = new MockitoHelper();
		sut.setupTargetObject(new UnicornTest(null), Arrays.asList(1, 2, 3));

	}

}
