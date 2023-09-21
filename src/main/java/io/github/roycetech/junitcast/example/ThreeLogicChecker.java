package io.github.roycetech.junitcast.example;

/**
 * An example class for testing a method that accepts three parameters.
 */
public class ThreeLogicChecker {

	/**
	 * Default constructor
	 */
	public ThreeLogicChecker()
	{
	}

	/**
	 * Applies logical first or second and third. This will evaluate as (first or (second and third)).
	 *
	 * @param first  rtfc.
	 * @param second rtfc.
	 * @param third  rtfc.
	 * @return the result of the evaluation.
	 */
	public boolean evaluate(final boolean first, final boolean second, final boolean third)
	{
		return first || second && third;
	}
}