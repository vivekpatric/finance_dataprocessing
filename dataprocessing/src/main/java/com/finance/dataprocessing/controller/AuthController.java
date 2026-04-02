package com.finance.dataprocessing.controller;

import com.finance.dataprocessing.model.User;
import com.finance.dataprocessing.repository.UserRepository;
import com.finance.dataprocessing.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.beans.Encoder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public UserRepository repo;

    @Autowired
    public JwtUtil jwtUtil;

    @Autowired
    public PasswordEncoder encoder;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        User dbUser = repo.findByUsername(user.getUsername()).orElseThrow();
        if(encoder.matches(user.getPassword(), dbUser.getPassword())){
            return jwtUtil.generateToken(dbUser.getUsername());
        }
        throw new RuntimeException("Invalid Credentials");
    }



}
