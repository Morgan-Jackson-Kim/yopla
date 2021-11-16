package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceUserInfo {
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private int couponCount;
    private int saveMoney;
    private String rank;
}
