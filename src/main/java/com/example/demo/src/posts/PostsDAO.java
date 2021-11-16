package com.example.demo.src.posts;

import com.example.demo.config.*;
import com.example.demo.src.posts.model.bookmark.*;
import com.example.demo.src.posts.model.recipe.*;
import com.example.demo.src.posts.model.*;
import com.example.demo.src.posts.model.review.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PostsDAO {

        private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createRecipes(createNewRecipe request) {
        String createRecipesQuery = "insert into recipes (userId, recipeName , recipeFrontImage , category) VALUES (?,?,?,?)";
        Object[] createRecipesParams = new Object[]{request.getUserId(), request.getRecipeName(),request.getFrontImageUrl(),request.getCategory() };
        this.jdbcTemplate.update(createRecipesQuery, createRecipesParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public void createRecipeDetails(int recipeId, String title, String ingredients, String content, String detailsFileUrl){
        String createRecipeDetailsQuery = "insert into recipeDetails (recipeId, title , ingredients ,contents, recipeFiles) VALUES (?,?,?,?,?)";
        Object[] createRecipeDetailsParams = new Object[]{recipeId, title ,ingredients,content,detailsFileUrl };
        this.jdbcTemplate.update(createRecipeDetailsQuery, createRecipeDetailsParams);
    }

    public int checkTag(String tagName){
        String checkDuplicateQuery = "select exists (select tagsIdx from tags where tagName = ?)";
        String checkDuplicateParams = tagName;
        return this.jdbcTemplate.queryForObject(checkDuplicateQuery, int.class, checkDuplicateParams);
    }

    public GetTagIdRes getTagId(String tagName){
        String checkDuplicateQuery = "select tagsIdx from tags where tagName = ?";
        String checkDuplicateParams = tagName;
        return  this.jdbcTemplate.queryForObject(checkDuplicateQuery,
                (rs,rowNum) -> new GetTagIdRes(
                        rs.getInt("tagsIdx")),
                checkDuplicateParams);
    }

    public int addNewTag(String tagName){
        String addNewTagQuery = "insert into tags (tagName) VALUE (?)";
        Object[] addNewTagParams = new Object[]{tagName};
        this.jdbcTemplate.update(addNewTagQuery, addNewTagParams);

        String lastInsertIdQuery = "select last_insert_id() ";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }

    public void linkTag(int tagId, int recipeId){
        String createLinkQuery = "insert into taglinker (recipeId, tagId) VALUES (?,?)";
        Object[] createLinkParams = new Object[]{recipeId,tagId };
        this.jdbcTemplate.update(createLinkQuery, createLinkParams);
    }



    public int createBookmark(PostBookmarkReq postBookmarkReq){
        String createBookmarkQuery  = "insert into rBookmarks (userId , recipeId) VALUES(?,?)";
        Object[] createBookmarkParams = new Object[]{ postBookmarkReq.getUserId() , postBookmarkReq.getRecipeId()};
        return this.jdbcTemplate.update(createBookmarkQuery,createBookmarkParams);
    }

//    public int addReviews(PostProductReviewReq postProductReviewReq){
//        String addReviewsQuery  = "insert into reviews (productId , userId,imagePAth,title,content) VALUES(?,?,?,?,?)";
//        Object[] addReviewsParams = new Object[]{ postProductReviewReq.getProductId(),postProductReviewReq.getUserId() ,postProductReviewReq.getImagePath(),postProductReviewReq.getTitle(),postProductReviewReq.getContent() };
//        this.jdbcTemplate.update(addReviewsQuery,addReviewsParams);
//
//        String lastNumberofReviewQuery = "select last_insert_id()";
//        return this.jdbcTemplate.queryForObject(lastNumberofReviewQuery,int.class);
//
//    }

    public int modifyBookmarkStatus(PatchBookmarkReq patchBookmarkReq,String status){
        String modifyBookmarkStatusQuery  ="update rBookmarks set status = ? where userId = ? && recipeId = ? && status = 'active'";
        Object[] modifyBookmarkStatusParams = new Object[]{status,patchBookmarkReq.getUserId(),patchBookmarkReq.getRecipeId()};
        return this.jdbcTemplate.update(modifyBookmarkStatusQuery,modifyBookmarkStatusParams);
    }

    public int checkBookmarks(int userId , int recipeId){
        String checkBookmarkQuery = "select exists(select status from rBookmarks where status = 'active' && userId = ? && recipeId = ?)";
        Object[] checkBookmarkParams = new Object[]{userId,recipeId};
        return this.jdbcTemplate.queryForObject(checkBookmarkQuery,
                int.class,
                checkBookmarkParams);
    }


}
