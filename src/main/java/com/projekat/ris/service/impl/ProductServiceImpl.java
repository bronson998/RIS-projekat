package com.projekat.ris.service.impl;

import com.projekat.ris.dto.ProductDTO;
import com.projekat.ris.dto.mappers.ProductMapper;
import com.projekat.ris.model.Product;
import com.projekat.ris.repository.ProductRepository;
import com.projekat.ris.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found!"));

        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO getProductByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product Not Found!"));

        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        return productMapper.toDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new RuntimeException("Product Not Found!"));

        Optional.ofNullable(productDTO.getName()).ifPresent(product::setName);
        Optional.of(productDTO.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(productDTO.getDescription()).ifPresent(product::setDescription);
        Optional.of(productDTO.getQuantity()).ifPresent(product::setQuantity);

        Product updated = productRepository.save(product);
        return productMapper.toDTO(updated);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
