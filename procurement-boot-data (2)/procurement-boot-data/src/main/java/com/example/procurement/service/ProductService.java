package com.example.procurement.service;

import com.example.procurement.model.ImageProduct;
import com.example.procurement.model.Product;
import com.example.procurement.repository.ImageProductRepository;
import com.example.procurement.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;
    private ImageProductRepository imageProductRepository;

    public ProductService(ProductRepository productRepository, ImageProductRepository imageProductRepository) {
        this.productRepository = productRepository;
        this.imageProductRepository = imageProductRepository;
    }

    public List<Product> productList(String title) {
        return title != null ? productRepository.findByTitle(title) : productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow();

    }

    public void saveProduct(Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        List<ImageProduct> images = new ArrayList<>();
        if (file1.getSize() != 0) {
            ImageProduct image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            images.add(image1);
        }
        if (file2.getSize() != 0) {
            ImageProduct image2 = toImageEntity(file2);
            images.add(image2);
        }
        if (file3.getSize() != 0) {
            ImageProduct image3 = toImageEntity(file3);
            images.add(image3);
        }
        log.info("Добавили новый товар. Title: {}; Price: {}", product.getTitle(), product.getPrice());

        Product savedProduct = productRepository.save(product);

        for (ImageProduct image : images) {
            image.setProduct(savedProduct);
            ImageProduct savedImage = imageProductRepository.save(image);
            log.info("Сохранили фото товаров с ID: {}", savedImage.getId());
        }

        savedProduct.setImages(images);
        savedProduct.setPreviewImageId(images.get(0).getId());
        productRepository.save(savedProduct);
    }

    private ImageProduct toImageEntity(MultipartFile file) throws IOException {
        ImageProduct image = new ImageProduct();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
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
