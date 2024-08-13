package com.example.systemtaskmanagement.exeption.handling;


public class ApiMessages {

    public static final String BAD_REQUEST = "Invalid request. Please check request once more. ";
    public static final String NOT_FOUND = "Could not find requesting data ";
    public static final String INTERNAL_SERVER_ERROR = "Error occurs while exchange data with database. Please try again later, after checking given error details. ";
    public static final String ACCESS_DENIED = "This resource is forbidden for this credentials! ";
    public static final String UNAUTHORIZED = "Please login first in order to access the resource! ";


    private ApiMessages() {
    }
}
