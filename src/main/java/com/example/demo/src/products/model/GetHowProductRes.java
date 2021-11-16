package com.example.demo.src.products.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHowProductRes {
    private int productsIdx;
    private String productName;
    private String productImagePath;
    private int discount;
    private int basePrice;
}
