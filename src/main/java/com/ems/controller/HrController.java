package com.ems.controller;

import com.ems.model.Hr;
import com.ems.repository.HrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/hr")
public class HrController {

    @Autowired
    private HrRepository hrRepository;

    // Create HR (signup as pending)
    @PostMapping("/create")
    public Hr createHr(@RequestBody Map<String, String> userData) {
        Hr hr = new Hr();
        hr.setName(userData.get("name"));
        hr.setUsername(userData.get("username"));
        hr.setPassword(userData.get("password"));
        hr.setEmail(userData.get("email"));
        hr.setMobile(userData.getOrDefault("mobile", ""));
        hr.setGender(userData.getOrDefault("gender", ""));
        hr.setJoinDate(LocalDate.now());
        hr.setStatus("pending");
        if (hrRepository.findByEmail(hr.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (hrRepository.findByUsername(hr.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        return hrRepository.save(hr);
    }

    // HR login
    @PostMapping("/login")
    public Hr loginHr(@RequestBody Map<String, String> payload) {
        String hrId = payload.get("loginId");
        String password = payload.get("password");
        return hrRepository.findByHrId(hrId)
                .filter(h -> h.getPassword().equals(password) && "approved".equals(h.getStatus()))
                .orElseThrow(() -> new RuntimeException("Invalid ID or password, or not approved"));
    }

    // Get approved HR accounts
    @GetMapping("/accounts")
    public List<Hr> getHrAccounts() {
        return hrRepository.findByStatus("approved");
    }

    // Get pending HR
    @GetMapping("/pending")
    public List<Hr> getPendingHr() {
        return hrRepository.findByStatus("pending");
    }

    // Approve HR
    @PostMapping("/approve/{id}")
    public Hr approveHr(@PathVariable Long id) {
        Hr hr = hrRepository.findById(id).orElseThrow(() -> new RuntimeException("HR not found"));
        if ("pending".equals(hr.getStatus())) {
            String hrId = generateUnique4DigitId();
            hr.setHrId(hrId);
            hr.setStatus("approved");
            hrRepository.save(hr);
            sendSms(hr.getMobile(), "Your HR ID is " + hrId + ". Use it to login.");
        }
        return hr;
    }

    // Reject pending HR
    @DeleteMapping("/reject/{id}")
    public void rejectHr(@PathVariable Long id) {
        hrRepository.deleteById(id);
    }

    // Delete approved HR
    @DeleteMapping("/{id}")
    public void deleteHr(@PathVariable Long id) {
        hrRepository.deleteById(id);
    }

    // Update HR
    @PutMapping("/{id}")
    public Hr updateHr(@PathVariable Long id, @RequestBody Hr updatedHr) {
        Hr hr = hrRepository.findById(id).orElseThrow(() -> new RuntimeException("HR not found"));
        hr.setName(updatedHr.getName());
        hr.setUsername(updatedHr.getUsername());
        hr.setPassword(updatedHr.getPassword());
        hr.setEmail(updatedHr.getEmail());
        hr.setMobile(updatedHr.getMobile());
        hr.setGender(updatedHr.getGender());
        hr.setPosition(updatedHr.getPosition());
        hr.setJoinDate(updatedHr.getJoinDate());
        hr.setStatus(updatedHr.getStatus());
        return hrRepository.save(hr);
    }

    // Generate unique 4-digit ID
    private String generateUnique4DigitId() {
        Random rand = new Random();
        String id;
        do {
            id = String.format("%04d", rand.nextInt(10000));
        } while (hrRepository.findByHrId(id).isPresent());
        return id;
    }

    // Simulate SMS
    private void sendSms(String mobile, String message) {
        System.out.println("SIMULATED SMS to " + mobile + ": " + message);
    }
}