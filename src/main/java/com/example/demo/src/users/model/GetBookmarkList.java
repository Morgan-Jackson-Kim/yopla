package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookmarkList {
    private int bookmarksIdx;
    private String ImagePath;
    private String productName;
    private int basePrice;
    private int discount;
}
