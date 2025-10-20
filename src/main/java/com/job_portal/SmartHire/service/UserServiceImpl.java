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
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

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

    @Override
    public void sendResetOtp(String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+email));

        //Generate 6 digits Otp
        String otp=String.valueOf(ThreadLocalRandom.current().nextInt(1_00_000,10_00_000));

        //calculate expiry time(current time + 15 minutes in milliseconds
        long expiryTime=System.currentTimeMillis()+(15*60*1000);

        //update the profile/user
        user.setResetOtp(otp);
        user.setResetOtpExpireAt(expiryTime);

        //save into database
        userRepository.save(user);

        try{
            //send the reset otp email
            emailService.sendResetOtpEmail(user.getEmail(),otp);
        }catch (Exception e){
            throw  new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));

        if(user.getResetOtp()==null || !user.getResetOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }

        if(user.getResetOtpExpireAt() < System.currentTimeMillis()){
            throw new RuntimeException("OTP Expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setResetOtpExpireAt(0L);

        userRepository.save(user);
    }

    @Override
    public void sendOtp(String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+email));
        if(user.isAccountVerified()){
            return;
        }

        //Generate 6 digits Otp
        String otp=String.valueOf(ThreadLocalRandom.current().nextInt(1_00_000,10_00_000));

        //calculate expiry time(current time + 24 hours in milliseconds
        long expiryTime=System.currentTimeMillis()+(24 * 60 * 60 * 1000);

        //update the user
        user.setVerifyOtp(otp);
        user.setVerifyOtpExpireAt(expiryTime);

        userRepository.save(user);

        try{
            //send otp email
            emailService.sendAuthenticationOtpRequest(user.getEmail(),otp);
        }catch (Exception e){
            throw  new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+email));
        if(user.getVerifyOtp()==null || !user.getVerifyOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }

        if(user.getVerifyOtpExpireAt()<System.currentTimeMillis()){
            throw new RuntimeException("OTP Expires");
        }

        user.setAccountVerified(true);
        user.setVerifyOtp(null);
        user.setVerifyOtpExpireAt(0L);

        userRepository.save(user);
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
