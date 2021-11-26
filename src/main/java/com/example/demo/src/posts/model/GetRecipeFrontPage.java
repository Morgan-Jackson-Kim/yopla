package com.example.demo.src.posts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRecipeFrontPage {
    private int recipeIdx;
    private String title;
    private String recipeImage;
    private String userProfileImage;
    private String userNickName;
    private int hits;
    private int bookmarkCount;
    private String times;
    private String tags;
    private Boolean bookmarked;
}
