package com.iprody.product.service.rest.controller;

import com.iprody.product.service.exception.ResourceNotFoundException;
import com.iprody.product.service.exception.ResourceProcessingException;
import com.iprody.product.service.rest.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Error message if the resource was not found.
     */
    private static final String RESOURCE_NOT_FOUND = "Resource was not found";
    /**
     * Error message if an error occurred while processing some resource.
     */
    private static final String RESOURCE_PROCESSING_ERROR = "Error occurred during processing the resource";
    /**
     * Error message if some validation errors occurred.
     */
    private static final String VALIDATION_ERROR = "Request validation error occurred";


    /**
     * A method that catch and handles exceptions when resource not found.
     *
     * @param e expects an ResourceNotFoundException error to occur.
     * @return ResponseEntity object that contains error message, details, status code and HttpStatus - NOT_FOUND.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(ResourceNotFoundException e) {
        final ApiErrorResponse response = new ApiErrorResponse(
                RESOURCE_NOT_FOUND, Collections.singletonList(e.getMessage()),
                HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * A method that catch and handles exceptions that may have occurred while processing some resource.
     *
     * @param e expects an ResourceProcessingException error to occur.
     * @return ResponseEntity object that contains error message, details,
     * status code and HttpStatus - INTERNAL_SERVER_ERROR.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ResourceProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceProcessingException(ResourceProcessingException e) {
        final ApiErrorResponse response = new ApiErrorResponse(
                RESOURCE_PROCESSING_ERROR, Collections.singletonList(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * A method that catch and handles exceptions that may have occurred during a validation error.
     *
     * @param ex the exception to handle
     * @return ResponseEntity object that contains error message, details,
     * status code and HttpStatus - BAD_REQUEST.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleWebExchangeBindException(MethodArgumentNotValidException ex) {
        final List<String> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError ->
                        String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage())
                )
                .toList();
        final ApiErrorResponse response = new ApiErrorResponse(
                VALIDATION_ERROR, details, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(response);
    }
}
