package com.job_portal.SmartHire.service;

import com.job_portal.SmartHire.dto.UserRequest;
import com.job_portal.SmartHire.dto.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    UserResponse getUser(String email);
}
