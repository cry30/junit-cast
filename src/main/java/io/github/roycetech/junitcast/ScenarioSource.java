/**
 *   Copyright 2014 Royce Remulla
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
package io.github.roycetech.junitcast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Scenario observable.
 *
 * <pre>
 * &#64;author $Author$
 * @version $Date$
 * </pre>
 *
 * @param <S> data type of scenario element. Use object if scenario contain
 *            multiple types.
 */
public class ScenarioSource<S> {

	/** */
	private final transient AbstractTestCase<?, S> testCase;

	/** */
	@SuppressWarnings("rawtypes")
	private final transient Map<Enum, List<CaseObserver<S>>> enumObsMap = new ConcurrentHashMap<>();

	/** */
	@SuppressWarnings("rawtypes")
	private final transient Class<? extends Enum> enumType;

	/**
	 * By convention, accessible Variable enum defined on the test class.
	 *
	 * @param pTestCase the test class usually "this". Not null.
	 */
	public ScenarioSource(final AbstractTestCase<?, S> pTestCase) {
		this(pTestCase, pTestCase);
	}

	/**
	 * By convention, accessible Variable enum defined on the test class.
	 *
	 * @param pTestCase the test class usually "this". Not null.
	 */
	public ScenarioSource(final AbstractTestCase<?, S> pTestCase, final Object _pVarSource) {
		this.testCase = pTestCase;
//		this.enumType = findVariableEnum(pVarSource == null ? pTestCase : _pVarSource);
		this.enumType = findVariableEnum(pTestCase);
	}

	/**
	 * rtfc.
	 *
	 * @param pTestCase Not null.
	 * @return null when Variable enum is not found.
	 */
	@SuppressWarnings("unchecked")
	/* default */ final Class<? extends Enum<?>> findVariableEnum(final Object pVarSource)
	{
		for (final Class<?> innerClass : pVarSource.getClass().getDeclaredClasses()) {
			if (innerClass.getSimpleName().startsWith("Var")) {
				return (Class<? extends Enum<?>>) innerClass;
			}
		}
		return null;
	}

	/**
	 * Convenience method to set transient value on a test case.
	 *
	 * @param key        transient key/name.
	 * @param caseParser case parser instance. Must not be null.
	 * @param cases      applicable Variable cases.
	 *
	 * @param <C>        case enum.
	 * @param <T>        transient key.
	 */
	@SuppressWarnings("unchecked")
	public <C extends Enum<C>, T> void addTransientCase(final T key, final CaseParser caseParser,
			final C... cases)
	{
		addTransientCase(key, (Object) caseParser, cases);
	}

	/**
	 * Convenience method to set transient value on a test case using the case name.
	 *
	 * @param key   transient key/name.
	 * @param cases applicable Variable cases.
	 *
	 * @param <C>   case enum.
	 * @param <T>   transient key.
	 */
	@SuppressWarnings("unchecked")
	public <C extends Enum<C>, T> void addTransientCaseName(final T key, final C... cases)
	{
		checkValidTestCase(cases);
		for (final C nextCase : cases) {
			if (this.testCase.getParameter().getScenario().contains(nextCase.name())) {
				addTransientCase(key, nextCase.name(), cases);
			}
		}
	}

	/**
	 * Convenience method to set transient value on a test case.
	 *
	 * @param key   transient key/name.
	 * @param value transient value to set.
	 * @param cases applicable Variable cases.
	 *
	 * @param <C>   case enum.
	 * @param <T>   transient key.
	 */
	@SuppressWarnings("unchecked")
	public <C extends Enum<C>, T> void addTransientCase(final T key, final Object value,
			final C... cases)
	{
		for (final C nextCase : cases) {
			addObserver(nextCase, createNewCase(nextCase, key, value));
		}
	}

	/**
	 * Creates a new CaseObserver for the given case, key, and value.
	 *
	 * @param <T>      The type of the key.
	 * @param <C>      The enum type for the cases.
	 * @param nextCase The next case (enum value) to observe.
	 * @param key      The key associated with the observation.
	 * @param value    The value associated with the observation.
	 * @return A new CaseObserver instance.
	 */
	/* default */ <T, C extends Enum<C>> CaseObserver<S> createNewCase(final C nextCase,
			final T key, final Object value)
	{
		return new CaseObserver<>() {

			@Override
			public void prepareCase(final int index, final S caseRaw)
			{
				Object valueCalc;
				if (value instanceof CaseParser) {
					final CaseParser caseParser = (CaseParser) value;
					valueCalc = caseParser.parse(nextCase);
				} else {
					valueCalc = value;
				}
				@SuppressWarnings("unchecked")
				final AbstractTransientValueTestCase<?, S, Object> transCase = (AbstractTransientValueTestCase<?, S, Object>) ScenarioSource.this.testCase;
				transCase.setTransientValue(key, valueCalc);
			}
		};
	}

	/**
	 * Helper method for #addTransientCase(). Checks if test case supports the
	 * functionality.
	 */
	/* default */ <C extends Enum<C>> void checkValidTestCase(
			@SuppressWarnings("unchecked") final C... cases)
	{
		if (cases == null || cases.length == 0) {
			throw new IllegalArgumentException("Must have at least one valid case.");
		}
	}

	/**
	 *
	 * @param kaso     enum case.
	 * @param observer case observer instance.
	 */
	public void addObserver(final Enum<?> kaso, final CaseObserver<S> observer)
	{
		if (this.enumObsMap.get(kaso) == null) {
			this.enumObsMap.put(kaso, new ArrayList<>());
		}
		final List<CaseObserver<S>> caseObsList = this.enumObsMap.get(kaso);
		caseObsList.add(observer);
	}

	/** Notify all case observers. */
	public void notifyObservers()
	{

		for (int i = 0; i < this.testCase.getParameter().getScenario().size(); i++) {
			final S nextCase = this.testCase.getParameter().getScenario().get(i);

			@SuppressWarnings("unchecked")
			final Enum<?> nextEnum = Enum.valueOf(this.enumType,
					nextCase.toString().replaceAll(" ", ""));

			final List<CaseObserver<S>> caseObserverList = this.enumObsMap.get(nextEnum);

			prepareObserver(nextCase, i, caseObserverList);
		}

		this.enumObsMap.clear();
	}

	/**
	 * Refactored out of #notifyObservers.
	 * 
	 * @param caseObsList the observers list.
	 */
	private void prepareObserver(final S nextCase, final int scenarioIndex,
			final List<CaseObserver<S>> caseObsList)
	{
		if (caseObsList != null) {
			for (final CaseObserver<S> nextCaseObserver : caseObsList) {
				nextCaseObserver.prepareCase(scenarioIndex, nextCase);
			}
		}

	}

	/**
	 * Used for testing only.
	 * 
	 * @return the testCase transient value at the given key.
	 */
	/* default */ Object getTestCaseTransientValue(final String key)
	{
		return ((AbstractTransientValueTestCase<?, S, ?>) this.testCase).getTransientValue(key);
	}

//	Used for debugging only.
//	/** {@inheritDoc} */
//	@Override
//	public String toString()
//	{
//		return getClass().getSimpleName() + "[" + this.testCase.getClass().getSimpleName()
//				+ "] Observer size: " + this.enumObsMap.size();
//	}

}