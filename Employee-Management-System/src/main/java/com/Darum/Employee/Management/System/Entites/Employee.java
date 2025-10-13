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


@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends User {

    {this.setRole(Role.EMPLOYEE);}

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    private String position;

    private String employmentType;

    private String salary;

    private String address;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String gender;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Manager relationship, ignore full serialization to avoid recursion
    @ManyToOne
    @JoinColumn(name = "managerId", nullable = true)
    @JsonIgnore
    private Manager manager;

    // Department relationship, ignore full serialization
    @ManyToOne
    @JoinColumn(name = "departmentId", nullable = true)
    @JsonIgnore
    private Department department;

    // JSON property to expose only manager name
    @JsonProperty("managerName")
    public String getManagerName() {
        return manager != null ? manager.getFirstName() + " " + manager.getLastName() : null;
    }

    // JSON property to expose only department name
    @JsonProperty("departmentName")
    public String getDepartmentName() {
        return department != null ? department.getName() : null;
    }
}

