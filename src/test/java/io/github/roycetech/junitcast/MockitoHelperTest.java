package io.github.roycetech.junitcast;

import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class MockitoHelperTest {

	@Test
	public void testFindConstructor_notFound()
	{
		final MockitoHelper sut = new MockitoHelper();
		assertNull(sut.findConstructor(Object.class, Arrays.asList(1, 2)));
	}

}
