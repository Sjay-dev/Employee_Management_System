package com.Darum.Employee.Management.System.DTO;



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
