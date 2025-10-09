package com.Darum.Employee.Management.System.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgetPassword {
    private String email;
    private String password;
}
