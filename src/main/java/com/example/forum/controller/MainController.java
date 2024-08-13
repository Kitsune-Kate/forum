package com.example.forum.controller;

import com.example.forum.model.Category;
import com.example.forum.model.Message;
import com.example.forum.model.User;
import com.example.forum.repository.MessageRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Controller
public class MainController {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/forum")
    public String getMapping(Model model, @AuthenticationPrincipal User user) {
        List<Message> messages = messageRepository.findAll();
        List<User> userList = userRepository.findAll();
        List<String> userNameList = userList.stream().map(x -> x.getUsername()).toList();
        model.addAttribute("userNames", userNameList);
        model.addAttribute("messages", messages);
        String currentUsername = user.getUsername();
        model.addAttribute("currentUsername", currentUsername);
        return "forum";
    }

    @PostMapping("/createMessage")
    public String postMap(@AuthenticationPrincipal User user, @RequestParam(name = "title") String title, @RequestParam(name = "textarea") String textarea, @RequestParam(name = "choice") String choice,
                          @RequestParam("image") MultipartFile file, Model model) throws IOException {
        Date date = Calendar.getInstance().getTime();
        Message message = new Message();
        message.setChoice(Category.valueOf(choice));
        message.setTextarea(textarea);
        message.setTitle(title);
        message.setDate(date);
        message.setUser(user);
        String uuid = UUID.randomUUID().toString().substring(0, 13);
        String fileName = uuid + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + fileName));
        if (!file.isEmpty()) {
            message.setFilename(fileName);
        }
        messageRepository.save(message);
        return "redirect:/forum";
    }

    @PostMapping("/filter")
    public String postMapFilter(@RequestParam(name = "choice") String category, Model model) {
        List<Message> messagesFromCategory = messageRepository.findByChoice(Category.valueOf(category));
        model.addAttribute("messages", messagesFromCategory);
        return "forum";
    }


    @PostMapping("/filterUser")
    public String postMapSortedUser(@RequestParam(name = "username") String username, Model model) {
        if (username.equals("0")) {
            List<Message> messages = messageRepository.findAll();
            model.addAttribute("messages", messages);
            List<User> userList = userRepository.findAll();
            List<String> userNameList = userList.stream().map(x -> x.getUsername()).toList();
            model.addAttribute("userNames", userNameList);
        } else {
            User user = userRepository.findByUsername(username);
            List<Message> messagesByUser = messageRepository.findByUser(user);
            model.addAttribute("messages", messagesByUser);
            List<User> userList = userRepository.findAll();
            List<String> userNameList = userList.stream().map(x -> x.getUsername()).toList();
            model.addAttribute("userNames", userNameList);
        }
        return "forum";
    }

    @PostMapping("/sorted")
    public String postMapSorted(@RequestParam(name = "type") String type, Model model) {
        if (type.equals("asc")) {
            List<Message> messagesSorted = messageRepository.findAll();
            List<Message> messagesNew = messagesSorted.stream().sorted(Comparator.comparing(x -> x.getDate())).toList();
            model.addAttribute("messages", messagesNew);
        }
        if (type.equals("desc")) {
            List<Message> messagesSorted = messageRepository.findAll();
            List<Message> messagesNew = messagesSorted.stream().sorted((x, y) -> y.getDate().compareTo(x.getDate())).toList();
            model.addAttribute("messages", messagesNew);
        }
        return "forum";
    }


}
