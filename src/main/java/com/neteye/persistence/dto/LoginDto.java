package com.neteye.persistence.dto;

import com.neteye.utils.annotations.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDto {
    @ValidEmail
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
