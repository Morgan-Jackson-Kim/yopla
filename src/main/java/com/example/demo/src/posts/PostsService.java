package com.example.demo.src.posts;

import com.example.demo.config.BaseException;
import com.example.demo.src.posts.model.bookmark.*;
import com.example.demo.src.posts.model.recipe.*;
import com.example.demo.src.posts.model.*;
import com.example.demo.src.posts.model.review.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class PostsService {

        final Logger logger = LoggerFactory.getLogger(this.getClass());

        private final PostsDAO postsDAO;
        private final PostsProvider postsProvider;
        private final JwtService jwtService;

        @Autowired
        public PostsService(PostsDAO postsDAO,  PostsProvider postsProvider, JwtService jwtService){
                this.postsDAO = postsDAO;
                this.postsProvider = postsProvider;
                this.jwtService = jwtService;
        }

        public int CreateNewRecipes(createNewRecipe request) throws BaseException{
                try {
                        int recipeId ;
//                        if(request.getTags() == null){
//                                recipeId = postsDAO.createRecipes(request);
//
//                                return recipeId;
//
//                        }else {

                                recipeId = postsDAO.createRecipes(request);

                                for(int i = 0 ; i < request.getTags().size(); i ++){
                                        String nTageName = request.getTags().get(i);
                                        int checkedTagId = postsDAO.checkTag(nTageName);
                                        if(checkedTagId == 0){
                                                int newTagId = postsDAO.addNewTag(nTageName);
                                                postsDAO.linkTag(newTagId,recipeId);
                                        }else {
                                                GetTagIdRes getTagIdRes = postsDAO.getTagId(nTageName);
                                                int existTagId = getTagIdRes.getTagId();
                                                postsDAO.linkTag(existTagId,recipeId);
                                        }
                                }

//                                for(String tags : request.getTags()){
//                                        String nTageName = tags;
//                                        int checkedTagId = postsDAO.checkTag(nTageName);
//                                        if(checkedTagId == 0){
//                                                int newTagId = postsDAO.addNewTag(nTageName);
//                                                postsDAO.linkTag(newTagId,recipeId);
//                                        }else {
//                                                int existTagId = postsDAO.getTagId(nTageName);
//                                                postsDAO.linkTag(existTagId,recipeId);
//                                        }
//
//                                }
//                                request.getTags().stream().forEach( tags -> {
//                                        String nTageName = tags.getTagName();
//                                        int checkedTagId = postsDAO.checkTag(nTageName);
//
//                                        if( checkedTagId > 0 ){
//                                               postsDAO.linkTag(checkedTagId,recipeId);
//
//                                        }else {
//                                               int newTagId = postsDAO.addNewTag(nTageName);
//                                               postsDAO.linkTag(newTagId,recipeId);
//                                        }
//                                });
                                return recipeId;
//                        }
                }catch (Exception exception) {
                        throw new BaseException(TEST_ERROR);
                }
        }

        public String addRecipeDetails(RecipeDetailsList request) throws BaseException{
                int recipeId = request.getRecipeId();
                try {
                        String result ;
                        request.getNewRecipeDetails().stream().forEach(newRecipeDetails -> {
                                String title = newRecipeDetails.getTitle();
                                String ingredients = newRecipeDetails.getIngredients();
                                String content = newRecipeDetails.getContents();
                                String fileUrl = newRecipeDetails.getDetailFileUrl();

                                postsDAO.createRecipeDetails(recipeId,title,ingredients,content,fileUrl);
                        });

                        result = "success";

                        return result;

                }catch (Exception exception) {
                        throw new BaseException(DATABASE_ERROR);
                }

//                        for(NewRecipeDetails recipeDetails : request.getNewRecipeDetails()){
//                                String title = recipeDetails.getTitle();
//                                String ingredients = recipeDetails.getIngredients();
//                                String content = recipeDetails.getContents();
//                                MultipartFile files = recipeDetails.getRecipeFile();
//                                String detailsFileUrl = s3Uploader.upload(files,"recipeDetails");
//                                postsDAO.createRecipeDetails(recipeId,title,ingredients,content,detailsFileUrl);
//                        }


        }

        public void createBookmark(PostBookmarkReq postBookmarkReq)throws BaseException{
                if(postsProvider.checkBookmark(postBookmarkReq) == 1){
                    throw new BaseException(POST_USERS_EXISTS_BOOKMARK);
                }
                try{
                    int result = postsDAO.createBookmark(postBookmarkReq);

                    if(result == 0){
                        throw new BaseException(INSERT_FAIL_POSTBOOKMARK);
                    }
                }catch (Exception exception) {
                    throw new BaseException(TEST_ERROR);
                }
        }

        public void modifyBookmarkStatus(PatchBookmarkReq patchBookmarkReq) throws BaseException{
                try{
                        String status = "disable";
                        int result = postsDAO.modifyBookmarkStatus(patchBookmarkReq,status);
                        if(result == 0){
                        throw new BaseException(MODIFY_FAIL_BOOKMARK);
                        }
                } catch (Exception exception){
                    throw new BaseException(DATABASE_ERROR);
                }
        }
}




