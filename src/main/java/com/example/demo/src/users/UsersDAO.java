package com.example.demo.src.users;


import com.example.demo.src.users.model.*;
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
        String checkDuplicateLoginIdQuery = "select exists(select loginId from users where loginId = ?)";
        String checkDuplicateLoginIdParams = loginId;
        return this.jdbcTemplate.queryForObject(checkDuplicateLoginIdQuery,
                int.class,
                checkDuplicateLoginIdParams);
    }

    public int checkDuplicatenickname(String nickname){
        String checkDuplicateLoginIdQuery = "select exists(select loginId from users where userNickName = ?)";
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
        String getPwdQuery = "select usersIdx, loginId, password , userName, userNickName, userEmail, phoneNumber, address, userGrade, accountStatus from users where loginId = ?";
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
        String  getbookmarkListsQuery = "select bookmarkIdx ,productImagePath, productName, basePrice, discount from pBookmarks join products  on pBookmarks.productId = products.productsIdx where pBookmarks.userId= ?  && pBookmarks.status='active' ";
        Object[] getbookmarkListsParams = new Object[]{userId};
        return this.jdbcTemplate.query(getbookmarkListsQuery,
                (rs,rowNum) -> new GetBookmarkList(
                        rs.getInt("bookmarkIdx"),
                        rs.getString("productImagePath"),
                        rs.getString("productName"),
                        rs.getInt("basePrice"),
                        rs.getInt("discount")),
                getbookmarkListsParams);
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

