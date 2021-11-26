package com.example.demo.src.posts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchRes {
    private int recipeIdx;
    private String title;
    private String recipeImage;
    private String userProfileImage;
    private String userNickName;
    private int hits;
    private int bookmarkCount;
    private float averageScore;
    private Boolean bookmarked;
}
