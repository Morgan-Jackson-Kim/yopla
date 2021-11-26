package com.example.demo.src.posts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetRecipePage {
    private GetRecipeFrontPage getRecipeFrontPage;
    private List<GetRecipeDetailsPages> getRecipeDetailsPages;
}
