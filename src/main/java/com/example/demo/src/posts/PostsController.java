package com.example.demo.src.posts;

import com.example.demo.config.*;
import com.example.demo.src.posts.model.bookmark.*;
import com.example.demo.src.posts.model.recipe.*;
import com.example.demo.src.posts.model.*;
import com.example.demo.src.posts.model.review.*;
import com.example.demo.src.products.model.CountReview;
import com.example.demo.src.products.model.GetProductInfoMDI;
import com.example.demo.src.products.model.GetProductShowRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/users")
public class PostsController {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostsProvider postsProvider;
    @Autowired
    private final PostsService postsService;
    @Autowired
    private final JwtService jwtService;


    public PostsController(PostsProvider postsProvider, PostsService postsService , JwtService jwtService){
        this.postsProvider = postsProvider;
        this.postsService = postsService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/recipes")
    public BaseResponse<Integer> createNewRecipes(@RequestBody createNewRecipe request){
        int userId = request.getUserId();
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try{

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            Integer recipeId = 0;

            recipeId = postsService.CreateNewRecipes(request);


            return new BaseResponse<>(recipeId);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/recipes/{recipeId}")
    public BaseResponse<String> createNewdetailRecipes(@RequestBody RecipeDetailsList request){
        try{

            String result ;

            if (request.getRecipeId() == 0){
                return new BaseResponse<>(POST_RECIPE_ID);
            }

            if(request.getNewRecipeDetails() ==null){
                return new BaseResponse<>(POST_RECIPE_DETAILS);
            }

            result =postsService.addRecipeDetails(request);

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //레시피 수정
    @ResponseBody
    @PatchMapping("/recipes/patchFront")
    public BaseResponse<Integer> patchRecipes(@RequestBody PatchRecipe request){
        int userId = request.getUserId();
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try{

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            Integer recipeId = 0;

            recipeId = postsService.PatchRecipes(request);


            return new BaseResponse<>(recipeId);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/recipes/patchDetails")
    public BaseResponse<String> patchDRecipes(@RequestBody RecipeDetailsList request){
        try{

            String result ;

            if (request.getRecipeId() == 0){
                return new BaseResponse<>(POST_RECIPE_ID);
            }

            if(request.getNewRecipeDetails() ==null){
                return new BaseResponse<>(POST_RECIPE_DETAILS);
            }

            result =postsService.PatchRecipesDetails(request);

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/recipes/status")
    public BaseResponse<String> delRecipes(@RequestBody DeleteRecipe request){
        int userId = request.getUserId();
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try{

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            postsService.deleteRecipe(request);

            String result = "deleted";


            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //레시피 수정 끝

    @ResponseBody
    @PostMapping("/recipes/bookmark")
    public BaseResponse<String> bookmarking(@RequestBody PostBookmarkReq postBookmarkReq){
        int userId = postBookmarkReq.getUserId();
        int recipeId = postBookmarkReq.getRecipeId();
        try {

            if(userId == 0){
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
            }

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            if(recipeId == 0){
                return new BaseResponse<>(POST_RECIPE_ID);
            }


            postsService.createBookmark(postBookmarkReq);

            String reuslt;
            reuslt = "success";


            return new BaseResponse<>(reuslt);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/recipes/bookmark/status")
    public BaseResponse<String> disableBookmark(@RequestBody PatchBookmarkReq patchBookmarkReq){
        try {
            Integer userId = patchBookmarkReq.getUserId();
            int recipeId = patchBookmarkReq.getRecipeId();

            if(userId == null){
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
            }

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            if(recipeId == 0){
                return new BaseResponse<>(POST_RECIPE_ID);
            }

            postsService.modifyBookmarkStatus(patchBookmarkReq);

            String result = "disabled";

            return new BaseResponse<>(result);

        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/{userId}/pplsRecipes")
    public BaseResponse4<List<GetMainAdvertisesRes>,List<GetShortsRes>,List<GetHotsRes>,List<GetRecommendRes>> GetMainPPlsPage (@PathVariable("userId") Integer userId){
        try {
            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse4<>(INVALID_USER_JWT);
                }
            }

            List<GetMainAdvertisesRes> getMainAdvertisesRes = postsProvider.getMainAds();

            List<GetShortsRes> getShortsRes = postsProvider.getShorts(userId);

            List<GetHotsRes> getHotsRes = postsProvider.getHots(userId);

            List<GetRecommendRes> getRecommendRes = postsProvider.getRecommends(userId);


            return new BaseResponse4<>(getMainAdvertisesRes,getShortsRes,getHotsRes,getRecommendRes);
        }catch (BaseException exception){
            return new BaseResponse4<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/pplsRecipes/shortsForm")
    public BaseResponse<List<GetShortsRes>> getpplsRecipesSF (@PathVariable("userId") Integer userId){
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try {


            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            List<GetShortsRes> getShortsRes = postsProvider.getmoreShorts(userId);

            return new BaseResponse<>(getShortsRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/pplsRecipes/hots")
    public BaseResponse<List<GetHotsRes>> getpplsRecipesH (@PathVariable("userId") Integer userId){
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try {

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            List<GetHotsRes> getHotsRes = postsProvider.getmoreHots(userId);


            return new BaseResponse<>(getHotsRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // 대중 레시피 전용

    @ResponseBody
    @GetMapping("/{userId}/publicRecipes")
    public BaseResponse4<List<GetMainAdvertisesRes>,List<GetShortsRes>,List<GetHotsRes>,List<GetRecommendRes>> GetMainPublicPage (@PathVariable("userId") Integer userId){
        if(userId == 0){
            return new BaseResponse4<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try {


            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse4<>(INVALID_USER_JWT);
                }
            }

            List<GetMainAdvertisesRes> getMainAdvertisesRes = postsProvider.getMainAds();

            List<GetShortsRes> getShortsRes = postsProvider.getpubShorts(userId);

            List<GetHotsRes> getHotsRes = postsProvider.getpubHots(userId);

            List<GetRecommendRes> getRecommendRes = postsProvider.getpubRecommends(userId);


            return new BaseResponse4<>(getMainAdvertisesRes,getShortsRes,getHotsRes,getRecommendRes);
        }catch (BaseException exception){
            return new BaseResponse4<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/publicRecipes/shortsForm")
    public BaseResponse<List<GetShortsRes>> getpublicRecipesRecipesSF (@PathVariable("userId") Integer userId){
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try {

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            List<GetShortsRes> getShortsRes = postsProvider.getPublicMoreShorts(userId);

            return new BaseResponse<>(getShortsRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/publicRecipes/hots")
    public BaseResponse<List<GetHotsRes>> getpublicRecipesRecipesH (@PathVariable("userId") Integer userId){
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try {

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            List<GetHotsRes> getHotsRes = postsProvider.getPublicMoreHots(userId);


            return new BaseResponse<>(getHotsRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/{userId}/publicRecipes/recommends")
    public BaseResponse<List<GetRecommendRes>> getpublicRecipesRecipesRecomeends (@PathVariable("userId") Integer userId){
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try {

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            List<GetRecommendRes> getRecommendRes = postsProvider.getPublicMoreRecommends(userId);


            return new BaseResponse<>(getRecommendRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 대중레시피 업로드 전용
    @ResponseBody
    @PostMapping("/public/recipes")
    public BaseResponse<Integer> createPublicNewRecipes(@RequestBody createNewRecipe request){
        int userId = request.getUserId();
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        try{

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            Integer recipeId = 0;

            recipeId = postsService.CreatePublicNewRecipes(request);


            return new BaseResponse<>(recipeId);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/public/recipes/{recipeId}")
    public BaseResponse<String> createPublicNewRecipesDetails(@RequestBody RecipeDetailsList request){
        try{

            String result ;

            if (request.getRecipeId() == 0){
                return new BaseResponse<>(POST_RECIPE_ID);
            }

            if(request.getNewRecipeDetails() ==null){
                return new BaseResponse<>(POST_RECIPE_DETAILS);
            }

            result =postsService.addPublicRecipeDetails(request);

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/recipes/search")
    public BaseResponse<List<GetSearchRes>> searchingProduct(@PathVariable("userId") Integer userId, @RequestParam(value = "title",required = false) String input,@RequestParam(value = "tag",required = false) String tag){
        try {
            if(userId != 0){
                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }
            if(input == null && tag == null){
                return new BaseResponse<>(POST_INPUT_SEARCH);
            }

            List<GetSearchRes> getSearchRes = null;

            if(input != null && input != ""){
                getSearchRes = postsProvider.GetsearchResult(input,userId);
                
            }

            if(tag != null && tag != ""){
                getSearchRes = postsProvider.GetsearchTagResult(tag,userId);
            }

            return new BaseResponse<>(getSearchRes);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

//    @ResponseBody
//    @GetMapping("/{userId}/recipes/search")
//    public BaseResponse<List<GetSearchRes>> searchingtag(@PathVariable("userId") Integer userId, @RequestParam("tag") String input){
//        try {
//            if(userId != 0){
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse<>(INVALID_USER_JWT);
//                }
//            }
//            if(input == null){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//
//
//            List<GetSearchRes> getSearchRes = postsProvider.GetsearchTagResult(input,userId);
//
//            return new BaseResponse<>(getSearchRes);
//
//        }catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }

    @ResponseBody
    @PostMapping("/recipes/reviews")
    public BaseResponse<Integer> createProductReview(@RequestBody PostProductReviewReq postProductReviewReq){
        int userId = postProductReviewReq.getUserId();
        int productId = postProductReviewReq.getRecipeId();
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        if (productId == 0) {
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_RECIPEID);
        }
        try{
            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }



            int result = postsService.postProductReview(postProductReviewReq);

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/recipes/reviews/patch")
    public BaseResponse<String> editReview(@RequestBody EditReviewReq editReviewReq){
        int userId = editReviewReq.getUserId();
        int productId = editReviewReq.getReviewsIdx();
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        if (productId == 0) {
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_REVIEWIDX);
        }
        try{
            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            String  finalre;

            int result = postsService.editReview(editReviewReq);
            if (result != 0){
                finalre = "edit success";
            }else {
                finalre ="edit failed";

            }

            return new BaseResponse<>(finalre);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/recipes/{recipeId}/reviews")
    public BaseResponse<List<GetReviews>> getProductsReviewList (@PathVariable("recipeId") int recipeId){
        try {

            if(recipeId == 0){
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRODUCTID);
            }

            List<GetReviews> getReviews;
            getReviews = postsProvider.getReviewList(recipeId);
            return new BaseResponse<>(getReviews);


        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/publicRecipes/{recipeId}/reviews")
    public BaseResponse<List<GetReviews>> getpublicReviewList (@PathVariable("recipeId") int recipeId){
        try {

            if(recipeId == 0){
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRODUCTID);
            }

            List<GetReviews> getReviews;
            getReviews = postsProvider.getpublicReviewList(recipeId);
            return new BaseResponse<>(getReviews);


        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("{userId}/recipes/{recipeId}")
    public BaseResponse2<GetRecipeFrontPage,List<GetRecipeDetailsPages>> getRecipeDetails (@PathVariable("userId") int userId,@PathVariable("recipeId") int recipeId ){
        try {
            if(userId == 0){
                return new BaseResponse2<>(POST_PRODUCTS_EMPTY_USERID);
            }

            if(recipeId == 0){
                return new BaseResponse2<>(POST_PRODUCTS_EMPTY_RECIPEID);
            }

            GetRecipeFrontPage getRecipeFrontPage =postsProvider.getRecipeFInfo(userId,recipeId);
            List<GetRecipeDetailsPages> getRecipeDetailsPages =postsProvider.getRecipeDInfo(recipeId);
//            getRecipePage = postsProvider.getRecipeInfo(userId ,recipeId);
            return new BaseResponse2<>(getRecipeFrontPage,getRecipeDetailsPages);


        }catch (BaseException exception){
            return new BaseResponse2<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("{userId}/publicRecipes/{recipeId}")
    public BaseResponse2<GetRecipeFrontPage,List<GetRecipeDetailsPages>> getpublicRecipeDetails (@PathVariable("userId") int userId,@PathVariable("recipeId") int recipeId ){
        try {
            if(userId == 0){
                return new BaseResponse2<>(POST_PRODUCTS_EMPTY_USERID);
            }

            if(recipeId == 0){
                return new BaseResponse2<>(POST_PRODUCTS_EMPTY_RECIPEID);
            }

            GetRecipeFrontPage getRecipeFrontPage =postsProvider.getpublicRecipeFInfo(userId,recipeId);
            List<GetRecipeDetailsPages> getRecipeDetailsPages =postsProvider.getpublicRecipeDInfo(recipeId);
//            getRecipePage = postsProvider.getRecipeInfo(userId ,recipeId);
            return new BaseResponse2<>(getRecipeFrontPage,getRecipeDetailsPages);


        }catch (BaseException exception){
            return new BaseResponse2<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/{userId}/category/{category}")
    public BaseResponse<List<GetSearchRes>> searchingByCategory (@PathVariable("userId") Integer userId, @PathVariable(value = "category") String input){
        try {
            if(userId != 0){
                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }


            List<GetSearchRes> getSearchRes = null;

            if(input == null && input == ""){
                return new BaseResponse<>(POST_INPUT_CATEGORY);
            }
            getSearchRes = postsProvider.getCategories(input,userId);


            return new BaseResponse<>(getSearchRes);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
