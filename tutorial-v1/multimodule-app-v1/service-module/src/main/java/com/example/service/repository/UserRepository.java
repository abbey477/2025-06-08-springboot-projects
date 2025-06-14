package com.example.service.repository;

import com.example.service.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    Optional<User> findByEmail(String email);

    List<User> findByDepartment(String department);

    @Query("SELECT * FROM users WHERE name LIKE '%' || :name || '%'")
    List<User> findByNameContaining(@Param("name") String name);

    @Query("SELECT COUNT(*) > 0 FROM users WHERE email = :email")
    boolean existsByEmail(@Param("email") String email);
}