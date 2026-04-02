package com.finance.dataprocessing.controller;

import com.finance.dataprocessing.model.Role;
import com.finance.dataprocessing.model.User;
import com.finance.dataprocessing.repository.UserRepository;
import jakarta.annotation.Resource;
import org.apache.catalina.users.SparseUserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repo;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers(){
        return repo.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public User updateRole(@PathVariable Long id, @RequestParam Role role){
        User user = repo.findById(id).orElseThrow();
        user.setRole(role);
        return repo.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public User updateStatus(@PathVariable Long id,@RequestParam boolean active){
        User user =repo.findById(id).orElseThrow();
        user.setActive(active);
        return repo.save(user);
    }

}
