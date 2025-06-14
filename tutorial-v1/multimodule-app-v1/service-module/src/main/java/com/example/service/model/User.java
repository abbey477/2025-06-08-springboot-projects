package com.example.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Table("users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long id;

    @NotBlank(message = "Name is required")
    @Column("name")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column("email")
    private String email;

    @Column("department")
    private String department;
}