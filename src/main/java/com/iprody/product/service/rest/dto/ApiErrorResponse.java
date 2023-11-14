package com.iprody.product.service.rest.dto;

import java.util.List;

/**
 * The class is used to create an object that contains all info about an error.
 *
 * @param message The error message.
 * @param details The list of error details.
 * @param status  HTTP Status (e. g. 400, 404, 500).
 */
public record ApiErrorResponse(String message, List<String> details, int status) {
}
