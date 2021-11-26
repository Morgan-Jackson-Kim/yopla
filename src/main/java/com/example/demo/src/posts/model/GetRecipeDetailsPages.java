package com.example.demo.src.posts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRecipeDetailsPages {
    private int recipeDetailsIdx;
    private String title;
    private String ingredients;
    private String contents;
    private String detailFileUrl;
    private String fileType;
}
