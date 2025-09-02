package com.projekat.ris.controller;

import com.projekat.ris.dto.UserRegistrationDTO;
import com.projekat.ris.dto.UserResponseDTO;
import com.projekat.ris.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "user-register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid UserRegistrationDTO userRegistrationDTO,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "user-register";
        }
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

    @GetMapping("/userInfo")
    public String userInfo(Authentication auth) {
        UserResponseDTO user = userService.getUserByUsername(auth.getName());
        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/{id}/password")
    public String showPasswordForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "user-password-reset";
    }

    @PostMapping("/{id}/password")
    public String resetPassword(@PathVariable Long id,
                                @RequestParam String password,
                                RedirectAttributes redirectAttributes) {
        UserResponseDTO userResponseDTO = userService.updatePassword(id, password);
        redirectAttributes.addFlashAttribute("alertOk", "Password successfully changed!");
        return "redirect:/users/" + id;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        UserResponseDTO userResponseDTO = userService.getUserById(id);

        model.addAttribute("userForm", userResponseDTO);
        model.addAttribute("userId", id);
        return "user-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("userForm") UserResponseDTO userResponseDTO,
                             RedirectAttributes redirectAttributes) {
        UserResponseDTO user = userService.updateUser(id, userResponseDTO);
        redirectAttributes.addFlashAttribute("alertOk", "User profile updated!");
        return "redirect:/users/" + id;
    }
}
