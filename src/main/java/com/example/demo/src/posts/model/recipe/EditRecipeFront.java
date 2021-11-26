package com.example.demo.src.posts.model.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
//@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class EditRecipeFront {

    private int userId;
    private int recipeId;
    private String recipeName;
    private String category;
    private String frontImageUrl;
    private String time;
    private ArrayList<String> tags ;
}