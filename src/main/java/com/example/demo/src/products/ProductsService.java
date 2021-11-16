//package com.example.demo.src.products;
//
//
//import com.example.demo.config.BaseException;
//import com.example.demo.config.secret.Secret;
//import com.example.demo.src.products.model.*;
//import com.example.demo.utils.AES128;
//import com.example.demo.utils.JwtService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import static com.example.demo.config.BaseResponseStatus.*;
//
//@Service
//public class ProductsService {
//
//    final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private final ProductsDAO productsDAO;
//    private final ProductsProvider productsProvider;
//    private final JwtService jwtService;
//
//    @Autowired
//    public ProductsService(ProductsDAO productsDAO, ProductsProvider productsProvider, JwtService jwtService){
//        this.productsDAO = productsDAO;
//        this.productsProvider = productsProvider;
//        this.jwtService = jwtService;
//    }
//

//
//    public void addtoCart(PostPCart postPCart) throws BaseException{
//        try{
//            int result = productsDAO.addtoCart(postPCart);
//            if(result == 0){
//                throw new BaseException(INSERT_FAIL_CART);
//            }
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public int postProductReview(PostProductReviewReq postProductReviewReq)throws BaseException{
//        try{
//            if(productsProvider.checkAbleWriteReview(postProductReviewReq.getUserId(),postProductReviewReq.getProductId()) == 0){
//                throw new BaseException(POST_REVIEWS_DISABLE);
//            }
//
//
//            int result = productsDAO.addReviews(postProductReviewReq);
//            if(result == 0){
//                throw new BaseException(INSERT_FAIL_REVIEW);
//            }
//            return result;
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//
//
//}
