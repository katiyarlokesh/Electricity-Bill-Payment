package com.ebp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class Role
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Role {

    @Id
    private Integer id;
    private String role;
}
