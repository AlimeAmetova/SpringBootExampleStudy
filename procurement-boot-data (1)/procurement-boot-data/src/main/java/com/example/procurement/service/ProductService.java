package com.example.procurement.service;

import com.example.procurement.model.Product;
import com.example.procurement.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> productList(String title) {
        return title != null ? productRepository.findByTitle(title) : productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow();

    }

    public void saveProduct(Product product) {
        productRepository.save(product);
        log.info("Продукт сохранен: {}", product);
    }


    public void deleteProducts(Long id) {
        productRepository.deleteById(id);
        log.info("Продукт удален с id: {}", id);

    }

    public void update(Product product) {
        Product updatedProduct = productRepository.save(product);
        log.info("Продукт изменен(сохранен): {}", updatedProduct);
    }
}
