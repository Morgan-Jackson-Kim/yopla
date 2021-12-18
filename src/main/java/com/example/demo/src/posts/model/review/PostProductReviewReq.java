package com.example.demo.src.posts.model.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProductReviewReq {
    private int userId;
    private int recipeId;
    private String content;
    private Float point;
    private String type;
}
