package com.ebp.dataTransfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @Author rohit.parihar 9/17/2022
 * @Class userClone
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class userClone {
    private Long userId;
    @NotEmpty
    @Size(min = 8, message = "Username must be greater than or equal to 8 Characters")
    private String username;

    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, message = "Password must be greater than 8 Characters")
    private String password;
}
