package com.example.demo.utils;

import java.util.HashMap;
import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public class smsService {
    public static void certifiedPhoneNumber(String phoneNumber, String cerNum){
        String api_key = "NCSNBKGXGIW3MCWX";
        String api_secret = "JJY7CTZ2LYSSZ0RSRRE1IQAT6IZNQFPU";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);
        params.put("from", "01020571878"); //무조건 자기번호 (인증)
        params.put("type", "SMS");
        params.put("text", "인증번호 : " + cerNum);
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            //send() 는 메시지를 보내는 함수
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }


    }
}
