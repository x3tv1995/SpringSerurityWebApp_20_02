package com.example.springSecurityWebApp.serurity;

import com.example.springSecurityWebApp.entity.User;
import com.example.springSecurityWebApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final String PREFIX_ROLE = "ROLE_";

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findByUsername(username);


        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(user.get().getUsername());
        userDetails.setPassword(user.get().getPassword());

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(PREFIX_ROLE + user.get().getRole().name()));


        userDetails.setRoles(roles);
        return userDetails;



    }
}

