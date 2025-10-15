package com.job_portal.SmartHire.controller;

import com.job_portal.SmartHire.dto.UserRequest;
import com.job_portal.SmartHire.dto.UserResponse;
import com.job_portal.SmartHire.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request){
        UserResponse response=userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-current-user")
    public UserResponse getUser(@CurrentSecurityContext(expression = "authentication?.name")String email){
        return userService.getUser(email);
    }
}
