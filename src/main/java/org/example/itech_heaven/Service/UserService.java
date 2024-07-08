package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.Role;
import org.example.itech_heaven.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface UserService {
    User addUser(User user);

    User findByEmail(String email);
    User findByUsernameOrEmail(String username, String email);

    void resetUserPassword(String password, String token);
    User findByUsername(String username);
    void updateUser(User user);

    List<User> findAll();

    void UpdateStatusOfUser(int id, boolean status);

    void updateUserRole(int id, int[] role_id);

    void resetUserPassword(User user, int pass);
    User findById(int id);
    void UpdateStatusAll(boolean status);

    List<User> findAllByRoles(int role_id);


    void updateLastLoginDate(int id);
    public Optional<Integer> getCurrentUserId();

    public Optional<String> getCurrentUserEmail();
    long countUsers();
    List<User> findNewUsers();

}
