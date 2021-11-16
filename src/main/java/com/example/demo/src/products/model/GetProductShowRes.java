package com.example.demo.src.products.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductShowRes {
    private int productsIdx;
    private String productName;
    private String productImagePath;
    private int discount;
    private int basePrice;
    private String kurlyOnly;
}
