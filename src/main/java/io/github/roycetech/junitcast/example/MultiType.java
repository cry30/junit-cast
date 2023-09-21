package io.github.roycetech.junitcast.example;

/**
 * Demo class with different types of factors that affect the outcome. Double
 * for the passing grade and a string for the position type.
 */
public class MultiType {

	/**
	 * Default constructor for instantiating the MultiType example class.
	 * Note: This constructor does not perform any meaningful initialization.
	 */
	public MultiType()
	{
	}

	/**
	 * Position being applied for.
	 */
	public enum Position {
		/**
		 * Special role with a high passing grade requirement.
		 */
		Special(10),
		/**
		 * Regular role that has a lower passing grade.
		 */
		Regular(7);

		/**
		 * Minimum passing grade.
		 */
		private final double minPassGrade;

		/**
		 * Creates instance of this object with the
		 *
		 * @param pMinPassGrade minimum passing grade for the position.
		 */
		Position(final double pMinPassGrade)
		{
			this.minPassGrade = pMinPassGrade;
		}

		/**
		 * Retrieves the minimum grade allowed for this position.
		 *
		 * @return the minimum grade threshold.
		 */
		double getMinimumGrade()
		{
			return this.minPassGrade;
		}

	}

	/**
	 * Example method with no return, where a its result is determine through the
	 * invocation of another method.
	 *
	 * @param position Position being applied for.
	 * @param grade    from 1 to 10.
	 */
	public void applyForJob(final Position position, final double grade)
	{

		if (position == Position.Special) {
			applyForSpecial(grade);

		} else if (position == Position.Regular) {
			applyForRegular(grade);

		}

	}

	/**
	 * Refactored out of {@link #applyForJob(Position, double)} that focuses on
	 * application for a special role.
	 *
	 * @param grade achieved by this application.
	 */
	private void applyForSpecial(final double grade)
	{
		if (grade == Position.Special.getMinimumGrade()) {
			recruit();
		} else {
			reject();
		}

	}

	/**
	 * Refactored out of {@link #applyForJob(Position, double)} that focuses on
	 * application for a regular role.
	 *
	 * @param grade achieved by this application.
	 */
	private void applyForRegular(final double grade)
	{
		if (grade >= Position.Regular.getMinimumGrade()) {
			recruit();
		} else {
			reject();
		}
	}

	/**
	 * reject job application.
	 */
	void reject()
	{
		// used to demo testing of unimplemented dependency.
	}

	/**
	 * accept job application.
	 */
	void recruit()
	{
		// used to demo testing of unimplemented dependency.
	}

}
