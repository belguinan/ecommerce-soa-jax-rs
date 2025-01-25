package com.example.userservice.database.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Length(min = 4, max = 20)
    private String username;

    @NotNull
    @NotEmpty
    @Length(min = 4, max = 199)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotEmpty
    @Length(min = 2, max = 199)
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Length(min = 10, max = 10)
    private String phoneNumber;

    @NotEmpty
    private String address;

    @NotEmpty
    private String city;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
