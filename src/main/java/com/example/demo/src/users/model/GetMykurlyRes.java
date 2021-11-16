package com.example.demo.src.users.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMykurlyRes {

    private int userIdx;
    private String rank;
    private String name;
    private int savemoney;
    private int couponCount;
    private int bookmarks;
    private String address;

}
