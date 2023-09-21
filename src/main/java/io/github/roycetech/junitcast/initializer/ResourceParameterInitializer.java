package io.github.roycetech.junitcast.initializer;

/**
 * An interface for classes responsible for initializing resource parameters.
 * Implementing classes must provide an implementation of the {@code initialize} method.
 */
public interface ResourceParameterInitializer {

	/** Perform the initialization. */
	void initialize();
}
