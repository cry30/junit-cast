package io.github.roycetech.junitcast.example;

/**
 * A sample class demonstrating multiple parameters that affect the outcome of one of its methods.
 */
public class Worker {

	/**
	 * Default constructor for the Worker object.
	 */
	public Worker()
	{
	}

	/**
	 * Days of the week parameters.
	 */
	public enum Day {
		/**
		 * rtfc
		 */
		Sunday,
		/**
		 * rtfc
		 */
		Monday,
		/**
		 * rtfc
		 */
		Tuesday,
		/**
		 * rtfc
		 */
		Wednesday,
		/**
		 * rtfc
		 */
		Thursday,
		/**
		 * rtfc
		 */
		Friday,
		/**
		 * rtfc
		 */
		Saturday
	}


	/**
	 * True if all of these rules are satisfied:
	 * <ul>
	 * 	<li>Monday to Saturday.
	 *  <li>Not Holiday
	 * </ul>
	 *
	 * @param context dummy session context.
	 * @return true if worker should report to work today.
	 */
	public boolean hasWork(final Object context)
	{
		return !isHoliday() && Day.Sunday != getDayOfTheWeek();
	}

	/**
	 * Intentionally unimplemented.
	 *
	 * @return if today is a holiday.
	 */
	public boolean isHoliday()
	{
		return false;
	}

	/**
	 * Intentionally unimplemented.
	 *
	 * @return the current day of the week.
	 */
	public Day getDayOfTheWeek()
	{
		return Day.Sunday;
	}

}