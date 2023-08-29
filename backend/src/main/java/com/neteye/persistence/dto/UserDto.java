package com.neteye.persistence.dto;

import com.neteye.utils.annotations.PasswordConfirmation;
import com.neteye.utils.annotations.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PasswordConfirmation
public class UserDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;
    private String passwordConfirmation;

    @NotBlank
    @ValidEmail
    private String email;
}
