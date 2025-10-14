package com.Darum.Employee.Management.System.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Manager> managers = new ArrayList<>();

    // Helper method to add a manager
    public void addManager(Manager manager) {
        managers.add(manager);
        manager.setDepartment(this);
    }

    // Helper method to remove a manager
    public void removeManager(Manager manager) {
        managers.remove(manager);
        manager.setDepartment(null);
    }

    // Helper method to add an employee
    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }

    // Helper method to remove an employee
    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setDepartment(null);
    }

    // JSON property to expose only manager names
    @JsonProperty("managerNames")
    public List<String> getManagerNames() {
        return managers.stream()
                .map(manager -> manager.getFirstName() + " " + manager.getLastName())
                .collect(Collectors.toList());
    }

    // JSON property to expose only employee names
    @JsonProperty("employeeNames")
    public List<String> getEmployeeNames() {
        return employees.stream()
                .map(employee -> employee.getFirstName() + " " + employee.getLastName())
                .collect(Collectors.toList());
    }
}

