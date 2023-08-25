package com.example.testflybuy.controller;

import com.example.testflybuy.Global.GlobalData;
import com.example.testflybuy.model.User;
import com.example.testflybuy.repository.RoleRepository;
import com.example.testflybuy.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        GlobalData.cart.clear();
        return "login";
    }
    @GetMapping("/register")
    public String registerGet(){
        return "register";
    }
    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request)throws ServletException {
    String password=user.getPassword();
    user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles=new ArrayList<>();
        roles.add(roleRepository.findById(2).get());
        user.setRoles(roles);
        userRepository.save(user);
        request.login(user.getEmail(),password);
        return "redirect:/";
    }
}
