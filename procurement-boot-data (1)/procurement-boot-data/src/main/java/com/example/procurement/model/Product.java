package com.example.procurement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
