package com.example.demo.src.products.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductInfoMDI {
    private int productIdx;
    private String productName;
    private String detailImagePath;
    private String detailProductName;
    private String productType;
    private String wrapUnitweight;
    private String certified;
    private String relatedLaw;
    private String origin;
    private String storageWay;
    private String productWarn;
    private String expirationDate;
    private String customServiceNumber;
    private int stock;
    private String bookmarkStatus;
}
