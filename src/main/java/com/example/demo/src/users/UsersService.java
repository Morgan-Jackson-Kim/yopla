package com.example.demo.src.users;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.users.model.*;
import com.example.demo.src.users.model.login.PostUsersReq;
import com.example.demo.src.users.model.login.PostUsersRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
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
    private final JwtService jwtService;


    @Autowired
    public UsersService(UsersDAO usersDAO, UsersProvider usersProvider, JwtService jwtService) {
        this.usersDAO = usersDAO;
        this.usersProvider = usersProvider;
        this.jwtService = jwtService;

    }

    //post
    public PostUsersRes createUser(PostUsersReq postUsersReq) throws BaseException {
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



}