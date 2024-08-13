package com.example.systemtaskmanagement.service;

import com.example.systemtaskmanagement.domain.dao.dto.UserDTO;
import com.example.systemtaskmanagement.domain.dao.mapper.UserMapper;
import com.example.systemtaskmanagement.domain.dao.model.User;
import com.example.systemtaskmanagement.domain.dao.repo.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User, UserDTO, UserRepo, UserMapper>{

    protected UserService(UserRepo repository, UserMapper mapper) {
        super(repository, mapper);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("No authentication data is available in the security context.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return getRepository().findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDetails.getUsername()));
        } else if (principal instanceof String username) {
            return getRepository().findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        } else {
            throw new SecurityException("Authentication principal is neither a UserDetails instance nor a valid username.");
        }
    }

    @Override
    public UserDTO update(UserDTO dto) {
        return null;
    }
}
