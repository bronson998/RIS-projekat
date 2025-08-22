package com.projekat.ris.controller;

import com.projekat.ris.dto.UserRegistrationDTO;
import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "user-register"; // templates/user-register.html
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserRegistrationDTO userRegistrationDTO, Model model) {
        try {
            userService.registerUser(userRegistrationDTO);
            return "redirect:/products";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            return "user-register";
        }
    }

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        UserResponseDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-detail";
    }
}
