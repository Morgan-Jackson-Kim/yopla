package com.example.demo.src.posts.model.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetReviews {
    private int reviewsIdx;
    private String title;
    private String name;
    private int existImage;
    private String createdAt;
    private String isItBest;
}
