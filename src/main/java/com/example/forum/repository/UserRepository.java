package com.example.forum.repository;

import com.example.forum.model.Message;
import com.example.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    public boolean existsByUsername(String username);

    public User findByUsername(String username);


}
