// service-module/src/main/java/com/example/service/repository/UserRepository.java
package com.example.service.repository;

import com.example.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    List<User> findByDepartment(String department);
    
    @Query("SELECT u FROM User u WHERE u.name LIKE %?1%")
    List<User> findByNameContaining(String name);
    
    boolean existsByEmail(String email);
}