//package com.example.demo.src.products;
//
//import com.example.demo.config.BaseException;
//import com.example.demo.config.secret.Secret;
//
//import com.example.demo.src.products.model.*;
//import com.example.demo.utils.AES128;
//import com.example.demo.utils.JwtService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//import static com.example.demo.config.BaseResponseStatus.*;
//
//@Service
//public class ProductsProvider {
//
//    final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private final ProductsDAO productsDAO;
//
//    private final JwtService jwtService;
//
//    @Autowired
//    public ProductsProvider(ProductsDAO productsDAO, JwtService jwtService){
//        this.productsDAO = productsDAO;
//        this.jwtService = jwtService;
//    }
//
//    public List<GetAdvertisesRes> getSubAds()throws BaseException{
//        try {
//            List<GetAdvertisesRes> getAdvertisesRes = productsDAO.getSubAds();
//            return  getAdvertisesRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetMainAdvertisesRes> getMainAds()throws BaseException{
//        try {
//            List<GetMainAdvertisesRes> getMainAdvertisesRes = productsDAO.getMainAds();
//            return  getMainAdvertisesRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetHowProductRes> getHowProducts()throws BaseException{
//        try {
//            List<GetHowProductRes> getHowProductRes = productsDAO.getHP();
//            return  getHowProductRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetTodaySaleRes> getTodaySales()throws BaseException{
//        try {
//            List<GetTodaySaleRes> getTodaySaleRes = productsDAO.getTS();
//            return  getTodaySaleRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetKurlyProductsRes> getKurlyProducts()throws BaseException{
//        try {
//            List<GetKurlyProductsRes> getKurlyProductsRes  = productsDAO.getKP();
//            return  getKurlyProductsRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetEndSaleProductRes> getEndSales()throws BaseException{
//        try {
//            List<GetEndSaleProductRes> getEndSaleProductRes  = productsDAO.getESP();
//            return  getEndSaleProductRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetProductShowRes> getNewProducts() throws BaseException{
//        try {
//
//            List<GetProductShowRes> getProductShowRes  = productsDAO.getNP();
//
//            return  getProductShowRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetProductShowRes> getBestProducts()throws BaseException{
//        try {
//
//            List<GetProductShowRes> getProductShowRes  = productsDAO.getBestP();
//
//            return  getProductShowRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//    public List<GetProductShowRes> getalltillProducts()throws BaseException{
//        try {
//
//            List<GetProductShowRes> getProductShowRes  = productsDAO.getalltill();
//
//            return  getProductShowRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public  GetProductInfoMDI getProductmoreDetailInfo(int userId,int productId) throws BaseException{
//        try {
//            GetProductInfoMDI getProductInfoMDI;
//            getProductInfoMDI = productsDAO.getProductmoreDetailInfo( userId, productId);
//            return getProductInfoMDI;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public GetProductInfo getProductDetail(int userId,int productId) throws BaseException{
//        try {
//
//            GetProductInfo getProductInfo;
//
//            getProductInfo = productsDAO.getProductDetaillogined(userId,productId);
//
//
//            return  getProductInfo;
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetProductShowRes> getSearchResult(String input) throws BaseException{
//        try {
//
//            List<GetProductShowRes> getProductShowRes  = productsDAO.getSearchResult(input);
//
//            return  getProductShowRes;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//    public int getSearchResultNum(String input) throws BaseException{
//        try {
//
//            int ResultNum = productsDAO.getSearchResultNum(input);
//
//            return  ResultNum;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//

//
//    public int checkAbleWriteReview(int userId, int productId)throws BaseException{
//        try{
//            return productsDAO.checkAbleWriteReview(userId,productId);
//
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public CountReview countReviews(int productId) throws BaseException{
//        try{
//            CountReview countReview = productsDAO.countingReviews(productId);
//            return countReview;
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetReviews> getReviewList(int productId) throws BaseException{
//        try {
//
//            List<GetReviews> getReviews  = productsDAO.getReviews(productId);
//
//            return  getReviews;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public GetReviewDetail getReviewDetails(int productId, int reviewIdk) throws BaseException{
//        try {
//            GetReviewDetail getReviewDetail = productsDAO.getReviewsDetails(productId,reviewIdk);
//            return getReviewDetail;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//
//    public List<GetRelatedProduct> getRelateProductss()throws BaseException{
//        try {
//            List<GetRelatedProduct> getRelatedProducts = productsDAO.getRP();
//            return  getRelatedProducts;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public List<GetHowProductRes> getOverReviews()throws BaseException{
//        try {
//            List<GetHowProductRes> getOverReview = productsDAO.getOverReview();
//            return  getOverReview;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//
//
//    public List<GetTrendRanksBoards> trandsRankBoards()throws BaseException{
//        try {
//            List<GetTrendRanksBoards> getTrendRanksBoards = productsDAO.getTrands();
//            return  getTrendRanksBoards;
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public String getOnlyUserName(int userId)throws BaseException{
//        try {
//            return productsDAO.getOnlyUserName(userId);
//
//        }catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }
//
//}
