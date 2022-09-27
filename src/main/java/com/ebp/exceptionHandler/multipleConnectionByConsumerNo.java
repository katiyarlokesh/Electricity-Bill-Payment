package com.ebp.exceptionHandler;

/**
 * @Author rohit.parihar 9/4/2022
 * @Class multipleConnectionByConsumerNo
 * @Project Electricity Bill Payment
 */
public class multipleConnectionByConsumerNo extends RuntimeException{
    private String one;
    private String two;
    private Object object;

    public multipleConnectionByConsumerNo(String one, String two, Object object) {
        super(String.format("Multiple %s found with %s %s. Please contact admin for Resolution", one, two, object));
        this.one = one;
        this.two = two;
        this.object = object;
    }
}
