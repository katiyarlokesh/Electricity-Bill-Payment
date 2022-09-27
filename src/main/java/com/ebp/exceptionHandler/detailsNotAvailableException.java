package com.ebp.exceptionHandler;

public class detailsNotAvailableException extends RuntimeException{
    private String resource;
    private String field;
    private Object object;

    public detailsNotAvailableException(String resource, String field, Object object) {
        super(String.format("%s is not found with %s %s", resource, field, object));
        this.resource = resource;
        this.field = field;
        this.object = object;
    }

    public detailsNotAvailableException(String resource, String field) {
        super(String.format("%s is not found with %s", resource, field));
        this.resource = resource;
        this.field = field;
    }

    public detailsNotAvailableException(String resource) {
        super(String.format("%s is Empty or Not available", resource));
    }


}
