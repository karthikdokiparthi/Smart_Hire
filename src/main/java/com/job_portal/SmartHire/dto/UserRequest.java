package com.job_portal.SmartHire.dto;

import com.job_portal.SmartHire.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {


    private String name;
    private String email;
    private String password;
    private Role role;
}
