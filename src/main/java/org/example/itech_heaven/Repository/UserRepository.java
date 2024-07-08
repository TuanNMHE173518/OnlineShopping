package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.Role;
import org.example.itech_heaven.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);


    User findByEmail(String email);
    User findByUsernameOrEmail(String username, String email);
    @Modifying
    @Query(value = "UPDATE User SET enabled = :newstatus")
    void updateAllByEnabledIsFalse(@Param("newstatus")boolean newstatus);

    List<User> findAllByRolesContaining(Role role);
    List<User> findTop10ByOrderByIdDesc();
}