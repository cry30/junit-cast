/**
 *
 */
package junitcast.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junitcast.JUnitCastException;

/**
 * @author Royce Remulla
 *
 */
public class RuleUtil {
	/** Utility class. */
	private RuleUtil() {
	}

	/**
	 * Parse a rule definition mapping of outcome to clause from a rule definition
	 * string.
	 * 
	 * @param ruleDefinition the mapping of outcome to clause in a text form.
	 * 
	 * @return a map instance with the outcome to clause mapping.
	 */
	public static Map<String, Object> parseRuleDefinition(final String ruleDefinition)
	{
		if (ruleDefinition == null) {
			throw new JUnitCastException("Action-to-rule must not be null");
		} else if (ruleDefinition.trim().endsWith(",")) {
			throw new JUnitCastException("Invalid trailing comma detected");
		}

		final Map<String, Object> ruleMap = new LinkedHashMap<>();
		final String[] ruleArr = ruleDefinition.split("~");

		final List<String> duplicate = new ArrayList<>();
		for (final String nextRule : ruleArr) {
			if (!nextRule.contains(":")) {
				throw new JUnitCastException(
						"A colon is required to separate the outcome followed by the rule clause.");
			}

			final String[] actionClauseArr = nextRule.split(":");
			if (actionClauseArr.length > 2) {
				throw new JUnitCastException("Too many colons");
			}

			if (actionClauseArr.length == 1 || "".equals(actionClauseArr[0])) {
				throw new JUnitCastException("Invalid colon placement");
			}

			final String action = actionClauseArr[0].trim();
			if (duplicate.contains(action)) {
				throw new JUnitCastException("Duplicate outcomes detected.");
			} else {
				duplicate.add(action);
			}

			final String clause = actionClauseArr.length == 1 ? "" : actionClauseArr[1];
			ruleMap.put(action, clause.trim());
		}
		return ruleMap;
	}

}
