package com.projekat.ris.controller;

import com.projekat.ris.dto.ProductDTO;
import com.projekat.ris.service.CategoryService;
import com.projekat.ris.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductDTO> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        ProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }

    @GetMapping("/name/{name}")
    public String getProductByName(@PathVariable String name, Model model) {
        ProductDTO product = productService.getProductByName(name);
        model.addAttribute("product", product);
        return "product-detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String addProduct(@ModelAttribute("product") ProductDTO productDTO) {
        ProductDTO created = productService.addProduct(productDTO);
        return "redirect:/products";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") ProductDTO productDTO) {
        ProductDTO updated = productService.updateProduct(productDTO);
        return "redirect:/products";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}
