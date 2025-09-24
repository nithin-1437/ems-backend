package com.ems.controller;

import com.ems.model.Employee;
import com.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Create Employee
    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Map<String, String> userData) {
        Employee emp = new Employee();
        emp.setName(userData.get("name"));
        emp.setEmail(userData.get("email"));
        emp.setPosition(userData.get("position"));
        emp.setSalary(Double.parseDouble(userData.get("salary")));
        return employeeRepository.save(emp);
    }

    // Get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Update employee
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmp) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        emp.setName(updatedEmp.getName());
        emp.setEmail(updatedEmp.getEmail());
        emp.setPosition(updatedEmp.getPosition());
        emp.setSalary(updatedEmp.getSalary());
        return employeeRepository.save(emp);
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
}
