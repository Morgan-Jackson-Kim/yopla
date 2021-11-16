package com.example.demo.src.posts.model.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class NewRecipeDetails {

    private String title;
    private String ingredients;
    private String contents;
    private String detailFileUrl;


}
