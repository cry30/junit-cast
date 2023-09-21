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

/**
 * Allows encapsulation of preparation routine, receiving the scenario index and the token.
 *
 * @param <S> scenario parameter type. Normally String.
 */
public interface CaseObserver<S> {
	/**
	 * Prepares the case for observation. Case-specific scenario processor.
	 *
	 * @param index   scenario token index. Preferred over name so that it still
	 *                works when you have a similarly named variable tokens.
	 * @param caseRaw raw case string defined in properties file.
	 */
	void prepareCase(int index, S caseRaw);

}