package ca.ulaval.glo2003.ExceptionHandlers;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({ "error", "description" })
public class ErrorResponse {
    public String error;
    public String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}