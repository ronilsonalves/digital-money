package com.digitalhouse.money.usersservice.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1912491329356572322L;

    @jakarta.persistence.Id
    @Column(nullable = false,unique = true)
    private UUID Id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    @Size(min = 11, max = 14)
    @Column(unique = true)
    private String cpf;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 10, max = 15)
    private String phone;

    public User(String id) {
        this.Id = UUID.fromString(id);
    }

    public User(String id, String firstName, String lastName, String email) {
        this.Id = UUID.fromString(id);
        this.name = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Id != null && Objects.equals(Id, user.Id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
