package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCartList {
    private int productsIdx;
    private String productName;
    private String productImagePath;
    private int discount;
    private int basePrice;
    private String shippingType;
    private String wrappingType;
    private int cartstock;
}
