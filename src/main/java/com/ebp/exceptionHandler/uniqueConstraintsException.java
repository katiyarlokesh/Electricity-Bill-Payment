package com.ebp.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author rohit.parihar 9/6/2022
 * @Class uniqueConstraintsException
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class uniqueConstraintsException extends RuntimeException{
    private String one;
    private String two;

    public uniqueConstraintsException(String one, String two) {
        super(String.format("Please check your %s & %s again", one, two));
        this.one=one;
        this.two=two;
    }
}
