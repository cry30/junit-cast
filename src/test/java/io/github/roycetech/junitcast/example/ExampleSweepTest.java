package io.github.roycetech.junitcast.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * These tests are for coverage sweep only to confirm that the uncovered parts
 * of codes are intended to be not covered.
 */
public class ExampleSweepTest {

	@Test
	public void sweepIsHoliday()
	{
		final Worker sut = new Worker();
		assertFalse(sut.isHoliday());
	}

	@Test
	public void sweepGetDayOfTheWeek()
	{
		final Worker sut = new Worker();
		assertEquals(Worker.Day.Sunday, sut.getDayOfTheWeek());
	}

	@Test
	public void sweepMultiType()
	{
		final MultiType sut = new MultiType();
		sut.recruit();
		sut.reject();
		assertNotNull(sut);
	}

}
