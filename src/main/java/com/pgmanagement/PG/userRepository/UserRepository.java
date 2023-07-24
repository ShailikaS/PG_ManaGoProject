package com.pgmanagement.PG.userRepository;

import java.util.Optional;

import com.pgmanagement.PG.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
