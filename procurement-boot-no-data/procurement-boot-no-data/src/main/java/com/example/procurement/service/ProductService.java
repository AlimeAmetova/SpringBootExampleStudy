package com.example.procurement.service;

import com.example.procurement.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductService {


    private final List<Product> products = new ArrayList<>();
    private long ID = 0;

    {
        products.add(new Product(++ID, "Macbook", "Описание", 190000, "Apple", "+3906899553"));
        products.add(new Product(++ID, "Iphone", "Описание", 78000, "Apple", "+3906899553"));
        products.add(new Product(++ID, "Iphone13 Plus", "Описание", 108500, "Apple", "+3906899553"));
    }

    public Product getProductById(Long id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        throw new IllegalArgumentException("Продукт с заданным id не найден");
    }

    public void saveProduct(Product product) {
        product.setId(++ID);
        products.add(product);
        log.info("Продукт сохранен: {}", product);
    }

    public List<Product> productList() {
        return products;
    }

    public void deleteProducts(Long id) {
        boolean removed = products.removeIf(product -> product.getId().equals(id));
        if (removed) {
            log.info("Продукт удален с id: {}", id);
        } else {
            throw new IllegalArgumentException("Продукт с заданным id не найден");
        }
    }

    public void update(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                break;
            }
        }
        log.info("Продукт изменен(сохранен): {}", product);
    }
}
