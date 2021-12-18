package com.example.demo.src.posts;

import com.example.demo.config.*;
import com.example.demo.src.posts.model.bookmark.*;
import com.example.demo.src.posts.model.recipe.*;
import com.example.demo.src.posts.model.*;
import com.example.demo.src.posts.model.review.*;
import com.example.demo.src.products.model.GetProductShowRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostsDAO {

        private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createRecipes(createNewRecipe request) {
        String createRecipesQuery = "insert into recipes (userId, recipeName , recipeFrontImage ,recipes.time, category) VALUES (?,?,?,?,?)";
        Object[] createRecipesParams = new Object[]{request.getUserId(), request.getRecipeName(),request.getFrontImageUrl(),request.getTime(),request.getCategory() };
        this.jdbcTemplate.update(createRecipesQuery, createRecipesParams);

        String addPointsQuery = "update users set userGrade = userGrade + 200 where usersIdx = ? ";
        Object[] addPointsParams = new Object[]{request.getUserId()};
        this.jdbcTemplate.update(addPointsQuery, addPointsParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public void createRecipeDetails(int recipeId, String title, String ingredients, String content, String detailsFileUrl,String fileType){
        String createRecipeDetailsQuery = "insert into recipeDetails (recipeId, title , ingredients ,contents, recipeFiles , fileType) VALUES (?,?,?,?,?,?)";
        Object[] createRecipeDetailsParams = new Object[]{recipeId, title ,ingredients,content,detailsFileUrl ,fileType };
        this.jdbcTemplate.update(createRecipeDetailsQuery, createRecipeDetailsParams);
    }

    public void PatchNewRecipeDetails(int recipeId, String title, String ingredients, String content, String detailsFileUrl,String fileType){
        String createRecipeDetailsQuery = "insert into recipeDetails (recipeId, title , ingredients ,contents, recipeFiles , fileType) VALUES (?,?,?,?,?,?)";
        Object[] createRecipeDetailsParams = new Object[]{recipeId, title ,ingredients,content,detailsFileUrl ,fileType };
        this.jdbcTemplate.update(createRecipeDetailsQuery, createRecipeDetailsParams);
    }


    //레시피 수정 부분
    public void patchRecipes(PatchRecipe request) {
        String createRecipesQuery = "update recipes set recipeName = ? , recipeFrontImage = ? , recipes.time = ? , category = ?  where recipesIdx = ? ";
        Object[] createRecipesParams = new Object[]{request.getRecipeName(),request.getFrontImageUrl(),request.getTime(),request.getCategory(),request.getRecipeId() };
        this.jdbcTemplate.update(createRecipesQuery, createRecipesParams);

        String disableTagLinksQuery = "delete from taglinker where recipeId = ?  ";
        Object[] disableTagLinksParams = new Object[]{request.getRecipeId()};
        this.jdbcTemplate.update(disableTagLinksQuery, disableTagLinksParams);
    }

    public void deleteRecipeDetails(int recipeId){
        String createRecipeDetailsQuery = "update recipeDetails set status = 'disable' where recipeId = ? ";
        Object[] createRecipeDetailsParams = new Object[]{recipeId};
        this.jdbcTemplate.update(createRecipeDetailsQuery, createRecipeDetailsParams);
    }
    //수정 끝


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



    public int deleteRecipe(DeleteRecipe deleteRecipe){
        String deleteRecipQuery  = "update recipes,recipeDetails set recipes.status = 'disable', recipeDetails.status = 'disable'  where recipeDetails.recipeId = recipes.recipesIdx AND recipeId = ? ";
        Object[] deleteRecipParams = new Object[]{ deleteRecipe.getRecipeId()};
        return this.jdbcTemplate.update(deleteRecipQuery,deleteRecipParams);
    }

    public int createBookmark(PostBookmarkReq postBookmarkReq){
        String createBookmarkQuery  = "insert into rBookmarks (userId , recipeId,type) VALUES(?,?,?)";
        Object[] createBookmarkParams = new Object[]{ postBookmarkReq.getUserId() , postBookmarkReq.getRecipeId(),postBookmarkReq.getType()};
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
        String modifyBookmarkStatusQuery  ="update rBookmarks set status = ? where userId = ? AND recipeId = ? AND status = 'active' AND type = ? ";
        Object[] modifyBookmarkStatusParams = new Object[]{status,patchBookmarkReq.getUserId(),patchBookmarkReq.getRecipeId(),patchBookmarkReq.getType()};
        return this.jdbcTemplate.update(modifyBookmarkStatusQuery,modifyBookmarkStatusParams);
    }

    public int checkBookmarks(int userId , int recipeId,String type ){
        String checkBookmarkQuery = "select exists(select status from rBookmarks where status = 'active' AND userId = ? && recipeId = ? AND type = ?)";
        Object[] checkBookmarkParams = new Object[]{userId,recipeId,type};
        return this.jdbcTemplate.queryForObject(checkBookmarkQuery,
                int.class,
                checkBookmarkParams);
    }


    public int checkRecipeExist(DeleteRecipe deleteRecipe){
        String checkRecipeQuery = "select exists(select status from recipes where status = 'active' AND recipesIdx = ? AND userId = ? )";
        Object[] checkRecipeParams = new Object[]{deleteRecipe.getRecipeId(), deleteRecipe.getUserId()};
        return this.jdbcTemplate.queryForObject(checkRecipeQuery,
                int.class,
                checkRecipeParams);
    }

    public List<GetMainAdvertisesRes> getMainAds(){
        String getMainAdvertiseQuery = "select adIdx, adImagePath from advertisementsMain";
        return this.jdbcTemplate.query(getMainAdvertiseQuery,
                (rs,rowNum) -> new GetMainAdvertisesRes(
                        rs.getInt("adIdx"),
                        rs.getString("adImagePath"))
        );
    }

    public List<GetShortsRes> getShorts(int userId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes join recipeDetails where recipesIdx = recipeDetails.recipeId AND recipeDetails.fileType = 'video' AND recipes.status = 'active' group by recipesIdx ORDER BY recipes.createdAt DESC limit 8";
        int getShortsParam = userId;
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetShortsRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                        getShortsParam);

    }

    public List<GetHotsRes> getHots(int userId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes where recipes.status = 'active' order by hits DESC limit 8";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetHotsRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }

    public List<GetShortsRes> getmoreShorts(int userId){
        String getShortsQuery = " select recipesIdx,recipeName,recipeFrontImage,\n" +
                " (select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                " (select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,\n" +
                " (select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,\n" +
                " (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked \n" +
                " from recipes join recipeDetails where recipesIdx = recipeDetails.recipeId AND recipeDetails.fileType = 'video' AND recipes.status = 'active' group by recipesIdx  order by recipes.createdAt desc";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetShortsRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }

    public List<GetHotsRes> getmoreHots(int userId){
        String getShortsQuery = " select recipesIdx,recipeName,recipeFrontImage,\n" +
                " (select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                " (select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,\n" +
                " (select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,\n" +
                " (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked \n" +
                " from recipes where recipes.status = 'active' order by hits";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetHotsRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }


    public List<GetRecommendRes> getRecommends(int userId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes where recipes.status = 'active' order by rand() DESC limit 8";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetRecommendRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }
    // 대중 레시피 gets 들
    public List<GetShortsRes> getpubShorts(int userId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes join recipeDetails where recipesIdx = recipeDetails.recipeId AND recipeDetails.fileType = 'video' AND recipes.status = 'active' group by recipesIdx ORDER BY recipes.createdAt DESC limit 8";
        int getShortsParam = userId;
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetShortsRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }

    public List<GetHotsRes> getpubHots(int userId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,\n" +
                "(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                "(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,\n" +
                "(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,\n" +
                " (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  ,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes ORDER BY bookmarkCount desc limit 8";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetHotsRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }

    public List<GetRecommendRes> getpubRecommends(int userId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,\n" +
                "(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                "(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,\n" +
                "(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,\n" +
                " (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  ,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes ORDER BY averageScore desc limit 8";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetRecommendRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }

    public List<GetShortsRes> getPublicMoreShorts(int userId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,\n" +
                " (select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                " (select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,\n" +
                " (select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,\n" +
                " (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked \n" +
                " from recipes join recipeDetails where recipesIdx = recipeDetails.recipeId AND recipeDetails.fileType = 'video' AND recipes.status = 'active' group by recipesIdx  order by recipes.createdAt desc";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetShortsRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }

    public List<GetHotsRes> getPublicMoreHots(int userId){
        String getShortsQuery = " select recipesIdx,recipeName,recipeFrontImage,\n" +
                "(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                "(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,\n" +
                "(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,\n" +
                " (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  ,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes where recipes.status = 'active'  ORDER BY bookmarkCount desc ";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetHotsRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }

    public List<GetRecommendRes> getPublicMoreRecoomend(int userId){
        String getShortsQuery = " select recipesIdx,recipeName,recipeFrontImage,\n" +
                "(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                "(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,\n" +
                "(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,\n" +
                " (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  ,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes where recipes.status = 'active' ORDER BY averageScore desc ";
        Object[] getShortsParam = new Object[]{userId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetRecommendRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);

    }



    // 대중레시피 업로드 전용
    public int createPublicRecipes(createNewRecipe request) {
        String createRecipesQuery = "insert into publicRecipes (userId, recipeName , recipeFrontImage ,recipes.time, category) VALUES (?,?,?,?,?)";
        Object[] createRecipesParams = new Object[]{request.getUserId(), request.getRecipeName(),request.getFrontImageUrl(),request.getTime(),request.getCategory() };
        this.jdbcTemplate.update(createRecipesQuery, createRecipesParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public void createPublicRecipeDetails(int recipeId, String title, String ingredients, String content, String detailsFileUrl,String fileType){
        String createRecipeDetailsQuery = "insert into publicRecipeDetails (recipeId, title , ingredients ,contents, recipeFiles , fileType) VALUES (?,?,?,?,?,?)";
        Object[] createRecipeDetailsParams = new Object[]{recipeId, title ,ingredients,content,detailsFileUrl ,fileType };
        this.jdbcTemplate.update(createRecipeDetailsQuery, createRecipeDetailsParams);
    }
    //대중 레시피 업로드 끝

    public List<GetSearchRes> getSearchResultBytitle(String input,int userId){
        String  getSearchResultQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId) as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes where recipeName LIKE ?  AND recipes.status = 'active' order by createdAt DESC limit 8 ";
        int getSearchResultByUserIDParam = userId;
        String getSearchResultByInputParam = "%" + input + "%";

        return this.jdbcTemplate.query(getSearchResultQuery,
                (rs,rowNum) -> new GetSearchRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getSearchResultByUserIDParam,
                getSearchResultByInputParam);
    }


    public List<GetSearchRes> getSearchResultByTag(String input,int userId){
        String  getSearchResultQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                "(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active' AND rBookmarks.type = 'people') as bookmarkCount, (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , \n" +
                "(select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked\n" +
                "from recipes\n" +
                "join taglinker \n" +
                "join tags\n" +
                "where recipes.recipesIdx = taglinker.recipeId AND recipes.status = 'active' && taglinker.tagId = tags.tagsIdx && tags.tagName LIKE ? order by recipes.createdAt DESC limit 8 ";


        int getSearchResultByUserIDParam = userId;
        String getSearchResultByInputParam = "%" + input + "%";

        return this.jdbcTemplate.query(getSearchResultQuery,
                (rs,rowNum) -> new GetSearchRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getSearchResultByUserIDParam,
                getSearchResultByInputParam);
    }

    public List<GetSearchRes> getCategoriesResult(String input,int userId){
        String  getSearchResultQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,\n" +
                "(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,\n" +
                "(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId AND rBookmarks.type = 'people') as bookmarkCount,\n" +
                "(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  ,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked\n" +
                " from recipes where category = ? AND recipes.status = 'active' order by createdAt DESC  ";
        int getSearchResultByUserIDParam = userId;
        String getSearchResultByInputParam =  input;

        return this.jdbcTemplate.query(getSearchResultQuery,
                (rs,rowNum) -> new GetSearchRes(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getFloat("averageScore"),
                        rs.getBoolean("bookmarked")),
                getSearchResultByUserIDParam,
                getSearchResultByInputParam);
    }


    public int addReviews(PostProductReviewReq postProductReviewReq){
        String addReviewsQuery  = "insert into reviews (recipeId , userId,scores,content,type) VALUES(?,?,?,?,?)";
        Object[] addReviewsParams = new Object[]{ postProductReviewReq.getRecipeId(),postProductReviewReq.getUserId() ,postProductReviewReq.getPoint(),postProductReviewReq.getContent(),postProductReviewReq.getType() };
        this.jdbcTemplate.update(addReviewsQuery,addReviewsParams);

        String addPointsQuery = "update users set userGrade = userGrade + 20 where usersIdx = ? ";
        Object[] addPointsParams = new Object[]{postProductReviewReq.getUserId()};
        this.jdbcTemplate.update(addPointsQuery, addPointsParams);

        String lastNumberofReviewQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastNumberofReviewQuery,int.class);
    }

    public int editReview(EditReviewReq editReviewReq){
        String addReviewsQuery  = "update reviews set content= ? ,scores = ?,status = ? where  reviewsIdk = ?  ";
        Object[] addReviewsParams = new Object[]{ editReviewReq.getContent(),editReviewReq.getPoint(),editReviewReq.getStatus(),editReviewReq.getReviewsIdx()};
        return this.jdbcTemplate.update(addReviewsQuery,addReviewsParams);

    }



    public int checkAbleWriteReview(int userId, int productId ,String type){
        String checkAbleWriteReviewQuery = "select exists(select reviewsIdk from reviews where reviews.status = 'active'  AND userId = ? AND recipeId = ? AND type = ?)";
        Object[] checkBookmarkParams = new Object[]{userId,productId,type};
        return this.jdbcTemplate.queryForObject(checkAbleWriteReviewQuery,
                int.class,
                checkBookmarkParams);
    }

    public List<GetReviews> getReviews(int recipeId){
        String  getReviewsQuery = "select reviewsIdk,(select users.userNickName from users where users.usersIdx = reviews.userId) as userNickName, (select users.profileImage from users where users.usersIdx = reviews.userId) as userPI ,content, scores , date_format(reviews.createdAt, '%Y. %c. %d ')createdAt from reviews where  recipeId= ? AND type = 'people' AND status='active' order by reviews.reviewsIdk desc ";
        Object[] getReviewsParams = new Object[]{recipeId};
        return this.jdbcTemplate.query(getReviewsQuery,
                (rs,rowNum) -> new GetReviews(
                        rs.getInt("reviewsIdk"),
                        rs.getString("userNickName"),
                        rs.getString("userPI"),
                        rs.getString("content"),
                        rs.getFloat("scores"),
                        rs.getString("createdAt")),
                getReviewsParams);
    }

    public List<GetReviews> getpublicReviews(int recipeId){
        String  getReviewsQuery = "select reviewsIdk,(select users.userNickName from users where users.usersIdx = reviews.userId) as userNickName, (select users.profileImage from users where users.usersIdx = reviews.userId) as userPI ,content, scores , date_format(reviews.createdAt, '%Y. %c. %d ')createdAt from reviews where  recipeId= ? AND type ='public' AND status='active' order by reviews.reviewsIdk desc ";
        Object[] getReviewsParams = new Object[]{recipeId};
        return this.jdbcTemplate.query(getReviewsQuery,
                (rs,rowNum) -> new GetReviews(
                        rs.getInt("reviewsIdk"),
                        rs.getString("userNickName"),
                        rs.getString("userPI"),
                        rs.getString("content"),
                        rs.getFloat("scores"),
                        rs.getString("createdAt")),
                getReviewsParams);
    }

    public List<GetRecipeDetailsPages> getRecipeDPs(int recipeId){
        String getShortsQuery = "select recipeDetailIdx,title,ingredients,contents,recipeFiles,fileType from recipeDetails where recipeId = ? AND status = 'active'";
        Object[] getShortsParam = new Object[]{recipeId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetRecipeDetailsPages(
                        rs.getInt("recipeDetailIdx"),
                        rs.getString("title"),
                        rs.getString("ingredients"),
                        rs.getString("contents"),
                        rs.getString("recipeFiles"),
                        rs.getString("fileType")),
                getShortsParam);

    }

    public GetRecipeFrontPage getRecipeFP(int userId ,int recipeId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active' AND rBookmarks.type = 'people') as bookmarkCount,recipes.time,recipes.category,(select group_concat(tagName SEPARATOR ',')  from tags join taglinker where tags.tagsIdx = taglinker.tagId AND taglinker.recipeId = recipes.recipesIdx AND taglinker.type = 'people')  as tags , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes where recipes.recipesIdx = ? AND recipes.status = 'active'";
        Object[] getShortsParam = new Object[]{userId ,recipeId};
        return this.jdbcTemplate.queryForObject(getShortsQuery,
                (rs,rowNum) -> new GetRecipeFrontPage(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getString("recipes.time"),
                        rs.getString("recipes.category"),
                        rs.getString("tags"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);
    }

    public void addHitCount(int recipeId){
        String addHitCountsQuery  ="update recipes set hits = hits+1 where recipesIdx = ? AND recipes.status = 'active'";
        Object[] addHitCountParams = new Object[]{recipeId};

        this.jdbcTemplate.update(addHitCountsQuery,addHitCountParams);
    }



    //대중레시피 전용

    public List<GetRecipeDetailsPages> getpublicRecipeDPs(int recipeId){
        String getShortsQuery = "select recipeDetailIdx,title,ingredients,contents,recipeFiles,fileType from publicRecipeDetails where recipeId = ? ";
        Object[] getShortsParam = new Object[]{recipeId};
        return this.jdbcTemplate.query(getShortsQuery,
                (rs,rowNum) -> new GetRecipeDetailsPages(
                        rs.getInt("recipeDetailIdx"),
                        rs.getString("title"),
                        rs.getString("ingredients"),
                        rs.getString("contents"),
                        rs.getString("recipeFiles"),
                        rs.getString("fileType")),
                getShortsParam);

    }

    public GetRecipeFrontPage getpublicRecipeFP(int userId ,int recipeId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where publicRecipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where publicRecipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where publicRecipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active' AND rBookmarks.type = 'public') as bookmarkCount,publicRecipes.time,publicRecipes.category,(select group_concat(tagName SEPARATOR ',')  from tags join taglinker where tags.tagsIdx = taglinker.tagId AND taglinker.recipeId = publicRecipes.recipesIdx AND taglinker.type = 'public')  as tags , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = publicRecipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from publicRecipes where publicRecipes.recipesIdx = ?";
        Object[] getShortsParam = new Object[]{userId ,recipeId};
        return this.jdbcTemplate.queryForObject(getShortsQuery,
                (rs,rowNum) -> new GetRecipeFrontPage(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeName"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("usersPI"),
                        rs.getString("usersNN"),
                        rs.getInt("hits"),
                        rs.getInt("bookmarkCount"),
                        rs.getString("publicRecipes.time"),
                        rs.getString("publicRecipes.category"),
                        rs.getString("tags"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);
    }

    public void addpublicHitCount(int recipeId){
        String addHitCountsQuery  ="update publicRecipes set hits = hits+1 where recipesIdx = ? AND publicRecipes.status = 'active'";
        Object[] addHitCountParams = new Object[]{recipeId};

        this.jdbcTemplate.update(addHitCountsQuery,addHitCountParams);
    }
    // 대중 레시피 전용 끝


}
