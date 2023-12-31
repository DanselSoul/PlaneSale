package com.chikilev.planesale.Repository;
import com.chikilev.planesale.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String Username);

    boolean existsByUsername(String username);
}