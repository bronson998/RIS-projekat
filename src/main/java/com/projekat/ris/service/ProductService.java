package com.projekat.ris.service;

import com.projekat.ris.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO getProductByName(String name);
    ProductDTO addProduct(ProductDTO productDTO);
    ProductDTO updateProduct(ProductDTO productDTO);
    void deleteProductById(Long id);
}
