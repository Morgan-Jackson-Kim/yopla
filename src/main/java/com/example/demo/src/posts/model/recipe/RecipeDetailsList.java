package com.example.demo.src.posts.model.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RecipeDetailsList {
    private int recipeId;
    private List<NewRecipeDetails> newRecipeDetails;
}
