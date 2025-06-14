package com.example.userservice.repository;

import com.example.common.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT * FROM users WHERE name LIKE %:name%")
    Iterable<User> findByNameContaining(@Param("name") String name);
}