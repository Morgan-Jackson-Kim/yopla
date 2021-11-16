package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coupons {

    private int couponsIdx;
    private int productId;
    private String cName;
    private int discountPercent;
    private int discountMoney;
    private String status;
}
