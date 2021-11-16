package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPurchaedInfo {
    private int invoiceIdx;
    private String productName;
    private String productImagePath;
    private int basePrice;
    private int paidPrice;
    private int Stocks;
    private String shippment;
}
