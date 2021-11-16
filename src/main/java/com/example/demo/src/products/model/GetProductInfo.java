package com.example.demo.src.products.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductInfo {
    private int productIdx;
    private String productName;
    private String productSum;
    private String productImagePath;
    private int discount;
    private int basePrice;
    private String sellingUnit;
    private String weight;
    private String shippingType;
    private String wrapingType;
    private String allegi;
    private String expirationDate;
    private String productGuide;
    private int stock;
    private String bookmarkStatus;
}
