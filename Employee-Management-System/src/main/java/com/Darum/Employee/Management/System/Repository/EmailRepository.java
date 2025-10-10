package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

}
