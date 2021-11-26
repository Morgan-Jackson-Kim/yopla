package com.example.demo.src.posts.model.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
//@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class createNewRecipe {

    private int userId;
    private String recipeName;
    private String category;
    private String frontImageUrl;
    private String time;
    private ArrayList<String> tags ;

}