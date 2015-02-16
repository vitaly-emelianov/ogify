package net.ogify.backend.elements.rest;

public class ErrorResponse {
    public String error;

    public String errorDescription;

    public ErrorResponse() {
        error = "None";
        errorDescription = "Not provided";
    }

    public ErrorResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }
}
