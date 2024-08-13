package com.example.forum.controller;

import com.example.forum.model.Category;
import com.example.forum.model.Message;
import com.example.forum.model.Role;
import com.example.forum.model.User;
import com.example.forum.repository.MessageRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/registration")
    public String getMapping(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("messages", users);
        return "registration";
    }


    @PostMapping("/registration")
    public String postMapReg(@RequestParam(name = "name") String name, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password, @RequestParam(name = "password-repeat") String passwordRepeat, Model model) {
        if (!password.equals(passwordRepeat)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "registration";
        }
        //userRepository.findAll().stream().map(x->x.getUsername()).filter(x->x.equals(username)).count()>1
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Такой пользователь уже существут");
            return "registration";
        } else {
            User user = new User();
            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER);
            user.setName(name);
            user.setFirstName(firstName);
            user.setUsername(username);
            user.setPassword(password);
            user.setRoles(roles);
            user.setActive(true);
            userRepository.save(user);
        }
        return "redirect:/forum";
    }
}
