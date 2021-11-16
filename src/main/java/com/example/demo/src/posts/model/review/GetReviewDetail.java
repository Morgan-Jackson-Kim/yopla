package com.example.demo.src.posts.model.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewDetail {
    private int reviewsIdk;
    private String productName;
    private String title;
    private String imagePath;
    private String content;
    private String createdAt;
}
