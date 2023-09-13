package junitcast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

/** */
public class ScenarioSourceTest {

	/** */
	@SuppressWarnings({ "rawtypes" })
	private final transient ScenarioSource<String> sut = new ScenarioSource<>(
			new AbstractTestCase<Object, String>(null) {

				@Override
				protected void setupTargetObject(final List constructorParams)
				{
					/* Shell implementation. No need to implement. */
				}

				@Override
				protected void prepare()
				{
					/* Shell implementation. No need to implement. */
				}

				@Override
				protected void execute()
				{
					/* Shell implementation. No need to implement. */
				}
			});

	/** */
	@SuppressWarnings({ "rawtypes" })
	private final transient ScenarioSource<String> sutTrans = new ScenarioSource<>(
			new AbstractTransientValueTestCase<Object, String, Object>(null) {

				@Override
				protected void setupTargetObject(final List constructorParams)
				{
					/* Shell implementation. No need to implement. */
				}

				@Override
				protected void prepare()
				{
					/* Shell implementation. No need to implement. */
				}

				@Override
				protected void execute()
				{
					/* Shell implementation. No need to implement. */
				}
			});

	/** Null Case. */
	@SuppressWarnings("unchecked")
	@Test(expected = IllegalArgumentException.class)
	public void checkValidTestCase_nullTest()
	{
		this.sut.checkValidTestCase(null);
	}

	/** Empty Case. */
	@Test(expected = IllegalArgumentException.class)
	public void checkValidTestCase_emptyTest()
	{
		this.sut.checkValidTestCase(new TestEnum[0]);
	}

	/** Not a sub class of Transient class. */
	@Test(expected = UnsupportedOperationException.class)
	public void checkValidTestCase_nonTransientTest()
	{
		this.sut.checkValidTestCase(new TestEnum[] { TestEnum.Item1 });
	}

	/**  */
	@Test
	public void createNewCase_caseParserTest()
	{
		final CaseObserver<String> caseObs = this.sutTrans.createNewCase(null, null,
				new CaseParser() {

					@Override
					public <E extends Enum<E>> Object parse(final E kaso)
					{
						return null;
					}
				});

		caseObs.prepareCase(0, "test");
		assertNotNull(caseObs);

	}

	/**  */
	@Test
	public void test_toString()
	{
		assertEquals("Happy case", this.sut.getClass().getSimpleName() + "[] Observer size: 0",
				this.sut.toString());

	}

	/** */
	/* default */ enum TestEnum {
		/** */
		Item1
	}

}
