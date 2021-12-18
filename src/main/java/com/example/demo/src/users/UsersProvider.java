package com.example.demo.src.users;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.secret.Secret;

import com.example.demo.src.posts.model.bookmark.PostBookmarkReq;
import com.example.demo.src.users.model.*;
import com.example.demo.src.users.model.login.NewPassword;
import com.example.demo.src.users.model.login.PostLoginReq;
import com.example.demo.src.users.model.login.PostLoginRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UsersProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UsersDAO usersDAO;
    private final JwtService jwtService;

    @Autowired
    public UsersProvider(UsersDAO usersDAO, JwtService jwtService) {
        this.usersDAO = usersDAO;
        this.jwtService = jwtService;
    }


    //로그인
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        Users users;
        try {
            users = usersDAO.getPwd(postLoginReq);
        }catch (Exception ignored) {
            throw new BaseException(DISABLED_USER);
        }

        String password;
        try {

            if(this.idDuplicate(postLoginReq.getLoginId()) == "notExist"){
                throw new BaseException(FAILED_TO_LOGIN);
            }
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(users.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(postLoginReq.getPassword().equals(password)){
            int id = usersDAO.getPwd(postLoginReq).getId();
            String jwt = jwtService.createJwt(id);
            return new PostLoginRes(id,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    //id 중복여부 확인
    public String idDuplicate(String loginId)throws BaseException{
        try{
            if(usersDAO.checkDuplicateLoginId(loginId) == 1){
                return "Exist";
            }
            return "notExist";
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public String nickNameDuplicate(String nickName)throws BaseException{
        try{
            if(usersDAO.checkDuplicatenickname(nickName) == 1){
                return "Exist";
            }
            return "notExist";
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //핸드폰 인증번호 발급
    public String checkVN(int vnIdx,String verifyNum)throws BaseException{
        try{
            if(usersDAO.checkVN(vnIdx,verifyNum) == 1){
                return "verfied";
            }else{
                return "failed";
            }
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }


    public GetUserInfo getYoplaUserInfo(int userId) throws BaseException{
        try{
            GetUserInfo getUserInfo = usersDAO.getYoplaUserInfo(userId);

            return getUserInfo;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //마이컬리 데이터 가져오기
    public GetMykurlyRes getUserMykurlyInfo(int userId) throws BaseException{
        try{
            GetMykurlyRes getMykurlyRes = usersDAO.getUserMykurly(userId);

            return getMykurlyRes;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public CartUserInfo getcartuserInfo(int userId) throws BaseException{
        try {
            CartUserInfo cartUserInfo =usersDAO.getcartuserInfo(userId);
            return cartUserInfo;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCartList> cartList(int userId) throws BaseException{
        try {
            List<GetCartList> getCartList =usersDAO.cartList(userId);
            return getCartList;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public InvoiceUserInfo getinvoiceUserInfo(int userId) throws BaseException{
        try {
            InvoiceUserInfo invoiceUserInfo =usersDAO.getInvoiceUserInfo(userId);
            return invoiceUserInfo;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<InvoiceList> invoiceLists(int userId) throws BaseException{
        try {
            List<InvoiceList> invoiceList =usersDAO.invoiceListsfromdb(userId);
            return invoiceList;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBookmarkList> getBookmarkLists(int userId) throws BaseException{
        try {
            List<GetBookmarkList> getBookmarkList =usersDAO.bookmarkLists(userId);
            return getBookmarkList;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMyRecipeList> getMyRecipes(int userId) throws BaseException{
        try {
            List<GetMyRecipeList> getMyRecipeLists =usersDAO.myRecipes(userId);
            return getMyRecipeLists;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMyRR> getMyRRs(int userId) throws BaseException{
        try {
            List<GetMyRR> getMyRRList =usersDAO.myRReviews(userId);
            return getMyRRList;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPurchaedInfo> purchaedInfos(int userId) throws BaseException{
        try {
            List<GetPurchaedInfo> getPurchaedInfos =usersDAO.getPurchaedInfos(userId);
            return getPurchaedInfos;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetBookmarksCount getBookmarksCount(int userId) throws BaseException{
        try {
            GetBookmarksCount getBookmarksCount = usersDAO.bookmarksCount(userId);
            return getBookmarksCount;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReport(PostReport postReport) throws BaseException{
        try{
            return usersDAO.checkReports(postReport.getUserId(),postReport.getTargetId());

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkReviewReport(PostReport postReport) throws BaseException{
        try{
            return usersDAO.checkReviewReports(postReport.getUserId(),postReport.getTargetId());

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public int checkMyRecipe(PostReport postReport) throws BaseException{
        try{
            return usersDAO.checkMineRecipe(postReport.getUserId(),postReport.getTargetId());

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkMyReview(PostReport postReport) throws BaseException{
        try{
            return usersDAO.checkMineReview(postReport.getUserId(),postReport.getTargetId());

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public int diableCheck(int targetId) throws BaseException{
        try{
            return usersDAO.checkDisableReports(targetId);

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int diableReviewCheck(int targetId) throws BaseException{
        try{
            return usersDAO.checkReviewDisableReports(targetId);

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkAvailableNP(String email , String phoneNumber) throws BaseException{
        try {
            return usersDAO.checkAvailNP(email,phoneNumber);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkAvailableNP2(String email ) throws BaseException{
        try {
            return usersDAO.checkAvailNP2(email);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkPN(String phoneNumber) throws BaseException{
        try {
            return usersDAO.checkPN(phoneNumber);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


//    public List<PaidResponse> getPaidProducts(int userId, int size) throws BaseException{
//        try {
//            List<PaidResponse> PaidResponse =usersDAO.afterPaidProductsList(userId,size);
//            return PaidResponse;
//
//        }catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

}
