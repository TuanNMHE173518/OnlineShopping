package org.example.itech_heaven.Service;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Cart;
import org.example.itech_heaven.Entity.Role;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Repository.CartRepository;
import org.example.itech_heaven.Repository.PasswordResetTokenRepository;
import org.example.itech_heaven.Repository.RoleRepository;
import org.example.itech_heaven.Repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;


    @Override
    @Transactional
    public User addUser(User user) {
        Role role = roleRepository.findByName("ROLE_CUSTOMER");
        // Sử dụng ArrayList thay vì Collections.singletonList
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        user.setProvider("local");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Lưu User trước để đảm bảo rằng nó có ID
        User savedUser = userRepository.save(user);

        // Tạo giỏ hàng mới và liên kết với User đã lưu
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);

        // Thiết lập liên kết một lần nữa để đảm bảo đồng bộ
        savedUser.setCart(cart);

        // Lưu lại User với giỏ hàng liên kết
        return userRepository.save(savedUser);
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username,email);
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }



    @Override
    public void resetUserPassword(String password, String token) {
        User user = passwordResetTokenRepository.findUserByToken(token);
        if (user != null){
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

        }
    }



    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void UpdateStatusOfUser(int id, boolean status) {
        User user = userRepository.findById(id).orElse(null);
        assert user != null;
        boolean isAdmin = user.getUsername().equals("Admin123");
        if (!status && isAdmin) {
            throw new IllegalStateException("Cannot disable admin user");
        }
        user.setAccountExpireDate(null);
        user.setEnabled(status);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserRole(int id, int[] role_id) {
        List<Role> roles = new ArrayList<>();
        for(Integer i : role_id){
            Role role = roleRepository.findById(i).orElse(null);
            roles.add(role);
        }
        User user = userRepository.findById(id).orElse(null);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void resetUserPassword(User user, int pass) {
        user.setPassword(passwordEncoder.encode(""+pass));
        userRepository.save(user);
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void UpdateStatusAll(boolean status) {
        userRepository.updateAllByEnabledIsFalse(status);
    }

    @Override
    public List<User> findAllByRoles(int role_id) {
        Role role = roleRepository.findById(role_id).orElse(null);

        return userRepository.findAllByRolesContaining(role);
    }



    @Override
    public void updateLastLoginDate(int id) {
        User user = findById(id);
        if (user != null){
            LocalDate now = LocalDate.now();
            user.setLastLoginDate(now);
            user.setAccountExpireDate(now.plusYears(2));
            for(Role r : user.getRoles()){
                if (r.getName().equals("ROLE_ADMIN")){
                    user.setAccountExpireDate(null);
                    break;
                }
            }

            userRepository.save(user);
        }
    }

    @Override
    public Optional<Integer> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
            return userOptional.map(User::getId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
            return userOptional.map(User::getEmail);
        }
        return Optional.empty();
    }

    @Override
    public long countUsers() {
        return  userRepository.count();
    }

    @Override
    public List<User> findNewUsers() {
        return userRepository.findTop10ByOrderByIdDesc();
    }

}
