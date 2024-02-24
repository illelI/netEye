package com.neteye.services;

import com.neteye.persistence.dto.AccountUpdateDto;
import com.neteye.persistence.dto.UserDto;
import com.neteye.persistence.entities.User;
import com.neteye.persistence.repositories.UserRepository;
import com.neteye.utils.enums.AccountType;
import com.neteye.utils.exceptions.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        if (userRepository.findByEmail("admin@neteye.com").isEmpty()) {
            User admin = new User("admin", "admin", "admin@neteye.com",
                    passwordEncoder.encode("admin"));
            admin.setAccountType(AccountType.ADMIN);
            userRepository.save(admin);
        }
    }

    public User createUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Account with this email not found"));
    }

    public User updateUser(AccountUpdateDto accountDto, User existingUser) {
        if (!accountDto.getFirstName().isBlank()) {
            existingUser.setFirstName(accountDto.getFirstName());
        }
        if (!accountDto.getLastName().isBlank()) {
            existingUser.setLastName(accountDto.getLastName());
        }
        if (accountDto.getPassword().equals(accountDto.getPasswordConfirmation()) && !accountDto.getPassword().isBlank())
        {
            existingUser.setPassword(accountDto.getPassword());
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

}
