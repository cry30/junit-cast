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
package io.github.roycetech.junitcast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

/**
 * Helper class for Mockito test subclasses. You need to reference Mockito
 * library 1.8+ if you want to subclass this.
 */
public class MockitoHelper {

	/**
	 * Default constructor with doesn't do any customization.
	 */
	public MockitoHelper() {
	}

	/**
	 * Sets up the test subject by inspecting the defined class types, and creating
	 * the spy object for easy stubbing of dependent components.
	 *
	 * @param <T>               Test Object instance type. Does not support generic
	 *                          types, you can omit the generic argument of test
	 *                          subject type. Must not be null.
	 *
	 * @param testCase          Test case instance.
	 * @param constructorParams test subject constructor parameters.
	 */
	public <T> void setupTargetObject(final AbstractTestCase<T, ?> testCase,
			final List<Object> constructorParams)
	{
		try {

			final Constructor<T> constructor = computeConstructor(testCase, constructorParams);
			constructor.setAccessible(true);
			T realSubject;
			if (constructorParams == null) {
				realSubject = constructor.newInstance();
			} else {
				final int parameterCount = constructorParams.size();
				final Object[] constructorParamsArray = constructorParams
						.toArray(new Object[parameterCount]);
				realSubject = constructor.newInstance(constructorParamsArray);
			}
			testCase.setRealSubject(realSubject);
			testCase.setMockSubject(Mockito.spy(testCase.getRealSubject()));
		} catch (final InvocationTargetException | IllegalArgumentException | InstantiationException
				| IllegalAccessException any) {
			throw new JUnitCastException(any);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> Constructor<T> computeConstructor(final AbstractTestCase<T, ?> testCase,
			final List<Object> constructorParameters)
	{
		final List<Object> computedParameters = constructorParameters == null ? new ArrayList<>()
				: constructorParameters;
		if (computedParameters.isEmpty()) {
			return (Constructor<T>) testCase.getSubjectType().getDeclaredConstructors()[0];
		}

		return (Constructor<T>) findConstructor(testCase.getSubjectType(), computedParameters);
	}

	/**
	 * Auto resolve constructor based on List of Object parameter.
	 *
	 * @param klazz   class to derive constructor from.
	 * @param pParams List of Object parameters.
	 */
	Constructor<?> findConstructor(final Class<?> klazz, final List<Object> pParams)
	{
		for (final Constructor<?> nextConst : klazz.getDeclaredConstructors()) {
			if (nextConst.getParameterTypes().length == pParams.size()) {
				return nextConst;
			}
		}

		return null;
	}
}
