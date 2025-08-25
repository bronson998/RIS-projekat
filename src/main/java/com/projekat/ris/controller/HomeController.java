package com.projekat.ris.controller;

import com.projekat.ris.dto.ProductDTO;
import com.projekat.ris.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {
    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        List<ProductDTO> products = productService.getAllProducts();
        model.addAttribute("featured", products);
        return "redirect:/products";
    }
}
