package com.neteye.persistence.entities;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.neteye.utils.enums.AccountType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Table
public class User implements UserDetails {
    @PrimaryKey
    @Getter @Setter
    private UUID id;
    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;
    @Indexed
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private AccountType accountType;
    @Setter
    private boolean isEnabled;

    public User(String firstName, String lastName, String email, String password, AccountType accountType) {
        this.id = Uuids.timeBased();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.isEnabled = false;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.id = Uuids.timeBased();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountType = AccountType.USER;
        this.isEnabled = false;
    }

    public User() {
        this.id = Uuids.timeBased();
        this.accountType = AccountType.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
