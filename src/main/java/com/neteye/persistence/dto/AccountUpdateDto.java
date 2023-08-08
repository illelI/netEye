package com.neteye.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateDto {
    private String firstName;
    private String lastName;
    private String password;
    private String passwordConfirmation;
}
