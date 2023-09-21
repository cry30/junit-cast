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

/** A sample class demonstrating multiple parameters that affect the outcome of one of its methods. */
public class Worker {

	/**
	 * Default constructor for the Worker object.
	 */
	public Worker() {}

	/** Days of the week parameters. */
	public enum Day {
		/** rtfc */
		Sunday,
		/** rtfc */
		Monday,
		/** rtfc */
		Tuesday,
		/** rtfc */
		Wednesday,
		/** rtfc */
		Thursday,
		/** rtfc */
		Friday,
		/** rtfc */
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
	 *
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