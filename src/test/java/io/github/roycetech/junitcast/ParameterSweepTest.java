package io.github.roycetech.junitcast;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import io.github.roycetech.junitcast.Parameter;

public class ParameterSweepTest {

	@Test
	public void testParameter_withNewLineOnExpected()
	{
		final Parameter<String> sut = new Parameter<>("Sweep Test", Arrays.asList("Dummy Scenario"),
				"Expected\n" + "Second line", Arrays.asList("ID"));

		assertEquals("Sweep Test: Scenario[Dummy Scenario]", sut.toString());
	}

	@Test
	public void testParameter_withNewLineOnScenario()
	{
		final Parameter<String> sut = new Parameter<>("Sweep Test",
				Arrays.asList("Dummy\nScenario"), "Expected\nSecond line", Arrays.asList("ID"));

		assertEquals("Sweep Test", sut.toString());
	}
}
