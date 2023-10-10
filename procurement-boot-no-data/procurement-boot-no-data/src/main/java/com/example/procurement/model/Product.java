package com.example.procurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private Long id;
    private String title;
    private String descriptions;
    private int price;
    private String name;
    private String number;

}
