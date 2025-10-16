package com.assignment.fuelorder.repo;

import com.assignment.fuelorder.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Boolean existsByEmailIgnoreCase(String email);
}
