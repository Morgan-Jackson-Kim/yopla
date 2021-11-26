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

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public void createRecipeDetails(int recipeId, String title, String ingredients, String content, String detailsFileUrl,String fileType){
        String createRecipeDetailsQuery = "insert into recipeDetails (recipeId, title , ingredients ,contents, recipeFiles , fileType) VALUES (?,?,?,?,?,?)";
        Object[] createRecipeDetailsParams = new Object[]{recipeId, title ,ingredients,content,detailsFileUrl ,fileType };
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

    public List<GetMainAdvertisesRes> getMainAds(){
        String getMainAdvertiseQuery = "select adIdx, adImagePath from advertisementsMain";
        return this.jdbcTemplate.query(getMainAdvertiseQuery,
                (rs,rowNum) -> new GetMainAdvertisesRes(
                        rs.getInt("adIdx"),
                        rs.getString("adImagePath"))
        );
    }

    public List<GetShortsRes> getShorts(int userId){
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes ORDER BY recipes.createdAt DESC limit 8";
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
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes order by hits DESC limit 8";
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
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes order by recipes.createdAt DESC limit 8";
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
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where publicRecipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where publicRecipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where publicRecipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = publicRecipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = publicRecipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from publicRecipes ORDER BY publicRecipes.createdAt DESC limit 8";
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
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where publicRecipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where publicRecipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where publicRecipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = publicRecipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = publicRecipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from publicRecipes order by hits DESC limit 8";
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
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where publicRecipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where publicRecipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where publicRecipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = publicRecipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = publicRecipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from publicRecipes order by publicRecipes.createdAt DESC limit 8";
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
        String  getSearchResultQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId) as bookmarkCount,(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes where recipeName LIKE ? order by createdAt DESC limit 8 ";
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
                "(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount, (select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  , \n" +
                "(select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked\n" +
                "from recipes\n" +
                "join taglinker \n" +
                "join tags\n" +
                "where recipes.recipesIdx = taglinker.recipeId && taglinker.tagId = tags.tagsIdx && tags.tagName LIKE ? order by recipes.createdAt DESC limit 8 ";


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
                "(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId) as bookmarkCount,\n" +
                "(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore  ,\n" +
                " (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked\n" +
                " from recipes where category = ? order by createdAt DESC  ";
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
        String addReviewsQuery  = "insert into reviews (recipeId , userId,scores,content) VALUES(?,?,?,?)";
        Object[] addReviewsParams = new Object[]{ postProductReviewReq.getRecipeId(),postProductReviewReq.getUserId() ,postProductReviewReq.getPoint(),postProductReviewReq.getContent() };
        this.jdbcTemplate.update(addReviewsQuery,addReviewsParams);

        String lastNumberofReviewQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastNumberofReviewQuery,int.class);
    }

    public int editReview(EditReviewReq editReviewReq){
        String addReviewsQuery  = "update reviews set content= ? ,scores = ?,status = ? where userId = ? AND recipeId = ? ; ";
        Object[] addReviewsParams = new Object[]{ editReviewReq.getContent(),editReviewReq.getPoint(),editReviewReq.getStatus(),editReviewReq.getUserId(),editReviewReq.getRecipeId()};
        return this.jdbcTemplate.update(addReviewsQuery,addReviewsParams);

    }



    public int checkAbleWriteReview(int userId, int productId){
        String checkAbleWriteReviewQuery = "select exists(select reviewsIdk from reviews where reviews.status = 'active'  && userId = ? && recipeId = ?)";
        Object[] checkBookmarkParams = new Object[]{userId,productId};
        return this.jdbcTemplate.queryForObject(checkAbleWriteReviewQuery,
                int.class,
                checkBookmarkParams);
    }

    public List<GetReviews> getReviews(int recipeId){
        String  getReviewsQuery = "select reviewsIdk,(select users.userNickName from users where users.usersIdx = reviews.userId) as userNickName, (select users.profileImage from users where users.usersIdx = reviews.userId) as userPI ,content, scores , date_format(reviews.createdAt, '%Y. %c. %d ')createdAt from reviews where  recipeId= ? && status='active' order by reviews.reviewsIdk desc ";
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
        String getShortsQuery = "select recipeDetailIdx,title,ingredients,contents,recipeFiles,fileType from recipeDetails where recipeId = ? ";
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
        String getShortsQuery = "select recipesIdx,recipeName,recipeFrontImage,(select profileImage from users where recipes.userId = users.usersIdx)as usersPI ,(select userNickName from users where recipes.userId = users.usersIdx)as usersNN , hits,(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount,recipes.time,(select group_concat(tagName SEPARATOR ',')  from tags join taglinker where tags.tagsIdx = taglinker.tagId && taglinker.recipeId = recipes.recipesIdx)  as tags , (select exists(select userId from rBookmarks where rBookmarks.userId = ? && rBookmarks.recipeId = recipes.recipesIdx && rBookmarks.status = 'active')) as bookmarked from recipes where recipes.recipesIdx = ?";
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
                        rs.getString("tags"),
                        rs.getBoolean("bookmarked")),
                getShortsParam);
    }

    public void addHitCount(int recipeId){
        String addHitCountsQuery  ="update recipes set hits = hits+1 where recipesIdx = ? AND recipes.status = 'active'";
        Object[] addHitCountParams = new Object[]{recipeId};

        this.jdbcTemplate.update(addHitCountsQuery,addHitCountParams);
    }





}
