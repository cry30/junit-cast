package io.github.roycetech.junitcast.example;

/**
 * Checks if a number is prime number or not.
 */
public class PrimeNumber {


	/**
	 * Default constructor for instantiating this test class.
	 * <p>
	 * Note: This constructor does not perform any meaningful initialization.
	 */
	public PrimeNumber()
	{
	}

	/**
	 * Returns true if input is positive and is a prime number.
	 *
	 * @param input integer input.
	 * @return true if the {@code input} is deemed as prime.
	 * @throws IllegalArgumentException when the passed number below 1, which are considered invalid in this context.
	 */
	public boolean isPrimeNumber(final int input)
	{
		if (input <= 0) {
			throw new IllegalArgumentException("Invalid number: " + input);

		} else if (input == 1) {
			return false;
		}

		return computePrimeNumber(input);
	}

	/**
	 * Refactored out of {@link #isPrimeNumber(int)}. Handles the positive scenarios
	 * only.
	 *
	 * @param input integer input.
	 * @return whether the passed {@code input} is a prime number or not.
	 */
	private boolean computePrimeNumber(final int input)
	{

		for (int i = 2; i < input; i++) {
			final boolean primeYes = input % i != 0;
			if (!primeYes)
				return false;
		}
		return true;
	}

}