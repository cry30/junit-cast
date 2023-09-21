/**
 *   Copyright 2013 Royce Remulla
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
