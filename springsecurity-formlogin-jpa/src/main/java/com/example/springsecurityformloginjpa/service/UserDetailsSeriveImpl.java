package com.example.springsecurityformloginjpa.service;

import com.example.springsecurityformloginjpa.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsSeriveImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userService.getUserByUserName(username);

        if (userEntity == null) {
            System.out.println("User not found! " + username);
            throw new UsernameNotFoundException("User + " + username + " was not found in the database!");
        }

        System.out.println("Found user: " + username);

        List<String> roleName = this.userRoleService.getUserRoleByUserId(userEntity.getId());

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (roleName != null) {
            for (String role : roleName) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }

        UserDetails userDetails = new User(userEntity.getUsername(), userEntity.getPassword(), grantList);

        return userDetails;
    }
}
