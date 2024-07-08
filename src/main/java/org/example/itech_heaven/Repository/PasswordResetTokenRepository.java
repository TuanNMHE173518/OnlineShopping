package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.PasswordResetToken;
import org.example.itech_heaven.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUserId(int id);

    @Query("SELECT u FROM User u JOIN PasswordResetToken prt ON u.id = prt.user.id WHERE prt.token = :token")
    User findUserByToken(@Param("token") String token);

    void deleteByUser(User user);
}
