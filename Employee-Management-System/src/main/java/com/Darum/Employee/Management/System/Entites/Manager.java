package com.Darum.Employee.Management.System.Entites;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Enum.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    private Status status =  Status.INACTIVE;

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

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Leave> leaves = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;
}

