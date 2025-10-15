package com.job_portal.SmartHire.service;

import com.job_portal.SmartHire.dto.UserRequest;
import com.job_portal.SmartHire.dto.UserResponse;
import com.job_portal.SmartHire.entity.Role;
import com.job_portal.SmartHire.entity.User;
import com.job_portal.SmartHire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest request) {
        if(!userRepository.existsByEmail(request.getEmail())) {
            User user = convertToUser(request);
            user = userRepository.save(user);
            return convertToUserResponse(user);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT," email already exists");
    }

    @Override
    public UserResponse getUser(String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+ email));
        return convertToUserResponse(user);
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .userId(user.getUserId())
                .isAccountVerified(user.isAccountVerified())
                .build();
    }

    private User convertToUser(UserRequest request) {
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.APPLICANT); // Default role if not provided
        user.setEnabled(true);
        user.setAccountVerified(false);
        user.setResetOtpExpireAt(0L);
        user.setVerifyOtp(null);
        user.setVerifyOtpExpireAt(0L);
        user.setResetOtp(null);

        return user;
    }

}
