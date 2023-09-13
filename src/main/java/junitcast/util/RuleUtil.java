/**
 * 
 */
package junitcast.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Royce Remulla
 *
 */
public class RuleUtil {
	/** Utility class. */
	private RuleUtil() {
	}

	public static Map<String, Object> parseRuleDefinition(final String ruleDefinition)
	{
		final IllegalArgumentException exception = new IllegalArgumentException("Invalid null action to rule source.");
		if (ruleDefinition == null || ruleDefinition.trim().endsWith(",")) {
			throw exception;
		}

		final Map<String, Object> ruleMap = new LinkedHashMap<String, Object>();
		final String[] ruleArr = ruleDefinition.split("~");

		final List<String> duplicate = new ArrayList<String>();
		for (final String nextRule : ruleArr) {
			final String[] actionClauseArr = nextRule.split(":");
			if (ruleArr.length > 1 && (actionClauseArr.length < 2 || "".equals(actionClauseArr[1]))) {
				throw exception;
			}

			final String action = actionClauseArr[0].trim();
			if (duplicate.contains(action)) {
				throw exception;
			} else {
				duplicate.add(action);
			}

			final String clause = actionClauseArr.length == 1 ? "" : actionClauseArr[1];
			ruleMap.put(action, clause.trim());
		}
		return ruleMap;
	}

}
