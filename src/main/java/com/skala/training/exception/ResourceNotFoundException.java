package com.skala.training.exception;

/**
 * Custom exception for resource not found scenarios
 * Demonstrates custom exception handling in Spring
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
    }
}
