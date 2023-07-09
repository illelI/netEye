package com.neteye.persistence.dto;

import com.neteye.utils.annotations.PasswordConfirmation;
import com.neteye.utils.annotations.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@PasswordConfirmation
public class UserDto {
    @NotNull
    @NotEmpty
    @Getter @Setter
    private String firstName;

    @NotNull
    @NotEmpty
    @Getter @Setter
    private String lastName;

    @NotNull
    @NotEmpty
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String passwordConfirmation;

    @NotNull
    @NotEmpty
    @ValidEmail
    @Getter @Setter
    private String email;
}
