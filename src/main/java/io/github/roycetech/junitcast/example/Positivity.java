package io.github.roycetech.junitcast.example;

/**
 * A sample class for checking the positivity of integers.
 */
public class Positivity {

	/**
	 * Default constructor that provides no customization.
	 */
	public Positivity() {
	}

	/**
	 * Checks if the given integer is positive or zero.
	 *
	 * @param number The integer to check.
	 * @return {@code true} if the number is positive or zero, {@code false}
	 *         otherwise.
	 */
	boolean isPositive(final int number)
	{
		return number >= 0;
	}
}