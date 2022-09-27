package com.ebp.exceptionHandler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author rohit.parihar 9/6/2022
 * @Class methodArgumentMismatchException
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class methodArgumentMismatchException extends RuntimeException{
    private String one;
    private String two;

    public methodArgumentMismatchException(String one, String two) {
        super(String.format("%s should be of %s type", one, two));
        this.one = one;
        this.two = two;
    }
}
