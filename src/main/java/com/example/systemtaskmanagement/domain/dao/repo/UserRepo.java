package com.example.systemtaskmanagement.domain.dao.repo;

import com.example.systemtaskmanagement.domain.dao.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends  BaseRepo<User>{
    Optional<User> findByEmail(String email);
}
