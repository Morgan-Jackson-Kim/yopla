package com.example.demo.src.users;


import com.example.demo.config.BaseResponse2;
import com.example.demo.src.posts.model.bookmark.PostBookmarkReq;
import com.example.demo.src.users.model.login.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.users.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")
public class UsersController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UsersProvider usersProvider;
    @Autowired
    private final UsersService usersService;
    @Autowired
    private final JwtService jwtService;


    public UsersController(UsersProvider usersProvider, UsersService usersService, JwtService jwtService){
        this.usersProvider = usersProvider;
        this.usersService = usersService;
        this.jwtService = jwtService;
    }

//    @ResponseBody
//    @PostMapping("upload-test")
//    public String upload(@RequestParam(name = "profileImage", required = false) MultipartFile profileImage)throws IOException {
//        s3Uploader.upload(profileImage, "test");
//        return "test";
//
//    }

    //회원가입
    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<PostUsersRes> createUser(@RequestBody PostUsersReq postUsersReq) {

        if(postUsersReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUsersReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if(postUsersReq.getNickname() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }

        if(postUsersReq.getLoginId() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_LOGINID);
        }

        if(postUsersReq.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        try{
            PostUsersRes postUsersRes;

            if(postUsersReq.getProfileImageUrl() == null || postUsersReq.getProfileImageUrl() == "" ){
                postUsersReq.setProfileImageUrl(null);
                postUsersRes = usersService.createUser(postUsersReq);
            }else {
                postUsersRes = usersService.createUserwithPI(postUsersReq);
            }




            return new BaseResponse<>(postUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

//    @ResponseBody
//    @PostMapping("/sign-up/withPI")
//    public BaseResponse<PostUsersRes> createUserwithPi(@RequestParam("loginId") String loginId,@RequestParam("password") String password,@RequestParam("nickname") String nickname,@RequestParam("email") String  email,@RequestParam("phoneNumber") String phoneNumber, @RequestParam(name = "profileImage") MultipartFile profileImage) {
//
//        if(email == null){
//            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
//        }
//
//        if(nickname == null){
//            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
//        }
//
//        if( loginId == null) {
//            return new BaseResponse<>(POST_USERS_EMPTY_LOGINID);
//        }
//
//        if(password == null){
//            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
//        }
//
//        try{
//            PostUsersRes postUsersRes;
//
//            postUsersRes = usersService.createUserwithPI(loginId,password,nickname,email,phoneNumber,profileImage);
//
//
//            return new BaseResponse<>(postUsersRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }



    //로그인
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            if(postLoginReq.getLoginId() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_LOGINID);
            }
            if(postLoginReq.getPassword() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }

            PostLoginRes postLoginRes = usersProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //아이디 확인
    @ResponseBody
    @GetMapping("/sign-up/id-check")
    public BaseResponse<String> idDuplicate(@RequestParam String loginId){
        try{
            if(loginId == null || loginId == ""){
                return new BaseResponse<>(POST_USERS_EMPTY_LOGINID);
            }
            String result = usersProvider.idDuplicate(loginId);
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/sign-up/nickname")
    public BaseResponse<String> nickNameDuplicate(@RequestParam String nickname){
        try{
            if(nickname == null || nickname == ""){
                return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
            }
            String result = usersProvider.nickNameDuplicate(nickname);
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //휴대폰인증
    @ResponseBody
    @GetMapping("/sign-up/phoneVerify/sendSMS")
    public BaseResponse<Integer> phoneVerify(@RequestParam String phoneNumber ) throws BaseException {

        Integer result  ;

        if(phoneNumber == null || phoneNumber == "" ){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
        }

        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        result = usersService.sendCertifiedNumber(phoneNumber,numStr);

        return new BaseResponse<>(result);

    }

    //인증번호 확인
    @ResponseBody
    @GetMapping("/sign-up/phoneVerify/checkNum")
    public BaseResponse<String> phoneVerify(@RequestParam int vnIdx ,@RequestParam String verifyNum) throws BaseException {

        if(verifyNum == "" || verifyNum == null){
            return new BaseResponse<>(POST_USERS_EMPTY_VERIFYNUMBER);
        }


        String result;

        result = usersProvider.checkVN(vnIdx,verifyNum);

        if (result == "failed") {
            return new BaseResponse<>(VERIFY_FAIL);
        }
        return new BaseResponse<>(result);
    }

    @ResponseBody
    @GetMapping("/{userId}/mykurly")
    public BaseResponse<GetMykurlyRes> mykurly(@PathVariable("userId") int userId){

        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetMykurlyRes getMykurlyRes = usersProvider.getUserMykurlyInfo(userId);

            return new BaseResponse<>(getMykurlyRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/cart")
    public BaseResponse2<CartUserInfo, List<GetCartList>> cartList(@PathVariable("userId") int userId){
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse2<>(INVALID_USER_JWT);
            }
            CartUserInfo cartUserInfo = usersProvider.getcartuserInfo(userId);

            List<GetCartList> getCartList = usersProvider.cartList(userId);

            return new BaseResponse2<>(cartUserInfo,getCartList);

        } catch (BaseException exception) {
            return new BaseResponse2<>((exception.getStatus()));
        }
    }

    //유저의 북마크 목록
    @ResponseBody
    @GetMapping("/{userId}/bookmarks")
    public BaseResponse< List<GetBookmarkList>> getbookmarkss(@PathVariable("userId") int userId){
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetBookmarkList> getBookmarkList = usersProvider.getBookmarkLists(userId);

            return new BaseResponse<>(getBookmarkList);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    //유저 레시피 목록
    @ResponseBody
    @GetMapping("/{userId}/recipes")
    public BaseResponse< List<GetMyRecipeList>> getMyRecipes(@PathVariable("userId") int userId){
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetMyRecipeList> getMyRecipeLists = usersProvider.getMyRecipes(userId);

            return new BaseResponse<>(getMyRecipeLists);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //내 레시피 반응
    @ResponseBody
    @GetMapping("/{userId}/myRecipeReviews")
    public BaseResponse<List<GetMyRR>> getMyRecipesReviews(@PathVariable("userId") int userId){
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetMyRR> getMyRRList = usersProvider.getMyRRs(userId);

            return new BaseResponse<>(getMyRRList);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/purchasedInfo")
    public BaseResponse<List<GetPurchaedInfo>> getpurchasedInfo(@PathVariable("userId") int userId){
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetPurchaedInfo> getPurchaedInfos  = usersProvider.purchaedInfos(userId);

            return new BaseResponse<>(getPurchaedInfos);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }




    @ResponseBody
    @PatchMapping("/cart/product/unit")
    public BaseResponse<Integer> changeUnit(@RequestBody PatchCartUnit patchCartUnit){
        int userId = patchCartUnit.getUserId();
        int productId = patchCartUnit.getProductId();
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        if(productId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRODUCTID);
        }
        try{

            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }
            int result = usersService.modifyCartStocks(patchCartUnit);


            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/cart/invoice")
    public BaseResponse2<InvoiceUserInfo, List<InvoiceList>> cartInvoice(@PathVariable("userId") int userId){
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse2<>(INVALID_USER_JWT);
            }
            InvoiceUserInfo invoiceUserInfo = usersProvider.getinvoiceUserInfo(userId);

            List<InvoiceList> invoiceList = usersProvider.invoiceLists(userId);

            return new BaseResponse2<>(invoiceUserInfo,invoiceList);

        } catch (BaseException exception) {
            return new BaseResponse2<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/cart/invoice")
    public BaseResponse<String> invoicePayments(@RequestBody GetInvoicePaymentReq getInvoicePaymentReq){
        int userId = getInvoicePaymentReq.getUserId();
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


             usersService.postInvoices(getInvoicePaymentReq);
            String result = "success";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/secession")
    public BaseResponse<String> userDisable(@RequestBody UserDisable userDisable){
        int userId = userDisable.getUserId();
        String password = userDisable.getPassword();
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


            usersService.userDiabling(userId,password);
            String result = "account deleted";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/myInfo")
    public BaseResponse<GetUserInfo> yoplaUserInfoPage(@PathVariable("userId") int userId){
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

            GetUserInfo getUserInfo = usersProvider.getYoplaUserInfo(userId);

            return new BaseResponse<>(getUserInfo);


        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/myInfo/status")
    public BaseResponse<String> userInfoEdit (@RequestBody PatchUserInfo patchUserInfo){
        int userId = patchUserInfo.getUserId();
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


            usersService.userInfoPatch(patchUserInfo);
            String result = "edited";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/myInfo/pi")
    public BaseResponse<String> userPIEdit (@RequestBody PatchUserPI patchUserPI){
        int userId = patchUserPI.getUserId();
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


            usersService.userPIPatch(patchUserPI);
            String result = "edited";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/report")
    public BaseResponse<Integer> bookmarking(@RequestBody PostReport postReport){
        int userId = postReport.getUserId();
        int targetId = postReport.getTargetId();
        if(userId == 0){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_USERID);
        }
        if(targetId == 0){
            return new BaseResponse<>(POST_TARGET_ID);
        }

        try {



            if(userId != 0){

                int userIdxByJwt = jwtService.getUserIdx();

                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }



            int reuslt;
            reuslt = usersService.createReport(postReport);


            return new BaseResponse<>(reuslt);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/findpassword")
    public BaseResponse<String> findpassword (@RequestBody NewPassword newPassword){
        if(newPassword.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        try {

            usersService.createNewPassword(newPassword);

            String result = "sent";

            return new BaseResponse<>(result);

        }catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }





}
