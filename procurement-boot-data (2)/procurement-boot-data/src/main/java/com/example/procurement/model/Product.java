package com.example.procurement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "descriptions")
    private String descriptions;

    @Column(name = "price")
    private int price;

    @Column(name = "name")
    private String name;

    @Column(name = "number")
    private String number;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
    mappedBy = "product")
    private List<ImageProduct> images = new ArrayList<>();
    private Long previewImageId;
    private LocalDateTime dateOfCreated;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }
    //Аннотация @PrePersist указывает, что метод должен быть выполнен перед операцией сохранения
    // (persist) сущности в базе данных.
    // В данном случае, метод init() будет вызван перед сохранением сущности.

    public void addImageToProduct(ImageProduct image) {
        image.setProduct(this);
        images.add(image);
    }
}


