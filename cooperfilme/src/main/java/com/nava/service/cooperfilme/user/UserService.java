package com.nava.service.cooperfilme.user;

import com.nava.service.cooperfilme.user.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByName(username)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));
    }

    public User findByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new RuntimeException("Usuario não encontrado!"));
    }
}
