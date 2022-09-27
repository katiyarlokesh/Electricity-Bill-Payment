package com.ebp.authorizations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author rohit.parihar 9/18/2022
 * @Class forgotPassword
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class forgotPassword {
    private Long userId;
    private String username;
}
