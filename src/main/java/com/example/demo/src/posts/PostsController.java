package com.example.demo.src.posts;

import com.example.demo.config.*;
import com.example.demo.src.posts.model.bookmark.*;
import com.example.demo.src.posts.model.recipe.*;
import com.example.demo.src.posts.model.*;
import com.example.demo.src.posts.model.review.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<String> createNewRecipes(@RequestBody RecipeDetailsList request){
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

//    @ResponseBody
//    @GetMapping("/{userId}/products/{productId}/info")
//    public BaseResponse<GetProductInfo> getProductDetail(@PathVariable("userId") Integer userId,@PathVariable("productId") int productId){
//        try {
//            if(userId == null){
//                return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
//            }
//
//
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse<>(INVALID_USER_JWT);
//                }
//            }
//            if(productId == 0){
//                return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRODUCTID);
//            }
//
//            GetProductInfo getProductInfo;
//            getProductInfo = productsProvider.getProductDetail(userId,productId);
//            return new BaseResponse<>(getProductInfo);
//
//
//        }catch (BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }





}
