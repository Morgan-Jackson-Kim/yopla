package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BuyProductsInfo {
    private int productId;
    private int basePrice;
    private int paidPrice;
    private int buyUnits;
}
