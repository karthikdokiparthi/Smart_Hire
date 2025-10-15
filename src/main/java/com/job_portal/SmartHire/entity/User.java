package com.job_portal.SmartHire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String userId;

    @NotNull(message = "Name should not be empty")
    private String name;

    @Column(unique = true)
    @Email(message = "Enter valid email")
    @NotNull(message = "Email should not be empty")
    private String email;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;

//    @Column(name = "created_at")
//    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    private String verifyOtp;

    private boolean isAccountVerified;

    private Long verifyOtpExpireAt;

    private String resetOtp;

    private Long resetOtpExpireAt;


//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//    }

    public User(){}

    public User(String email,String password,Role role){
        this.email=email;
        this.password=password;
        this.role=role;
    }

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

}