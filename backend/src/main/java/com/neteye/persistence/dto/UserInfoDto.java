package com.neteye.persistence.dto;

import com.neteye.utils.enums.AccountType;
import lombok.Data;

@Data
public class UserInfoDto {
    public String email;
    public String firstName;
    public String lastName;
    public AccountType accountType;
}
