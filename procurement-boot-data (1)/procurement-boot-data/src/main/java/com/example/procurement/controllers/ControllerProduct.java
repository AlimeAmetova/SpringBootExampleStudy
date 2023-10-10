package com.example.procurement.controllers;


import com.example.procurement.model.Product;
import com.example.procurement.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class ControllerProduct {

    private final ProductService productService;

    public ControllerProduct(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
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
    public String showInfoProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "infoProduct";
    }

    @PostMapping("/product/create")
    public String createProduct(Product product) {
        productService.saveProduct(product);
        return "redirect:/";
    }

    @PostMapping("product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProducts(id);
        return "redirect:/";
    }

    @PostMapping("product/change/{id}")
    public String changeProduct(@PathVariable Long id, Product product) {
        try {
            log.info("Измененный продукт: {}", product);
            Product existingProduct = productService.getProductById(id);
            // Обновляем данные товара
            existingProduct.setTitle(product.getTitle());
            existingProduct.setDescriptions(product.getDescriptions());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setName(product.getName());
            existingProduct.setNumber(product.getNumber());
            productService.update(existingProduct);
        } catch (Exception e) {
            log.error("Ошибка при изменении товара: {}", e.getMessage());
        }
        return "redirect:/";
    }
}
