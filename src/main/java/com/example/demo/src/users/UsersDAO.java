package com.example.demo.src.users;


import com.example.demo.src.posts.model.bookmark.PostBookmarkReq;
import com.example.demo.src.posts.model.recipe.DeleteRecipe;
import com.example.demo.src.posts.model.review.GetReviews;
import com.example.demo.src.users.model.*;
import com.example.demo.src.users.model.login.NewPassword;
import com.example.demo.src.users.model.login.PostLoginReq;
import com.example.demo.src.users.model.login.PostUsersReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UsersDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createUser(PostUsersReq postUsersReq){
        String createUserQuery = "insert into users (loginId, password , userNickName , userEmail, phoneNumber) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUsersReq.getLoginId(), postUsersReq.getPassword() ,postUsersReq.getNickname(),postUsersReq.getEmail(),postUsersReq.getPhoneNumber() };
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int createUserWithPI(PostUsersReq postUsersReq){
        String createUserQuery = "insert into users (loginId, password , userNickName , userEmail, phoneNumber ,profileImage) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUsersReq.getLoginId(), postUsersReq.getPassword() ,postUsersReq.getNickname(),postUsersReq.getEmail(),postUsersReq.getPhoneNumber(),postUsersReq.getProfileImageUrl() };
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkDuplicateLoginId(String loginId){
        String checkDuplicateLoginIdQuery = "select exists(select loginId from users where loginId = ? AND accountStatus ='active')";
        String checkDuplicateLoginIdParams = loginId;
        return this.jdbcTemplate.queryForObject(checkDuplicateLoginIdQuery,
                int.class,
                checkDuplicateLoginIdParams);
    }

    public int checkDuplicatenickname(String nickname){
        String checkDuplicateLoginIdQuery = "select exists(select loginId from users where userNickName = ? AND accountStatus ='active')";
        String checkDuplicateLoginIdParams = nickname;
        return this.jdbcTemplate.queryForObject(checkDuplicateLoginIdQuery,
                int.class,
                checkDuplicateLoginIdParams);
    }

    public int insertPI(int id, String PIpath){
        String insetPIUserQuery = "update users set profileImage = ? where usersIdx = ? && accountStatus = 'active'";
        Object[] createUserParams = new Object[]{PIpath,id};
        return this.jdbcTemplate.update(insetPIUserQuery, createUserParams);
    }

    public int temptSaveNum(String certifiedNum){
        String temptQuery = "insert into verifyNum (vn) VALUES (?)";
        Object[] temptParams = new Object[]{ certifiedNum};
        this.jdbcTemplate.update(temptQuery, temptParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkVN(int vnidx,String verifyNum){
        String temptQuery = "select exists(select verifyNumIdx from verifyNum where verifyNumIdx = ? && vn = ?)";
        Object[] temptParams = new Object[]{vnidx, verifyNum};
        return this.jdbcTemplate.queryForObject(temptQuery,int.class, temptParams);
    }

    public Users getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select usersIdx, loginId, password , userName, userNickName, userEmail, phoneNumber, address, userGrade, accountStatus from users where loginId = ? AND accountStatus = 'active'";
        String getPwdParams = postLoginReq.getLoginId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new Users(
                        rs.getInt("usersIdx"),
                        rs.getString("loginId"),
                        rs.getString("password"),
                        rs.getString("userName"),
                        rs.getString("userNickName"),
                        rs.getString("userEmail"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("userGrade"),
                        rs.getString("accountStatus")
                ),
                getPwdParams);
    }

    public Users getPwd2(int userId){
        String getPwd2Query = "select usersIdx, loginId, password , userName, userNickName, userEmail, phoneNumber, address, userGrade, accountStatus from users where usersIdx = ? AND accountStatus ='active'";
        int getPwd2Params = userId;
        return this.jdbcTemplate.queryForObject(getPwd2Query,
                (rs,rowNum)-> new Users(
                        rs.getInt("usersIdx"),
                        rs.getString("loginId"),
                        rs.getString("password"),
                        rs.getString("userName"),
                        rs.getString("userNickName"),
                        rs.getString("userEmail"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("userGrade"),
                        rs.getString("accountStatus")
                ),
                getPwd2Params);
    }


    public GetUserInfo getYoplaUserInfo(int userId){
        String getUserYoplaQuery = "select usersIdx, profileImage, userNickName, loginId, userGrade , userEmail, phoneNumber, address from users where usersIdx = ? AND accountStatus ='active'";
        int getUserYoplaParams = userId;
        return this.jdbcTemplate.queryForObject(getUserYoplaQuery,
                (rs, rowNum) -> new GetUserInfo(
                        rs.getInt("usersIdx"),
                        rs.getString("profileImage"),
                        rs.getString("userNickName"),
                        rs.getString("loginId"),
                        rs.getInt("userGrade"),
                        rs.getString("userEmail"),
                        rs.getString("phoneNumber"),
                        rs.getString("address")
                ),
                getUserYoplaParams);

    }



    public GetMykurlyRes getUserMykurly(int userId){
        String getUserMykurlyQuery = "SELECT UsersIdx, Users.Rank, Users.Name, Users.SaveMoney, (select count(*) from clinker where Users.UsersIdx = clinker.userId) as 'couponCount', (select count(*) from pBookmarks where Users.UsersIdx = pBookmarks.userId && pBookmarks.status = 'active' ) as 'bookmarks', Users.Address from Users where Users.UsersIdx = ? limit 1";
        int getUserMykurlyParams = userId;
        return this.jdbcTemplate.queryForObject(getUserMykurlyQuery,
                (rs, rowNum) -> new GetMykurlyRes(
                        rs.getInt("Users.UsersIdx"),
                        rs.getString("Users.Rank"),
                        rs.getString("Users.Name"),
                        rs.getInt("Users.SaveMoney"),
                        rs.getInt("couponCount"),
                        rs.getInt("bookmarks"),
                        rs.getString("Address")
                ),
                getUserMykurlyParams);

    }

    public CartUserInfo getcartuserInfo(int userId){
        String getCartUserInfoQuery = "select Address ,(select count(*) from carts where carts.userId = Users.UsersIdx && status = 'active') as total from Users where Users.UsersIdx = ? ";
        int getCartUserInfoParam = userId;
        return  this.jdbcTemplate.queryForObject(getCartUserInfoQuery,
                (rs,rowNum) -> new CartUserInfo(
                        rs.getString("address"),
                        rs.getInt("total")),
                getCartUserInfoParam
                );
    }

    public List<GetCartList> cartList(int userId){
        String getCartListQuery = "select productsIdx, productName,productImagePath,discount,basePrice,shippingType,wrappingType ,carts.stocks as cartStock from products join carts on products.productsIdx = carts.productId where carts.userId = ? && carts.status = 'active'";
        int getCartListByuserId = userId;
        return  this.jdbcTemplate.query(getCartListQuery,
                (rs,rowNum) -> new GetCartList(
                        rs.getInt("productsIdx"),
                        rs.getString("productName"),
                        rs.getString("productImagePath"),
                        rs.getInt("discount"),
                        rs.getInt("basePrice"),
                        rs.getString("shippingType"),
                        rs.getString("wrappingType"),
                        rs.getInt("cartStock")),
                getCartListByuserId
                );
    }

    public int modifyCartStocks(PatchCartUnit patchCartUnit){
        String modifyCartStocksQuery = "update carts set stocks = ? where userId = ? && productId = ? && status = 'active'";
        Object[] modifyCartStocksParms = new Object[]{patchCartUnit.getStocks(),patchCartUnit.getUserId(),patchCartUnit.getProductId()};
        this.jdbcTemplate.update(modifyCartStocksQuery,modifyCartStocksParms);

        String checkStockChangeQuery = "select stocks from carts where userId = ? && productId = ? && status = 'active'";
        Object[] checkStockChangeParms = new Object[]{patchCartUnit.getUserId(),patchCartUnit.getProductId()};
        return this.jdbcTemplate.queryForObject(checkStockChangeQuery,int.class,checkStockChangeParms);

    }


    public InvoiceUserInfo getInvoiceUserInfo(int userId){
        String getInvoiceUserInfoQuery = "select Users.Name,PhoneNumber,Email, Address ,(select count(*) from clinker where Users.UsersIdx = clinker.userId) as 'couponCount',SaveMoney ,Users.Rank from Users where Users.UsersIdx = ? ";
        int getInvoiceUserInfoParam = userId;
        return  this.jdbcTemplate.queryForObject(getInvoiceUserInfoQuery,
                (rs,rowNum) -> new InvoiceUserInfo(
                        rs.getString("Users.Name"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getString("Address"),
                        rs.getInt("couponCount"),
                        rs.getInt("SaveMoney"),
                        rs.getString("Users.Rank")),
                getInvoiceUserInfoParam
        );
    }

    public List<InvoiceList> invoiceListsfromdb(int userId){
        String getCartListQuery = "select productsIdx, productName,productImagePath,discount,basePrice,shippingType,wrappingType ,carts.stocks as cartStock from products join carts on products.productsIdx = carts.productId where carts.userId = ? && carts.status = 'active'";
        int getCartListByuserId = userId;
        return  this.jdbcTemplate.query(getCartListQuery,
                (rs,rowNum) -> new InvoiceList(
                        rs.getInt("productsIdx"),
                        rs.getString("productName"),
                        rs.getString("productImagePath"),
                        rs.getInt("discount"),
                        rs.getInt("basePrice"),
                        rs.getString("shippingType"),
                        rs.getString("wrappingType"),
                        rs.getInt("cartStock")),
                getCartListByuserId
        );
    }

    public void addToInvoice(int userId, int productId, int basePrice, int paidPrice ,int buyUnits){
        String addToInvoiceQuery = "insert into invoice (userId, productId ,basePrice, paidPrice, stocks) VALUES (?,?,?,?,?)";
        int userIdParams = userId;
        int productIdParams = productId;
        int basePriceParams = basePrice;
        int paidPriceParams = paidPrice;
        int buyUnitsParams = buyUnits;
        this.jdbcTemplate.update(addToInvoiceQuery,userIdParams,productIdParams,basePriceParams,paidPriceParams,buyUnitsParams);
    }

    public void disableCartProducts(int userId , int productId){
        String disableCartProductsQuery = "update carts set status = 'disable' where userId = ? && productId = ? && status = 'active'";
        int disableCartProductsUserIDParams = userId;
        int disableCartProductsProductIdParams = productId;
        this.jdbcTemplate.update(disableCartProductsQuery,disableCartProductsUserIDParams,disableCartProductsProductIdParams);
    }

    public GetBookmarksCount bookmarksCount(int userId){
        String bookmarksCountQuery = "select count(*) as bookmarkCounts from pBookmarks where userId = ? && status = 'active'";
        int bookmarksCountParms = userId;
        return this.jdbcTemplate.queryForObject(bookmarksCountQuery,
                (rs,rowNum) -> new GetBookmarksCount(
                        rs.getInt("bookmarkCounts")),
                bookmarksCountParms);
    }

    public List<GetBookmarkList> bookmarkLists(int userId){
        String  getbookmarkListsQuery = "select bookmarkIdx , recipeId , if(type = 'people',recipes.recipeFrontImage,publicRecipes.recipeFrontImage) as recipeFrontImage   , if(type = 'people',recipes.recipeName,publicRecipes.recipeName) as recipeName , users.userNickName,\n" +
                " if(type = 'people',recipes.category ,publicRecipes.category) as category ,  \n" +
                "if(type = 'people',(select (sum(scores)/count(*)) from reviews where reviews.recipeId =recipes.recipesIdx AND reviews.type = 'people' AND reviews.status = 'active'),(select (sum(scores)/count(*)) from reviews where reviews.recipeId =publicRecipes.recipesIdx AND reviews.type = 'public' AND reviews.status = 'active')) as averageScore,\n" +
                "if(type = 'people',(select count(*)  from rBookmarks where  recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active' AND rBookmarks.type = 'people'),(select count(*)  from rBookmarks where  publicRecipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active' AND rBookmarks.type = 'public')) as bookmarkCount\n" +
                ", rBookmarks.type from rBookmarks \n" +
                "join users \n" +
                "\tleft join recipes ON rBookmarks.recipeId = recipes.recipesIdx AND recipes.userId = users.usersIdx AND recipes.status = 'active'\n" +
                "    left join publicRecipes ON rBookmarks.recipeId = publicRecipes.recipesIdx AND publicRecipes.userId = users.usersIdx AND publicRecipes.status ='active'\n" +
                "    where ((rBookmarks.type ='people' AND recipes.recipesIdx is NOT NULL) OR (rBookmarks.type ='public' AND publicRecipes.recipesIdx IS NOT NULL)) AND\n" +
                " rBookmarks.userId = ? AND rBookmarks.status = 'active' ORDER BY rBookmarks.bookmarkIdx DESC ";
        Object[] getbookmarkListsParams = new Object[]{userId};
        return this.jdbcTemplate.query(getbookmarkListsQuery,
                (rs,rowNum) -> new GetBookmarkList(
                        rs.getInt("bookmarkIdx"),
                        rs.getInt("recipeId"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("recipeName"),
                        rs.getString("userNickName"),
                        rs.getString("category"),
                        rs.getInt("averageScore"),
                        rs.getInt("bookmarkCount"),
                        rs.getString("rBookmarks.type")),
                getbookmarkListsParams);
    }




    public List<GetMyRecipeList> myRecipes(int userId){
        String  getbookmarkListsQuery = "select recipesIdx , recipeFrontImage, recipeName,category,\n" +
                "(select (sum(scores)/count(*)) from reviews where reviews.recipeId = recipes.recipesIdx && reviews.status = 'active') as averageScore,\n" +
                "(select count(*)  from rBookmarks where recipes.recipesIdx = rBookmarks.recipeId && rBookmarks.status = 'active') as bookmarkCount\n" +
                "from recipes\n" +
                "where userId = ? AND recipes.status = 'active'; ";
        Object[] getbookmarkListsParams = new Object[]{userId};
        return this.jdbcTemplate.query(getbookmarkListsQuery,
                (rs,rowNum) -> new GetMyRecipeList(
                        rs.getInt("recipesIdx"),
                        rs.getString("recipeFrontImage"),
                        rs.getString("recipeName"),
                        rs.getString("category"),
                        rs.getInt("averageScore"),
                        rs.getInt("bookmarkCount")),
                getbookmarkListsParams);
    }



    public List<GetMyRR> myRReviews(int userId){
        String  getReviewsQuery = "select reviewsIdk,(select users.userNickName from users where users.usersIdx = reviews.userId) as userNickName, \n" +
                "(select users.profileImage from users where users.usersIdx = reviews.userId) as userPI ,content, scores,(select recipeName from recipes where reviews.recipeId = recipes.recipesIdx) as recipeName ,\n" +
                " date_format(reviews.createdAt, '%Y. %c. %d ')createdAt \n" +
                " from reviews\n" +
                " join recipes\n" +
                " on recipes.userId = ? AND recipes.status = 'active'\n" +
                " where  reviews.recipeId = recipes.recipesIdx && reviews.status='active' order by reviews.reviewsIdk desc ";
        Object[] getReviewsParams = new Object[]{userId};
        return this.jdbcTemplate.query(getReviewsQuery,
                (rs,rowNum) -> new GetMyRR(
                        rs.getInt("reviewsIdk"),
                        rs.getString("userNickName"),
                        rs.getString("userPI"),
                        rs.getString("content"),
                        rs.getFloat("scores"),
                        rs.getString("recipeName"),
                        rs.getString("createdAt")),
                getReviewsParams);
    }




    public List<GetPurchaedInfo> getPurchaedInfos(int userId){
        String  getPurchaedInfosQuery = "select invoiceIdx , products.productName,products.productImagePath, invoice.basePrice, paidPrice,invoice.stocks,shippment from invoice join products  on invoice.productId = products.productsIdx where invoice.userId= ?  && invoice.payment='paid' ORDER BY invoice.createdAt DESC  ";
        Object[] getPurchaedInfosParams = new Object[]{userId};
        return this.jdbcTemplate.query(getPurchaedInfosQuery,
                (rs,rowNum) -> new GetPurchaedInfo(
                        rs.getInt("invoiceIdx"),
                        rs.getString("products.productName"),
                        rs.getString("products.productImagePath"),
                        rs.getInt("invoice.basePrice"),
                        rs.getInt("paidPrice"),
                        rs.getInt("invoice.stocks"),
                        rs.getString("shippment")),
                getPurchaedInfosParams);
    }



    public int userDisableWorks(int userId){
        String disableUserQuery = "update users set accountStatus = 'disable' where usersIdx = ?  AND accountStatus = 'active'";
        int disableUserParams = userId;
        return this.jdbcTemplate.update(disableUserQuery,disableUserParams);
    }

    public int userInfoPatchWork(PatchUserInfo patchUserInfo){
        String patchUserQuery = "update users set loginId = ?, userNickName = ?, userEmail = ?, address = ? where usersIdx = ?  AND accountStatus = 'active'";
        Object[] patchUserParams = new Object[]{patchUserInfo.getLoginId(),patchUserInfo.getUserNickName(),patchUserInfo.getEmail(),patchUserInfo.getAddress(),patchUserInfo.getUserId()};
        return this.jdbcTemplate.update(patchUserQuery,patchUserParams);
    }

    public int userInfoPatchWorkNotNewPass(PatchUserInfo patchUserInfo, String Newpassword){
        String patchUserQuery = "update users set loginId = ?, userNickName = ?, userEmail = ?, address = ? ,password = ? where usersIdx = ?  AND accountStatus = 'active'";

        Object[] patchUserParams = new Object[]{patchUserInfo.getLoginId(),patchUserInfo.getUserNickName(),patchUserInfo.getEmail(),patchUserInfo.getAddress(),Newpassword,patchUserInfo.getUserId()};
        return this.jdbcTemplate.update(patchUserQuery,patchUserParams);
    }


    public int userPIPatch(PatchUserPI patchUserPI){
        String patchUserQuery = "update users set profileImage = ? where usersIdx = ?  AND accountStatus = 'active'";
        Object[] patchUserParams = new Object[]{patchUserPI.getNewProfileImage(),patchUserPI.getUserId()};
        return this.jdbcTemplate.update(patchUserQuery,patchUserParams);
    }

    public int checkReports(int userId , int recipeId){
        String checkReportsQuery = "select exists(select rstatus from recipeReports where rstatus = 'active' && userId = ? && recipeId = ?)";
        Object[] checkReportsParams = new Object[]{userId,recipeId};
        return this.jdbcTemplate.queryForObject(checkReportsQuery,
                int.class,
                checkReportsParams);
    }

    public int checkReviewReports(int userId , int recipeId){
        String checkReportsQuery = "select exists(select rstatus from reviewReports where rstatus = 'active' && userId = ? && reviewId = ?)";
        Object[] checkReportsParams = new Object[]{userId,recipeId};
        return this.jdbcTemplate.queryForObject(checkReportsQuery,
                int.class,
                checkReportsParams);
    }

    public int checkMineRecipe(int userId , int recipeId){
        String checkReportsQuery = "select exists(select recipesIdx from recipes where status = 'active' && userId = ? && recipesIdx = ?)";
        Object[] checkReportsParams = new Object[]{userId,recipeId};
        return this.jdbcTemplate.queryForObject(checkReportsQuery,
                int.class,
                checkReportsParams);
    }

    public int checkMineReview(int userId , int reviewId){
        String checkReportsQuery = "select exists(select reviewsIdk from reviews where status = 'active' && userId = ? && reviewsIdk = ?)";
        Object[] checkReportsParams = new Object[]{userId,reviewId};
        return this.jdbcTemplate.queryForObject(checkReportsQuery,
                int.class,
                checkReportsParams);
    }


    public int checkDisableReports(int recipeId){
        String checkReportsQuery = "select count(*) from recipeReports where rstatus = 'active' && recipeId = ?";
        Object[] checkReportsParams = new Object[]{recipeId};
        return this.jdbcTemplate.queryForObject(checkReportsQuery,
                int.class,
                checkReportsParams);
    }
    public int checkReviewDisableReports(int recipeId){
        String checkReportsQuery = "select count(*) from reviewReports where rstatus = 'active' && reviewId = ?";
        Object[] checkReportsParams = new Object[]{recipeId};
        return this.jdbcTemplate.queryForObject(checkReportsQuery,
                int.class,
                checkReportsParams);
    }

    public int checkAvailNP(String email, String phoneNumber){
        String checkAvailNPQuery = "select exists(select usersIdx from users where accountStatus = 'active' && userEmail = ? && phoneNumber = ?)";
        Object[] checkAvailNPParams = new Object[]{email,phoneNumber};
        return this.jdbcTemplate.queryForObject(checkAvailNPQuery,
                int.class,
                checkAvailNPParams);
    }
    public int checkAvailNP2(String email){
        String checkAvailNPQuery = "select exists(select usersIdx from users where accountStatus = 'active' && userEmail = ?)";
        Object[] checkAvailNPParams = new Object[]{email};
        return this.jdbcTemplate.queryForObject(checkAvailNPQuery,
                int.class,
                checkAvailNPParams);
    }

    public int checkPN(String phoneNumber){
        String checkAvailNPQuery = "select exists(select usersIdx from users where accountStatus = 'active'  AND phoneNumber = ?)";
        Object[] checkAvailNPParams = new Object[]{phoneNumber};
        return this.jdbcTemplate.queryForObject(checkAvailNPQuery,
                int.class,
                checkAvailNPParams);
    }

    public int disableRecipeTemp(int targetId){
        String deleteRecipQuery  = "update recipes,recipeDetails set recipes.status = 'tempDisable', recipeDetails.status = 'tempDisable'  where recipeDetails.recipeId = recipes.recipesIdx AND recipes.recipesIdx = ? ";
        Object[] deleteRecipParams = new Object[]{ targetId};
        return this.jdbcTemplate.update(deleteRecipQuery,deleteRecipParams);
    }


    public int disableReviewTemp(int targetId){
        String disableReviewTempQuery  = "update reviews set status = 'tempDisable'  where reviewsIdk = ? ";
        Object[] disableReviewTempParams = new Object[]{ targetId};
        return this.jdbcTemplate.update(disableReviewTempQuery,disableReviewTempParams);
    }

    public int createReport(PostReport postReport){
        String postReportQuery  = "insert into recipeReports (userId , recipeId) VALUES(?,?)";
        Object[] postReportParams = new Object[]{ postReport.getUserId() , postReport.getTargetId()};
        this.jdbcTemplate.update(postReportQuery,postReportParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int createReviewReport(PostReport postReport){
        String postReportQuery  = "insert into reviewReports (userId , reviewId) VALUES(?,?)";
        Object[] postReportParams = new Object[]{ postReport.getUserId() , postReport.getTargetId()};
        this.jdbcTemplate.update(postReportQuery,postReportParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


    public void ChangePassword(String userEmail , String np){
        String ChangePasswordQuery = "update users set password = ? where userEmail = ? && accountStatus = 'active'";
        Object[] ChangePasswordParams = new Object[]{np,userEmail};
         this.jdbcTemplate.update(ChangePasswordQuery, ChangePasswordParams);
    }



//    public List<PaidResponse> afterPaidProductsList(int userId, int size){
//        String afterPaidProductsListQuery = "select productId from invoice  where userId = ? && payment = 'paid' ORDER BY updatedAt DESC LIMIT ? ";
//        int afterPaidProductsListByuserId = userId;
//        int afterPaidProductsListSize = size;
//        return  this.jdbcTemplate.query(afterPaidProductsListQuery,
//                (rs,rowNum) -> new PaidResponse(
//                        rs.getInt("productsIdx")
//                        ),
//                afterPaidProductsListByuserId,
//                afterPaidProductsListSize
//        );
//    }

}

