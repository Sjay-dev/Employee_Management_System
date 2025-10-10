package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {


    @Query("SELECT r from ResetToken  r WHERE r.token = :token")
    public Optional<ResetToken> findByToken(String token);

    @Query("SELECT r from ResetToken  r WHERE r.email = :email")
    public Optional<ResetToken> findByEmail(String email);

    @Query("DELETE from ResetToken r WHERE r.token = :token")
   public void deleteByToken(String token);
}
