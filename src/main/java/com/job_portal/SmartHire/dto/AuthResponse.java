package com.job_portal.SmartHire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String email;
    private String jwt;
}
