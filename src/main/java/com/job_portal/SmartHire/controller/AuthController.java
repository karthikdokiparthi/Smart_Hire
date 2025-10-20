package com.job_portal.SmartHire.controller;

import com.job_portal.SmartHire.dto.AuthRequest;
import com.job_portal.SmartHire.dto.AuthResponse;
import com.job_portal.SmartHire.dto.ResetPasswordRequest;
import com.job_portal.SmartHire.service.AppUserDetailsService;
import com.job_portal.SmartHire.service.UserService;
import com.job_portal.SmartHire.service.UserServiceImpl;
import com.job_portal.SmartHire.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private final AppUserDetailsService userDetailsService;

    @Autowired
    private final UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/test")
    public String test(){
        return "Hi Authentication is working";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        try{
            authenticate(request.getEmail(),request.getPassword());
            final UserDetails userDetails=userDetailsService.loadUserByUsername(request.getEmail());
            final String jwtToken=jwtUtil.generateToken(userDetails);
            ResponseCookie cookie=ResponseCookie.from("jwt",jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                    .body(new AuthResponse(request.getEmail(),jwtToken));

        }catch (BadCredentialsException e){
            Map<String ,Object> error=new HashMap<>();
            error.put("error",true);
            error.put("message","email or password is wrong");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }catch (DisabledException e){
            Map<String ,Object> error=new HashMap<>();
            error.put("error",true);
            error.put("message","Account is disable");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }catch (Exception e){
            Map<String ,Object> error=new HashMap<>();
            error.put("error",true);
            error.put("message","Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name")String email){
        return ResponseEntity.ok(email !=null);
    }

    @PostMapping("/send-reset-otp/{email}")
    public ResponseEntity<String> sendResetOtp(@PathVariable String email) {
        try {
            userService.sendResetOtp(email);
            return ResponseEntity.ok("Reset OTP sent successfully to " + email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        try{
            userService.resetPassword(request.getEmail(),request.getOtp(),request.getNewPassword());
            return ResponseEntity.ok("Reset password Successful");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email){
        try{
            userService.sendOtp(email);
            return ResponseEntity.ok("OTP Send Successful");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyEmail(
            @RequestBody Map<String, Object> request,
            @CurrentSecurityContext(expression = "authentication.name") String email) {

        Object otpObj = request.get("otp");
        if (otpObj == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing OTP");
        }

        String otp = otpObj.toString();

        try {
            userService.verifyOtp(email, otp);
            return ResponseEntity.ok("Account verified successfully");
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

}
