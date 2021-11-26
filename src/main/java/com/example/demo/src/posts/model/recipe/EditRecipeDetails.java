package com.example.demo.src.posts.model.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class EditRecipeDetails {

    private String title;
    private String ingredients;
    private String contents;
    private String detailFileUrl;
    private String fileType;
    private String status;
}
