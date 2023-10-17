package com.example.procurement.controllers;


import com.example.procurement.model.Product;
import com.example.procurement.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/") // поиск
    public String showProduct(@RequestParam(name = "title", required = false) String title, Model model) {
        List<Product> productList;
        if (title != null && !title.isEmpty()) {
            productList = productService.productList(title);
        } else {
            productList = productService.productList(null);
        }
        if (productList.isEmpty()) {
            model.addAttribute("errorMessage", "Товаров нет");
        } else {
            model.addAttribute("products", productList);
        }
        return "products";
    }

    @GetMapping("/product/{id}")
    @Transactional
    public String showInfoProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            model.addAttribute("errorMessage", "Товар не найден");
            return "error";
        }
        Hibernate.initialize(product.getImages()); // Метод Hibernate.initialize() принудительно инициализирует
        // ленивую коллекцию images перед ее использованием
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "infoProduct";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product) throws IOException {
        if (file1.isEmpty() && file2.isEmpty() && file3.isEmpty()) {
            // Обработка случая, когда не загружено ни одного изображения
            return "error";
        }

        productService.saveProduct(product, file1, file2, file3);
        return "redirect:/";
    }


    @PostMapping("product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            // Обработка случая, когда товар не найден
            return "error";
        }

        productService.deleteProducts(id);
        return "redirect:/";
    }

    @PostMapping("product/change/{id}")
    public String changeProduct(@PathVariable Long id, Product product) {
        try {
            log.info("Измененный продукт: {}", product);
            Product existingProduct = productService.getProductById(id);
            if (existingProduct == null) {
                // Обработка случая, когда товар не найден
                return "error";
            }
            // Обновляем данные товара
            existingProduct.setTitle(product.getTitle());
            existingProduct.setDescriptions(product.getDescriptions());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setName(product.getName());
            existingProduct.setNumber(product.getNumber());
            productService.update(existingProduct);
        } catch (Exception e) {
            log.error("Ошибка при изменении товара: {}", e.getMessage());
            return "error";
        }
        return "redirect:/";
    }
}
