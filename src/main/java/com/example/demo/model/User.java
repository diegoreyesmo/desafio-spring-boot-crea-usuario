package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @NotBlank(message = "El email no puede estar en blanco")
    @Email(message = "email con formato invalido")
    private String email;
    @NotBlank(message = "El email no puede estar en blanco")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "La contraseña debe se de largo mínimo 8 y debe contener letras y números.")
    private String password;
    @OneToMany
    private List<Phone> phones;
    private Instant created;
    private Instant modified;
    private Instant last_login;
    private UUID token;
    private Boolean isactive;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Instant getLast_login() {
        return last_login;
    }

    public void setLast_login(Instant last_login) {
        this.last_login = last_login;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public User(UUID id, String name, String email, String password, List<Phone> phones, Instant created, Instant modified, Instant last_login, UUID token, Boolean isactive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
        this.created = created;
        this.modified = modified;
        this.last_login = last_login;
        this.token = token;
        this.isactive = isactive;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phones=" + phones +
                ", created=" + created +
                ", modified=" + modified +
                ", last_login=" + last_login +
                ", token=" + token +
                ", isactive=" + isactive +
                '}';
    }
}

