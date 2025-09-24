package com.ems.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Hr {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String hrId; // Generated on approval, e.g., "1234"

    private String name;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String gender;
    private String department = "HR"; // Fixed
    private String position; // Optional
    private LocalDate joinDate;
    private String status = "pending"; // pending, approved, on_leave, inactive

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHrId() { return hrId; }
    public void setHrId(String hrId) { this.hrId = hrId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}