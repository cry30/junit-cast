package io.github.roycetech.junitcast.example;

/**
 * An example class for testing that accepts two integer parameters.
 */
public class SimpleDivider {

	/**
	 * Creates instance of this example class.
	 */
	public SimpleDivider()
	{
	}

	/**
	 * Return quotient. Return 0 for -0 result.
	 *
	 * @param dividend any integer from Integer.MIN_VALUE to Integer.MAX_VALUE.
	 * @param divisor  must not be 0.
	 * @return the quotient.
	 * @throws IllegalArgumentException exception when 0 divisor is passed.
	 */
	public double divide(final int dividend, final int divisor)
	{
		if (divisor == 0) {
			throw new IllegalArgumentException("Invalid divisor.");
		}
		double retval = (double) dividend / (double) divisor;
		if (retval == -0) return 0;
		return retval;
	}
}
