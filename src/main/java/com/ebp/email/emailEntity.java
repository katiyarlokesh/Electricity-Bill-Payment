package com.ebp.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author rohit.parihar 9/15/2022
 * @Class emailEntity
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class emailEntity {
    private String subject;
    private String content;
    private String to;
}
