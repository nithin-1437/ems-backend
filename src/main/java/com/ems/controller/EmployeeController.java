package com.ems.controller;

import com.ems.model.Employee;
import com.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Create Employee (signup as pending)
    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Map<String, String> userData) {
        Employee emp = new Employee();
        emp.setName(userData.get("name"));
        emp.setUsername(userData.get("username"));
        emp.setPassword(userData.get("password"));
        emp.setEmail(userData.get("email"));
        emp.setMobile(userData.getOrDefault("mobile", ""));
        emp.setGender(userData.getOrDefault("gender", ""));
        emp.setDepartment(userData.get("department"));
        emp.setJoinDate(LocalDate.now());
        emp.setStatus("pending");
        if (employeeRepository.findByEmail(emp.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (employeeRepository.findByUsername(emp.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        return employeeRepository.save(emp);
    }

    // Employee login
    @PostMapping("/login")
    public Employee loginEmployee(@RequestBody Map<String, String> payload) {
        String empId = payload.get("loginId");
        String password = payload.get("password");
        return employeeRepository.findByEmpId(empId)
                .filter(e -> e.getPassword().equals(password) && "approved".equals(e.getStatus()))
                .orElseThrow(() -> new RuntimeException("Invalid ID or password, or not approved"));
    }

    // Get approved employees
    @GetMapping
    public List<Employee> getEmployees() {
        return employeeRepository.findByStatus("approved");
    }

    // Get pending employees
    @GetMapping("/pending")
    public List<Employee> getPendingEmployees() {
        return employeeRepository.findByStatus("pending");
    }

    // Approve employee
    @PostMapping("/approve/{id}")
    public Employee approveEmployee(@PathVariable Long id) {
        Employee emp = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        if ("pending".equals(emp.getStatus())) {
            String empId = generateUnique4DigitId();
            emp.setEmpId(empId);
            emp.setStatus("approved");
            employeeRepository.save(emp);
            sendSms(emp.getMobile(), "Your Employee ID is " + empId + ". Use it to login.");
        }
        return emp;
    }

    // Reject pending employee
    @DeleteMapping("/reject/{id}")
    public void rejectEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }

    // Delete approved employee
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }

    // Update employee
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmp) {
        Employee emp = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        emp.setName(updatedEmp.getName());
        emp.setUsername(updatedEmp.getUsername());
        emp.setPassword(updatedEmp.getPassword());
        emp.setEmail(updatedEmp.getEmail());
        emp.setMobile(updatedEmp.getMobile());
        emp.setGender(updatedEmp.getGender());
        emp.setDepartment(updatedEmp.getDepartment());
        emp.setPosition(updatedEmp.getPosition());
        emp.setJoinDate(updatedEmp.getJoinDate());
        emp.setStatus(updatedEmp.getStatus());
        return employeeRepository.save(emp);
    }

    // Generate unique 4-digit ID
    private String generateUnique4DigitId() {
        Random rand = new Random();
        String id;
        do {
            id = String.format("%04d", rand.nextInt(10000));
        } while (employeeRepository.findByEmpId(id).isPresent());
        return id;
    }

    // Simulate SMS
    private void sendSms(String mobile, String message) {
        System.out.println("SIMULATED SMS to " + mobile + ": " + message);
    }
}