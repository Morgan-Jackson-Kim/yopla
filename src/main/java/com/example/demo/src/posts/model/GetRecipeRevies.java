package com.example.demo.src.posts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class GetRecipeRevies {
    private int reviewsIdx;
    private String userNickName;
    private String userPI;
    private String content;
    private float reviewScore;
    private String createdAt;
}
