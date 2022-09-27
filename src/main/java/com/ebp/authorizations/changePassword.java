package com.ebp.authorizations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author rohit.parihar 9/18/2022
 * @Class changePassword
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class changePassword {
    private String oldPassword;
    private String newPassword;
}
