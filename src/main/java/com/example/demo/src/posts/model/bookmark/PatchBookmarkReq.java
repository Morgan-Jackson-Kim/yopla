package com.example.demo.src.posts.model.bookmark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchBookmarkReq {
    private int userId;
    private int recipeId;
    private String type;
}
