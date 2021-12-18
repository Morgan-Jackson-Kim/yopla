package com.example.demo.src.users;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.posts.model.bookmark.PostBookmarkReq;
import com.example.demo.src.users.model.*;
import com.example.demo.src.users.model.login.NewPassword;
import com.example.demo.src.users.model.login.PostUsersReq;
import com.example.demo.src.users.model.login.PostUsersRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.Mail.MailDto;
import com.example.demo.utils.Mail.MailService;
import com.example.demo.utils.smsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UsersService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UsersDAO usersDAO;
    private final UsersProvider usersProvider;
    private final MailService mailService;
    private final JwtService jwtService;


    @Autowired
    public UsersService(UsersDAO usersDAO, UsersProvider usersProvider, JwtService jwtService,MailService mailService) {
        this.usersDAO = usersDAO;
        this.usersProvider = usersProvider;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    //post
    public PostUsersRes createUser(PostUsersReq postUsersReq) throws BaseException {
        try {
            if(usersProvider.idDuplicate(postUsersReq.getLoginId()) == "Exist"){
                throw new BaseException(POST_USERS_EXISTS_LOGINID);
            }
            if(usersProvider.checkAvailableNP(postUsersReq.getEmail(),postUsersReq.getPhoneNumber())!= 0){
                throw new BaseException(POST_USERS_EXISTS_PHONENUMBER_AND_EMAIL);
            }
            if(usersProvider.checkPN(postUsersReq.getPhoneNumber()) != 0){
                throw new BaseException(POST_USERS_EXISTS_PHONENUMBER);
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUsersReq.getPassword());
            postUsersReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int id = usersDAO.createUser(postUsersReq);
            String successed;
            if (id>=0){
                successed = "success";
            }else {
                successed = "fail";
            }

            return new PostUsersRes(successed);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUsersRes createUserwithPI(PostUsersReq postUsersReq) throws BaseException {
        try {
            if(usersProvider.idDuplicate(postUsersReq.getLoginId()) == "Exist"){
                throw new BaseException(POST_USERS_EXISTS_LOGINID);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUsersReq.getPassword());
            postUsersReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int id = usersDAO.createUserWithPI(postUsersReq);
            String successed;
            if (id>=0){
                successed = "success";
            }else {
                successed = "fail";
            }
            return new PostUsersRes(successed);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public Integer sendCertifiedNumber(String phoneNumber , String certifiedNum)throws BaseException{
        try {
            smsService.certifiedPhoneNumber(phoneNumber,certifiedNum);

            return usersDAO.temptSaveNum(certifiedNum);

        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postInvoices(GetInvoicePaymentReq getInvoicePaymentReq) throws BaseException{
        try {
            int userId = getInvoicePaymentReq.getUserId();


            for(BuyProductsInfo prices : getInvoicePaymentReq.getPrices()){

                int productId = prices.getProductId() ;
                int basePrice = prices.getBasePrice();
                int paidPrice = prices.getPaidPrice();
                int buyUnits = prices.getBuyUnits();

                usersDAO.addToInvoice(userId,productId,basePrice,paidPrice,buyUnits);

                usersDAO.disableCartProducts(userId,productId);
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public int modifyCartStocks(PatchCartUnit patchCartUnit) throws BaseException{
        try{
            int result = usersDAO.modifyCartStocks(patchCartUnit);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_BOOKMARK);
            }
            return result;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void userDiabling(int userId,String password) throws BaseException{
        String origin;
        try {
            Users users = usersDAO.getPwd2(userId);

            origin = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(users.getPassword());


        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

        if( !origin.equals(password) ){
            throw new BaseException(WRONG_PASSWORD);
        }
        try{


            int result = usersDAO.userDisableWorks(userId);

            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERSTATUS);
            }

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void userInfoPatch(PatchUserInfo patchUserInfo) throws BaseException{
        String origin;
        int userId = patchUserInfo.getUserId();
        String password = patchUserInfo.getLastPassword();
        try {
            Users users = usersDAO.getPwd2(userId);

            origin = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(users.getPassword());


        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

        if( !origin.equals(password) ){
            throw new BaseException(WRONG_PASSWORD);
        }

        try{
            int result ;
            if(patchUserInfo.getNewPassword() == null || patchUserInfo.getNewPassword() == ""){
                result = usersDAO.userInfoPatchWork(patchUserInfo);
            }else {
                String Newpassword ;
                try{
                    //암호화
                    Newpassword = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchUserInfo.getNewPassword());
                } catch (Exception ignored) {
                    throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
                }

                result = usersDAO.userInfoPatchWorkNotNewPass(patchUserInfo,Newpassword);
            }



            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERSINFOTATUS);
            }

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public void userPIPatch(PatchUserPI patchUserPI) throws BaseException{


        try{
            int result ;


            result = usersDAO.userPIPatch(patchUserPI);

            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPISTATUS);
            }

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int createReport(PostReport postReport)throws BaseException{
        Integer result;
        if(postReport.getType() == "recipe" ){

            int myRecipe = usersProvider.checkMyRecipe(postReport);

            if( myRecipe == 1){
                throw new BaseException(POST_USERS_SELF_REPORT);
            }

            int reported = usersProvider.checkReport(postReport);

            if( reported == 1){
                throw new BaseException(POST_USERS_EXISTS_REPORT);
            }
            result = usersDAO.createReport(postReport);

            if(result == null || result == 0 ){
                throw new BaseException(INSERT_FAIL_REPORT);
            }

            if(usersProvider.diableCheck(postReport.getTargetId()) >= 3){
                usersDAO.disableRecipeTemp(postReport.getTargetId());
            }
        }
        else {

            int myReview = usersProvider.checkMyReview(postReport);

            if( myReview == 1){
                throw new BaseException(POST_USERS_SELF_REPORT);
            }

            int reported = usersProvider.checkReviewReport(postReport);

            if( reported == 1){
                throw new BaseException(POST_USERS_EXISTS_REPORT);
            }

            result = usersDAO.createReviewReport(postReport);

            if(result == null || result == 0 ){
                throw new BaseException(INSERT_FAIL_REPORT);
            }

            if(usersProvider.diableReviewCheck(postReport.getTargetId()) >= 3){
                usersDAO.disableReviewTemp(postReport.getTargetId());
            }
        }

        try{

            return  result;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createNewPassword(NewPassword newPassword)throws BaseException{

        try{

            if(usersProvider.checkAvailableNP2(newPassword.getEmail()) == 0){
             throw new BaseException(FAILED_TO_CHECKEMAIL_OR_PHONENUMBER);
            }

            String temptPassword = getTempPassword();

            String EncryptPassword = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(temptPassword);

            usersDAO.ChangePassword(newPassword.getEmail(),EncryptPassword);

            mailService.mailSend(mailService.createMaillChangePassword(newPassword.getEmail(),temptPassword));


        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }



    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 8; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }



}
