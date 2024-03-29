package com.neteye.persistence.entities;

import com.neteye.utils.enums.AccountType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private AccountType accountType;

    public User(String firstName, String lastName, String email, String password, AccountType accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountType = AccountType.USER;
    }

    public User() {
        this.accountType = AccountType.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (accountType) {
            case USER -> Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            case ADMIN -> Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        };
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
