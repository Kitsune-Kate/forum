package com.example.forum.controller;

import com.example.forum.model.Category;
import com.example.forum.model.Message;
import com.example.forum.model.Role;
import com.example.forum.model.User;
import com.example.forum.repository.MessageRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/adminPage")
    public String getMapping(Model model, @AuthenticationPrincipal User user) {
        List<User> users = userRepository.findAll();
        users.removeIf(u -> u.getRoles().contains(Role.ADMIN) && u.getName().contains("admin"));
        model.addAttribute("users", users);
        List<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        String currentUsername = user.getUsername();
        model.addAttribute("currentUsername", currentUsername);
        return "adminPage";
    }

    @GetMapping("/buttonBan/{id}")
    public String getMappingButtonBan(Model model, @PathVariable String id) {
        User userById = userRepository.findById(Integer.parseInt(id)).get();
        if (userById.isActive()) {
            userById.setActive(false);
            userRepository.save(userById);
        } else {
            userById.setActive(true);
            userRepository.save(userById);
        }
        return "redirect:/adminPage";
    }

    @GetMapping("/updateUser/{id}")
    public String getMappingUpdateUser(Model model, @PathVariable String id, @AuthenticationPrincipal User user) {
        User userById = userRepository.findById(Integer.parseInt(id)).get();
        if (userById.getRoles().contains(Role.ADMIN)) {
            model.addAttribute("admin", "admin");
        }
        if (userById.getRoles().contains(Role.USER)) {
            model.addAttribute("user", "user");
        }
        if (userById.getRoles().contains(Role.MODERATOR)) {
            model.addAttribute("mod", "mod");
        }
        int idFromDB = userById.getId();
        String name = userById.getName();
        String firstName = userById.getFirstName();
        String username = userById.getUsername();
        String password = userById.getPassword();
        boolean active = userById.isActive();
        model.addAttribute("id", idFromDB);
        model.addAttribute("name", name);
        model.addAttribute("firstName", firstName);
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("active", active);
        String currentUsername = user.getUsername();
        model.addAttribute("currentUsername", currentUsername);
        return "userProfile";
    }

    @PostMapping("/savingNewUserData")
    public String postMappingSavingNewUserData(@RequestParam(name = "admin", required = false, defaultValue = " ") String admin,
                                               @RequestParam(name = "user", required = false) String user,
                                               @RequestParam(name = "mod", required = false, defaultValue = " ") String mod,
                                               @RequestParam(name = "name") String name, @RequestParam(name = "firstName") String firstName,
                                               @RequestParam(name = "username") String username, @RequestParam(name = "password") String password, @RequestParam(name = "id") String id, Model model) {
        User userById = userRepository.findById(Integer.parseInt(id)).get();
        Set<Role> rolesNew = new HashSet<>();
        if (admin.equals("admin")) {
            Set<Role> roles = userById.getRoles();
            roles.add(Role.ADMIN);
//            userById.setRoles(roles);
            rolesNew.add(Role.ADMIN);
        }
        if (user.equals("user")) {
            Set<Role> roles = userById.getRoles();
            roles.add(Role.USER);
//            userById.setRoles(roles);
            rolesNew.add(Role.USER);
        }
        if (mod.equals("mod")) {
            Set<Role> roles = userById.getRoles();
            roles.add(Role.MODERATOR);
//            userById.setRoles(roles);
            rolesNew.add(Role.MODERATOR);
        }
        userById.setRoles(rolesNew);

        userById.setName(name);
        userById.setFirstName(firstName);
        userById.setUsername(username);
        userById.setPassword(password);
        userById.setActive(true);
        userRepository.save(userById);
        return "redirect:/adminPage";
    }
}
