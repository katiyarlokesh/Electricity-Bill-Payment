package com.ebp.exceptionHandler;

import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class multipleDateHandler
 * @Project Electricity Bill Payment
 */
public class multipleDateHandler extends RuntimeException{
    private String one;
    private LocalDate two;

    public multipleDateHandler(String one, LocalDate two) {
        super(String.format("%s is already present for %s", one, two));
        this.one = one;
        this.two=two;
    }
}
