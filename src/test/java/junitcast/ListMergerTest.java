package junitcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ListMergerTest {

	/** */
	private final transient ListMerger<String> sut = new ListMerger<>();

	/**
	 * Test basic scenario.
	 */
	@Test
	public void simpleTest()
	{
		final List<String> list = new ArrayList<>(Arrays.asList("false", "true"));
		final List<List<String>> all = new ArrayList<>(Arrays.asList(list, list));
		final List<List<String>> result = sut.merge(all);

		Assert.assertEquals("[[false, false], [false, true], [true, false], [true, true]]",
				result.toString());

	}
}
