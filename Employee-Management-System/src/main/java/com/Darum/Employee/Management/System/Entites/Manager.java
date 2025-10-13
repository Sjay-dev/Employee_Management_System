package com.Darum.Employee.Management.System.Entites;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Enum.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "managers")
@Getter
@Setter
@NoArgsConstructor
public class Manager extends User {

    {this.setRole(Role.MANAGER);}

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    private String position;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private LocalDate employmentDate;

    private String employmentType;

    private String salary;

    private String address;

    @Column(nullable = false)
    private String gender;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Employees managed by this manager
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Employee> employees = new ArrayList<>();

    // Department relationship
    @ManyToOne
    @JoinColumn(name = "departmentId")
    @JsonIgnore
    private Department department;

    // Expose only department name in JSON
    @JsonProperty("departmentName")
    public String getDepartmentName() {
        return department != null ? department.getName() : null;
    }

    // Expose only employee names in JSON
    @JsonProperty("employeeNames")
    public List<String> getEmployeeNames() {
        return employees.stream()
                .map(emp -> emp.getFirstName() + " " + emp.getLastName())
                .collect(Collectors.toList());
    }
}



