package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMyRR {
    private int reviewsIdx;
    private String userNickName;
    private String userPI;
    private String content;
    private float reviewScore;
    private String recipeName;
    private String createdAt;
}
