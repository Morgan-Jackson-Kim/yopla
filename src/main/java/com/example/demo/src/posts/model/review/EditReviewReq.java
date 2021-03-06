package com.example.demo.src.posts.model.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EditReviewReq {
    private int userId;
    private int reviewsIdx;
    private String content;
    private Float point;
    private String status;
}
