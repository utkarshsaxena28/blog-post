package com.blog.security.service;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

//import com.blog.security.model.CustomUserDetails;
import com.blog.entity.User;
import com.blog.repositiory.UserRepositiory;
import org.springframework.stereotype.Service;

/*
@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
    private UserRepositiory userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {

        final User user = this.userRepository.findByName(userName);

        if (user == null) {
            throw new UsernameNotFoundException("User not found !!");
        } else {
            return new CustomUserDetails(user);
        }
    }
}*/
