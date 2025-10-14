package com.Darum.Employee.Management.System.Entites;


import com.Darum.Employee.Management.System.Entites.Enum.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "userId")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

    {this.setRole(Role.ADMIN);}


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}

