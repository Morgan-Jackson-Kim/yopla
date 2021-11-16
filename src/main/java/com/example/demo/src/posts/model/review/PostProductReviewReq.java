package com.example.demo.src.posts.model.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProductReviewReq {
    private int userId;
    private int productId;
    private String imagePath;
    private String title;
    private String content;
}
