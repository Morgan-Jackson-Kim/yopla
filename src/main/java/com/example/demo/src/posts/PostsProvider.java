package com.example.demo.src.posts;

import org.springframework.stereotype.Service;
import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;

import com.example.demo.src.posts.model.bookmark.*;
import com.example.demo.src.posts.model.recipe.*;
import com.example.demo.src.posts.model.*;
import com.example.demo.src.posts.model.review.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class PostsProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostsDAO postsDAO;

    private final JwtService jwtService;

    @Autowired
    public PostsProvider(PostsDAO postsDAO, JwtService jwtService){
        this.postsDAO = postsDAO;
        this.jwtService = jwtService;
    }

    public int checkBookmark(PostBookmarkReq postBookmarkReq) throws BaseException{
        try{
            return postsDAO.checkBookmarks(postBookmarkReq.getUserId(),postBookmarkReq.getRecipeId());

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
