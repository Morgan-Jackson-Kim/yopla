//package com.example.demo.src.products;
//
//import com.example.demo.src.products.model.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.util.List;
//
//@Repository
//public class ProductsDAO {
//
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public void setDataSource(DataSource dataSource){
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//    public List<GetAdvertisesRes> getSubAds(){
//        String getAdvertiseQuery = "select * from advertisementsSubs";
//        return this.jdbcTemplate.query(getAdvertiseQuery,
//                (rs,rowNum) -> new GetAdvertisesRes(
//                        rs.getInt("adIdx"),
//                        rs.getString("adImagePath"))
//        );
//    }
//
//    public List<GetMainAdvertisesRes> getMainAds(){
//        String getMainAdvertiseQuery = "select * from advertisementsMain";
//        return this.jdbcTemplate.query(getMainAdvertiseQuery,
//                (rs,rowNum) -> new GetMainAdvertisesRes(
//                        rs.getInt("adIdx"),
//                        rs.getString("adImagePath"))
//        );
//    }
//
//    public List<GetHowProductRes> getHP(){
//        String  getHPQuery = "select productsIdx,productName,productImagePath,discount, basePrice from products ORDER BY rand() limit 8";
//        return this.jdbcTemplate.query(getHPQuery,
//                (rs,rowNum) -> new GetHowProductRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"))
//        );
//    }
//
//    public List<GetTodaySaleRes> getTS(){
//        String  getTSQuery = "select productsIdx,productName,productImagePath,discount, basePrice from products where section = 'ts'";
//        return this.jdbcTemplate.query(getTSQuery,
//                (rs,rowNum) -> new GetTodaySaleRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"))
//
//                );
//    }
//
//    public List<GetKurlyProductsRes> getKP(){
//        String  getKPQuery = "select productsIdx,productName,productImagePath,discount, basePrice from products where section = 'kp'";
//        return this.jdbcTemplate.query(getKPQuery,
//                (rs,rowNum) -> new GetKurlyProductsRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"))
//
//                );
//    }
//
//    public List<GetEndSaleProductRes> getESP(){
//        String  getEPQuery = "select productsIdx,productName,productImagePath,discount, basePrice from products where section = 'esp'";
//        return this.jdbcTemplate.query(getEPQuery,
//                (rs,rowNum) -> new GetEndSaleProductRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"))
//
//                );
//    }
//
//    public List<GetProductShowRes> getNP(){
//        String  getNPQuery = "select productsIdx,productName,productImagePath,discount,basePrice,kurlyOnly from products where section = 'np'";
//        return this.jdbcTemplate.query(getNPQuery,
//                (rs,rowNum) -> new GetProductShowRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"),
//                        rs.getString("kurlyOnly"))
//
//        );
//    }
//
//    public List<GetProductShowRes> getBestP(){
//        String  getBestPQuery = "select productsIdx,productName,productImagePath,discount,basePrice,kurlyOnly from products where section = 'bestp'";
//        return this.jdbcTemplate.query(getBestPQuery,
//                (rs,rowNum) -> new GetProductShowRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"),
//                        rs.getString("kurlyOnly"))
//
//        );
//    }
//
//    public List<GetProductShowRes> getalltill(){
//        String  getBestPQuery = "select productsIdx,productName,productImagePath,discount,basePrice,kurlyOnly from products where section = 'sales'";
//        return this.jdbcTemplate.query(getBestPQuery,
//                (rs,rowNum) -> new GetProductShowRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"),
//                        rs.getString("kurlyOnly"))
//        );
//    }
//

//
//    public int checkAbleWriteReview(int userId, int productId){
//        String checkAbleWriteReviewQuery = "select exists(select review from invoice where review = 'able'  && userId = ? && productId = ?)";
//        Object[] checkBookmarkParams = new Object[]{userId,productId};
//        return this.jdbcTemplate.queryForObject(checkAbleWriteReviewQuery,
//                int.class,
//                checkBookmarkParams);
//    }
//
//    public int addtoCart(PostPCart postPCart){
//        String addtoCartQuery = "insert into carts (userId,productId,stocks) VALUES(?,?,?)";
//        Object[] addtoCartParams = new Object[] {postPCart.getUserId(),postPCart.getProductId(),postPCart.getStock()};
//        return this.jdbcTemplate.update(addtoCartQuery,addtoCartParams);
//    }
//
//

//
//    public GetProductInfoMDI getProductmoreDetailInfo (int userId,int productId){
//        String getProductmoreDetailInfoQuery = "select * ,(select status from pBookmarks where pBookmarks.userId = ? && pBookmarks.productId = products.productsIdx  ORDER BY updatedAt DESC LIMIT 1 ) as bookmarkStatus from products where products.productsIdx = ?";
//        int getProductmoreDetailInfoByuserId = userId;
//        int getProductmoreDetailInfoByproductId = productId;
//        return this.jdbcTemplate.queryForObject(getProductmoreDetailInfoQuery,
//                (rs,rowNum) -> new GetProductInfoMDI(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("detailImagePath"),
//                        rs.getString("detailProductName"),
//                        rs.getString("productType"),
//                        rs.getString("wrapUnitweight"),
//                        rs.getString("certified"),
//                        rs.getString("relatedLaw"),
//                        rs.getString("origin"),
//                        rs.getString("storageWay"),
//                        rs.getString("productWarn"),
//                        rs.getString("expirationDate"),
//                        rs.getString("customServiceNumber"),
//                        rs.getInt("stock"),
//                        rs.getString("bookmarkStatus")
//                ),
//                getProductmoreDetailInfoByuserId,
//                getProductmoreDetailInfoByproductId);
//    }
//
//    public GetProductInfo getProductDetaillogined(int userId,int productId){
//        String getProductDetailQuery = "select * ,(select status from pBookmarks where pBookmarks.userId = ? && pBookmarks.productId = products.productsIdx  ORDER BY updatedAt DESC LIMIT 1 ) as bookmarkStatus from products where products.productsIdx = ?";
//        int getProductDetailByuserid = userId;
//        int getProductDetailByProductId = productId;
//        return this.jdbcTemplate.queryForObject(getProductDetailQuery,
//                (rs,rowNum) -> new GetProductInfo(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productSummary"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"),
//                        rs.getString("sellingUnit"),
//                        rs.getString("weight"),
//                        rs.getString("shippingType"),
//                        rs.getString("wrappingType"),
//                        rs.getString("allegiInfo"),
//                        rs.getString("expirationDate"),
//                        rs.getString("productGuide"),
//                        rs.getInt("stock"),
//                        rs.getString("bookmarkStatus")
//                        ),
//                getProductDetailByuserid,
//                getProductDetailByProductId);
//    }
//
//    public List<GetProductShowRes> getSearchResult(String input){
//        String  getSearchResultQuery = "select productsIdx,productName,productImagePath,discount,basePrice,kurlyOnly from products where productName LIKE ? ";
//        String getSearchResultByProductInputParam = "%" + input + "%";
//        return this.jdbcTemplate.query(getSearchResultQuery,
//                (rs,rowNum) -> new GetProductShowRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"),
//                        rs.getString("kurlyOnly")),
//                getSearchResultByProductInputParam);
//    }
//
//    public int getSearchResultNum(String input){
//        String  getSearchResultNumQuery = "select count(*) from products where productName LIKE ? ";
//        String getSearchResultNumByProductInputParam = "%" + input + "%";
//        return this.jdbcTemplate.queryForObject(getSearchResultNumQuery,int.class,getSearchResultNumByProductInputParam);
//    }
//
//
//
//
//    public List<GetReviews> getReviews(int productId){
//        String  getReviewsQuery = "select reviewsIdk,title,(select Users.Name from Users where Users.UsersIdx = reviews.userId) as userName, IF(reviews.imagePath=NULL,0,1) as existImage , date_format(reviews.createdAt, '%Y. %c. %d ')createdAt,best as isItBest from reviews where  productId= ? && status='active' ";
//        Object[] getReviewsParams = new Object[]{productId};
//        return this.jdbcTemplate.query(getReviewsQuery,
//                (rs,rowNum) -> new GetReviews(
//                        rs.getInt("reviewsIdk"),
//                        rs.getString("title"),
//                        rs.getString("userName"),
//                        rs.getInt("existImage"),
//                        rs.getString("createdAt"),
//                        rs.getString("isItBest")),
//                getReviewsParams);
//    }
//
//    public CountReview countingReviews(int productId){
//        String countingReviewsQuery = "select count(*) as reviewCounts from reviews where productId = ? && status = 'active'";
//        int countingReviewsParams = productId;
//        return this.jdbcTemplate.queryForObject(countingReviewsQuery,
//                (rs,rowNum) -> new CountReview(
//                        rs.getInt("reviewCounts")),
//                countingReviewsParams);
//    }
//
//    public GetReviewDetail getReviewsDetails(int productId, int reviewIdk){
//        String getReviewsDetailsQuery = "select reviewsIdk,(select productName from products where products.productsIdx = reviews.reviewsIdk)as productName,title, imagePath,content , date_format(reviews.createdAt, '%Y. %c. %d ')createdAt from reviews where productId= ? && reviewsIdk = ? && status='active' ";
//        Object[] getReviewsDetailsParams = new Object[]{productId,reviewIdk};
//        return this.jdbcTemplate.queryForObject(getReviewsDetailsQuery,
//                (rs,rowNum) -> new GetReviewDetail(
//                        rs.getInt("reviewsIdk"),
//                        rs.getString("productName"),
//                        rs.getString("title"),
//                        rs.getString("imagePath"),
//                        rs.getString("content"),
//                        rs.getString("createdAt")),
//                getReviewsDetailsParams);
//    }
//
//    public String getOnlyUserName(int userId){
//        String getOnlyUserNameQuery = "select Users.Name from Users where Users.UsersIdx=?";
//        Object[] getOnlyUserNameParams = new Object[]{userId};
//        return this.jdbcTemplate.queryForObject(getOnlyUserNameQuery, String.class ,getOnlyUserNameParams);
//    }
//
//    public List<GetRelatedProduct> getRP(){
//        String  getOverReviewQuery = "select productsIdx,productName,productImagePath,discount, basePrice from products ORDER BY rand() limit 8";
//        return this.jdbcTemplate.query(getOverReviewQuery,
//                (rs,rowNum) -> new GetRelatedProduct(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"))
//        );
//    }
//
//
//    public List<GetHowProductRes> getOverReview(){
//        String  getOverReviewQuery = "select productsIdx,productName,productImagePath,discount, basePrice from products where (select count(*) from reviews where products.productsIdx = reviews.productId) >=1 ";
//        return this.jdbcTemplate.query(getOverReviewQuery,
//                (rs,rowNum) -> new GetHowProductRes(
//                        rs.getInt("productsIdx"),
//                        rs.getString("productName"),
//                        rs.getString("productImagePath"),
//                        rs.getInt("discount"),
//                        rs.getInt("basePrice"))
//        );
//    }
//
//
//
//    public List<GetTrendRanksBoards> getTrands(){
//        String  getTrandsQuery = "select trendsIdx,trendName,trendImagePath from trends where status = 'active' ";
//        return this.jdbcTemplate.query(getTrandsQuery,
//                (rs,rowNum) -> new GetTrendRanksBoards(
//                        rs.getInt("trendsIdx"),
//                        rs.getString("trendName"),
//                        rs.getString("trendImagePath"))
//        );
//    }
//
//
//}
