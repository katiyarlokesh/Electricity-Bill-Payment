package com.ebp.exceptionHandler;

/**
 * @Author rohit.parihar 9/4/2022
 * @Class alreadyActivated
 * @Project Electricity Bill Payment
 */
public class alreadyActivated extends RuntimeException{
    private String resource;
    private Object object;

    public alreadyActivated(String resource, Object object) {
        super(String.format("%s is already Activated for Id %s", resource, object));
        this.resource = resource;
        this.object = object;
    }
}
