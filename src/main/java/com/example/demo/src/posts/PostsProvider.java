package com.example.demo.src.posts;

import com.example.demo.src.products.model.GetProductInfoMDI;
import com.example.demo.src.products.model.GetProductShowRes;
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



    public int checkRecipe(DeleteRecipe deleteRecipe) throws BaseException{
        try{
            return postsDAO.checkRecipeExist(deleteRecipe);

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMainAdvertisesRes> getMainAds()throws BaseException{
        try {
            List<GetMainAdvertisesRes> getMainAdvertisesRes = postsDAO.getMainAds();
            return  getMainAdvertisesRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //일반인 레시피 전용 gets
    public List<GetShortsRes> getShorts(int userId)throws BaseException{
        try {
            List<GetShortsRes> getShortsRes = postsDAO.getShorts(userId);
            return  getShortsRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetShortsRes> getmoreShorts(int userId)throws BaseException{
        try {
            List<GetShortsRes> getShortsRes = postsDAO.getmoreShorts(userId);
            return  getShortsRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetHotsRes> getHots(int userId)throws BaseException{
        try {
            List<GetHotsRes> getHotsRes = postsDAO.getHots(userId);
            return getHotsRes ;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetHotsRes> getmoreHots(int userId)throws BaseException{
        try {
            List<GetHotsRes> getHotsRes = postsDAO.getmoreHots(userId);
            return getHotsRes ;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetRecommendRes> getRecommends(int userId)throws BaseException{
        try {
            List<GetRecommendRes> getRecommendRes = postsDAO.getRecommends(userId);
            return  getRecommendRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //대중 레시피 전용 gets
    public List<GetShortsRes> getpubShorts(int userId)throws BaseException{
        try {
            List<GetShortsRes> getShortsRes = postsDAO.getpubShorts(userId);
            return  getShortsRes;
        }catch (Exception exception) {
            throw new BaseException(TEST_ERROR2);
        }
    }

    public List<GetHotsRes> getpubHots(int userId)throws BaseException{
        try {
            List<GetHotsRes> getHotsRes = postsDAO.getpubHots(userId);
            return getHotsRes ;
        }catch (Exception exception) {
            throw new BaseException(TEST_ERROR3);
        }
    }

    public List<GetRecommendRes> getpubRecommends(int userId)throws BaseException{
        try {
            List<GetRecommendRes> getRecommendRes = postsDAO.getpubRecommends(userId);
            return  getRecommendRes;
        }catch (Exception exception) {
            throw new BaseException(TEST_ERROR4);
        }
    }

    public List<GetShortsRes> getPublicMoreShorts(int userId)throws BaseException{
        try {
            List<GetShortsRes> getShortsRes = postsDAO.getPublicMoreShorts(userId);
            return  getShortsRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetHotsRes> getPublicMoreHots(int userId)throws BaseException{
        try {
            List<GetHotsRes> getHotsRes = postsDAO.getPublicMoreHots(userId);
            return getHotsRes ;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 대중 끝


    // 검색
    public List<GetSearchRes> GetsearchResult(String input , int userId) throws BaseException{
        try {

            List<GetSearchRes> getSearchRes  = postsDAO.getSearchResultBytitle(input,userId);

            return  getSearchRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSearchRes> getCategories(String input , int userId) throws BaseException{
        try {

            List<GetSearchRes> getSearchRes  = postsDAO.getCategoriesResult(input,userId);

            return  getSearchRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSearchRes> GetsearchTagResult(String input , int userId) throws BaseException{
        try {

            List<GetSearchRes> getSearchRes  = postsDAO.getSearchResultByTag(input,userId);

            return  getSearchRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkAbleWriteReview(int userId, int recipeId)throws BaseException{
        try{
            return postsDAO.checkAbleWriteReview(userId,recipeId);

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetReviews> getReviewList(int productId) throws BaseException{
        try {

            List<GetReviews> getReviews  = postsDAO.getReviews(productId);

            return  getReviews;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public GetRecipePage getRecipeInfo(int userId,int recipeId) throws BaseException{
//        try {
//            GetRecipePage getRecipePage = null;
//
//            postsDAO.addHitCount(recipeId);
//
//            GetRecipeFrontPage getRecipeFrontPage = postsDAO.getRecipeFP(userId,recipeId);
//            List<GetRecipeDetailsPages> getRecipeDetailsPages = postsDAO.getRecipeDPs(recipeId);
//
//            getRecipePage.setGetRecipeFrontPage(getRecipeFrontPage);
//            getRecipePage.setGetRecipeDetailsPages(getRecipeDetailsPages);
//
//            return getRecipePage;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

    public GetRecipeFrontPage getRecipeFInfo(int userId,int recipeId) throws BaseException{
        try {

            postsDAO.addHitCount(recipeId);

            GetRecipeFrontPage getRecipeFrontPage = postsDAO.getRecipeFP(userId,recipeId);

            return getRecipeFrontPage;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetRecipeDetailsPages> getRecipeDInfo(int recipeId) throws BaseException{
        try {

            List<GetRecipeDetailsPages> getRecipeDetailsPages = postsDAO.getRecipeDPs(recipeId);

            return getRecipeDetailsPages;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 대중 레시피 전용

    public GetRecipeFrontPage getpublicRecipeFInfo(int userId,int recipeId) throws BaseException{
        try {

            postsDAO.addpublicHitCount(recipeId);

            GetRecipeFrontPage getRecipeFrontPage = postsDAO.getpublicRecipeFP(userId,recipeId);

            return getRecipeFrontPage;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetRecipeDetailsPages> getpublicRecipeDInfo(int recipeId) throws BaseException{
        try {

            List<GetRecipeDetailsPages> getRecipeDetailsPages = postsDAO.getpublicRecipeDPs(recipeId);

            return getRecipeDetailsPages;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
