package com.ajd.pieceOfCode.user;

import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Basic(optional = false)
    private String lastName;
    @Basic(optional = false)
    private String firstName;
    @Basic(optional = false)
    @Column(unique = true)
    private String email;
    @Basic(optional = false)
    private String password;

    @PersistenceConstructor
    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("lastName") String lastName, @JsonProperty("firstName") String firstName,
                @JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        setPassword(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(View.Summary.class)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonView(View.Summary.class)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonView(View.Summary.class)
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @JsonView(View.UserProfile.class)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "user");
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    private void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
