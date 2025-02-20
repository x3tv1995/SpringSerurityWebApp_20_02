package com.example.springSecurityWebApp.controller;


import com.example.springSecurityWebApp.entity.User;
import com.example.springSecurityWebApp.enums.Role;
import com.example.springSecurityWebApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



   @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/login")
    public  String  registration(){
       return "authorization";
    }

    @GetMapping("/accounts")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "accounts";
    }


    @GetMapping("/registration")
    public  String authenticate(){
        return "Registration";
    }


    @PostMapping("/register")
    public  String  register(@RequestParam String username, String password,String email,Model model){

        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole(Role.USER);

            userRepository.save(newUser);
            return "redirect:/login";
        }else{
            model.addAttribute("message", "Такой юзер существует");
            return "redirect:/registration";
        }

    }


}
