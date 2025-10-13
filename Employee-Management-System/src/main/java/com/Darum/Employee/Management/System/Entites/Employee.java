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
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
    public class Employee extends User {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Status status =  Status.INACTIVE;

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


    @ManyToOne
    @JoinColumn(name = "managerId")
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;


}
