package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts")
@Builder
@Data
public class Account extends BaseEntity implements UserDetails, Serializable, ExcelExportable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name="email",length = 50)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "active")
    private boolean active;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRole().getName().toUpperCase()));
//        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorityList;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
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
        return true;
    }

    @Override
    public Object[] toExcelRow() {
        return new Object[]{id, username, password,active,super.getCreatedAt(), super.getUpdatedAt()};
    }

    @Override
    public String[] getColumnHeaders() {
        return new String[]{"ID","Tên đăng nhập","Mật khẩu","Kích hoạt","Ngày tạo","Ngày cập nhật"};
    }
}
