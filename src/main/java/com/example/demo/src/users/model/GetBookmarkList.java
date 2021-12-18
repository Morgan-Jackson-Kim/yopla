package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookmarkList {
    private int bookmarksIdx;
    private int recipeId;
    private String recipeImage;
    private String recipeName;
    private String userNickName;
    private String category;
    private float averageScore;
    private int bookmarkCount;
    private String type;
}
