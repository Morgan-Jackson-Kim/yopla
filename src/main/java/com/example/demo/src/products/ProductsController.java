//package com.example.demo.src.products;
//
//import com.example.demo.config.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.example.demo.src.products.model.*;
//import com.example.demo.utils.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.parameters.P;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//import static com.example.demo.config.BaseResponseStatus.*;
//
//@RestController
//@RequestMapping("/app/users")
//public class ProductsController {
//    final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private final ProductsProvider productsProvider;
//    @Autowired
//    private final ProductsService productsService;
//    @Autowired
//    private final JwtService jwtService;
//
//
//    public ProductsController(ProductsProvider productsProvider, ProductsService productsService , JwtService jwtService){
//        this.productsProvider = productsProvider;
//        this.productsService = productsService;
//        this.jwtService = jwtService;
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/kurly-recommend")
//    public BaseResponse5<List<GetMainAdvertisesRes>,List<GetHowProductRes>,List<GetTodaySaleRes>,List<GetKurlyProductsRes>,List<GetEndSaleProductRes>> GetkurlyRecom(@PathVariable("userId") Integer userId){
//        try {
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse5<>(INVALID_USER_JWT);
//                }
//            }
//
//            List<GetMainAdvertisesRes> getMainAdvertisesRes = productsProvider.getMainAds();
//
//            List<GetHowProductRes> getHowProductRes = productsProvider.getHowProducts();
//
//            List<GetTodaySaleRes> getTodaySaleRes = productsProvider.getTodaySales();
//
//            List<GetKurlyProductsRes> getKurlyProductsRes = productsProvider.getKurlyProducts();
//
//            List<GetEndSaleProductRes> getEndSaleProductRes = productsProvider.getEndSales();
//
//            return new BaseResponse5<>(getMainAdvertisesRes,getHowProductRes,getTodaySaleRes,getKurlyProductsRes,getEndSaleProductRes);
//        }catch (BaseException exception){
//            return new BaseResponse5<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/new-products")
//    public BaseResponse<List<GetProductShowRes>> getNewProducts(@PathVariable("userId") Integer userId){
//        try {
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse<>(INVALID_USER_JWT);
//                }
//            }
//            List<GetProductShowRes> getProductShowRes = productsProvider.getNewProducts();
//
//            return new BaseResponse<>(getProductShowRes);
//
//        }catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/best-products")
//    public BaseResponse<List<GetProductShowRes>> getBestProducts(@PathVariable("userId") Integer userId){
//        try {
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse<>(INVALID_USER_JWT);
//                }
//            }
//            List<GetProductShowRes> getProductShowRes = productsProvider.getBestProducts();
//
//            return new BaseResponse<>(getProductShowRes);
//
//        }catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/save-shopping")
//    public BaseResponse<List<GetProductShowRes>> getAlltillProducts(@PathVariable("userId") Integer userId){
//        try {
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse<>(INVALID_USER_JWT);
//                }
//            }
//            List<GetProductShowRes> getProductShowRes = productsProvider.getalltillProducts();
//
//            return new BaseResponse<>(getProductShowRes);
//
//        }catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/special-sale")
//    public BaseResponse<List<GetAdvertisesRes>> getSubAds(@PathVariable("userId") Integer userId){
//        try {
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse<>(INVALID_USER_JWT);
//                }
//            }
//
//            List<GetAdvertisesRes> getAdvertisesRes = productsProvider.getSubAds();
//            return new BaseResponse<>(getAdvertisesRes);
//
//        }catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//
//    }
//

//
//    @ResponseBody
//    @GetMapping("/{userId}/products")
//    public BaseResponse2<Integer ,List<GetProductShowRes>> searchingProduct(@PathVariable("userId") Integer userId,@RequestParam String input){
//        try {
//            if(userId != 0){
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse2<>(INVALID_USER_JWT);
//                }
//            }
//            if(input == null){
//                return new BaseResponse2<>(INVALID_USER_JWT);
//            }
//            int total;
//            total = productsProvider.getSearchResultNum(input);
//            List<GetProductShowRes> getProductShowRes = productsProvider.getSearchResult(input);
//
//            return new BaseResponse2<>(total,getProductShowRes);
//
//        }catch (BaseException exception) {
//            return new BaseResponse2<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/{productId}/detail-info")
//    public BaseResponse<GetProductInfoMDI> getProductmoreDetailInfo(@PathVariable("userId") Integer userId,@PathVariable("productId") int productId){
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
//            GetProductInfoMDI getProductInfoMDI;
//            getProductInfoMDI = productsProvider.getProductmoreDetailInfo(userId,productId);
//            return new BaseResponse<>(getProductInfoMDI);
//
//
//        }catch (BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @PostMapping("/{userId}/products/{productId}/cart")
//    public BaseResponse<String> addtoCart(@PathVariable("userId") Integer userId,@PathVariable("productId") int productId,@RequestParam int stock){
//        try {
//
//            if (userId == null) {
//                return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
//            }
//
//            if (userId != 0) {
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if (userId != userIdxByJwt) {
//                    return new BaseResponse<>(INVALID_USER_JWT);
//                }
//            }
//
//            if (productId == 0) {
//                return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRODUCTID);
//            }
//
//            PostPCart postPCart = new PostPCart(userId,productId,stock);
//            productsService.addtoCart(postPCart);
//
//            String result;
//            result = "장바구니 추가 성공";
//
//            return new BaseResponse<>(result);
//
//
//        }catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//
//    }
//
//    @ResponseBody
//    @PostMapping("/products/reviews")
//    public BaseResponse<Integer> createProductReview(@RequestBody PostProductReviewReq postProductReviewReq){
//        int userId = postProductReviewReq.getUserId();
//        int productId = postProductReviewReq.getProductId();
//        if(userId == 0){
//            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
//        }
//        if (productId == 0) {
//            return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRODUCTID);
//        }
//        try{
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse<>(INVALID_USER_JWT);
//                }
//            }
//
//
//
//            int result = productsService.postProductReview(postProductReviewReq);
//
//            return new BaseResponse<>(result);
//
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/{productId}/reviews")
//    public BaseResponse2<CountReview,List<GetReviews>> getProductsReviewList (@PathVariable("userId") Integer userId,@PathVariable("productId") int productId){
//        try {
//
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse2<>(INVALID_USER_JWT);
//                }
//            }
//            if(productId == 0){
//                return new BaseResponse2<>(POST_PRODUCTS_EMPTY_PRODUCTID);
//            }
//            CountReview countReview = productsProvider.countReviews(productId);
//            List<GetReviews> getReviews;
//            getReviews = productsProvider.getReviewList(productId);
//            return new BaseResponse2<>(countReview,getReviews);
//
//
//        }catch (BaseException exception){
//            return new BaseResponse2<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/{productId}/reviews/{reviewId}")
//    public BaseResponse<GetReviewDetail> getReviewDetail (@PathVariable("userId") Integer userId,@PathVariable("productId") int productId,@PathVariable("reviewId") int reviewIdk){
//        try {
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
//            GetReviewDetail getReviewDetail = productsProvider.getReviewDetails(productId,reviewIdk);
//
//            return new BaseResponse<>(getReviewDetail);
//
//
//        }catch (BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//    @ResponseBody
//    @GetMapping("/{userId}/products/recommends-tab")
//    public BaseResponse4<String,List<GetRelatedProduct>,List<GetTrendRanksBoards>,List<GetHowProductRes>> RecommendationTab(@PathVariable("userId") Integer userId){
//        try {
//            String userName;
//            if(userId != 0){
//
//                int userIdxByJwt = jwtService.getUserIdx();
//
//                if(userId != userIdxByJwt){
//                    return new BaseResponse4<>(INVALID_USER_JWT);
//                }
//
//               userName = productsProvider.getOnlyUserName(userId);
//            }else {
//               userName = "아무개";
//            }
//
//
//
//            List<GetRelatedProduct> getRelatedProducts = productsProvider.getRelateProductss();
//
//            List<GetTrendRanksBoards> getTrendRanksBoards = productsProvider.trandsRankBoards();
//
//            List<GetHowProductRes> overReviews = productsProvider.getOverReviews();
//
//            return new BaseResponse4<>(userName,getRelatedProducts,getTrendRanksBoards,overReviews);
//        }catch (BaseException exception){
//            return new BaseResponse4<>((exception.getStatus()));
//        }
//    }
//
//
//
//
//}
