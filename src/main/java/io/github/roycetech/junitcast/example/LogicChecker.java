package io.github.roycetech.junitcast.example;

/**
 * An example class for testing a method that accepts two parameters.
 */
public class LogicChecker {


	/**
	 * Default constructor for instantiating this test class.
	 * <p>
	 * Note: This constructor does not perform any meaningful initialization.
	 */
	public LogicChecker()
	{
	}

	/**
	 * Perform logical AND operation on two arguments.
	 *
	 * @param argument1 first argument.
	 * @param argument2 second argument.
	 * @return the AND result of the parameters.
	 */
	public boolean and(final boolean argument1, final boolean argument2)
	{
		return argument1 && argument2;
	}
}
