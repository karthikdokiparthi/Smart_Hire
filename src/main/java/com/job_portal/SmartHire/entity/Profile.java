package com.job_portal.SmartHire.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "full_name")
    private String fullName;
    private String phone;

    @Column(name = "resume_file_path")
    private String resumeFilePath;

    @ElementCollection
    @CollectionTable(name = "profile_skills",joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skills")
    private List<String> skills;

    @Column(length = 1000)
    private String experience;

    public Profile(){}

    public Profile(User user,String fullName){
        this.user=user;
        this.fullName=fullName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getResumeFilePath() {
        return resumeFilePath;
    }

    public void setResumeFilePath(String resumeFilePath) {
        this.resumeFilePath = resumeFilePath;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}