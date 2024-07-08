package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 6, message = "Tên người dùng phải có ít nhất 6 ký tự")
    private String username;

    @Size(min = 6, message = "Mật khẩu phải có từ 6 đến 20 k tự")

    @Size(min =8, message = "Mật khẩu phải có ít nhất 8 kí tự")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()/\\\\,.?\":{}|<>])[A-Za-z\\d!@#$%^&*()/\\\\,.?\":{}|<>]{8,}$",
            message = "Mật khẩu phải bao gồm chữ thường, chữ hoa, số và ký tự đặc biệt")

    private String password;
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Họ và tên đệm không được chứa số và ký tự đặc biệt")
    private String firstname;
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Tên không được chứa số và ký tự đặc biệt")
    private String lastname;
    @Email( regexp="^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Định dạng Email không hợp lệ")
    private String email;
    @Pattern(
            regexp = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-6|8|9]|9[0-4|6-9])[0-9]{7}$",
            message = "Định dạng số điện thoại không hợp lệ"
    )
    private String phone;
    @NotBlank(message = "Vui lòng nhập địa chỉ")
    private String address;

    private boolean gender;
    private String provider;

    @Builder.Default
    private boolean enabled = true;
    private LocalDate accountExpireDate;
    private LocalDate lastLoginDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        roles.stream().flatMap(role -> role.getPermissions().stream()).forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountExpireDate == null || this.accountExpireDate.isAfter(LocalDate.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
