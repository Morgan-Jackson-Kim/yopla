package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.demo.config.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "ads" , "howProduct","todaySale","kurlyProducts","endSale"})
public class BaseResponse5<T,T2,T3,T4,T5> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T ads;
    private T2 howProduct;
    private T3 todaySale;
    private T4 kurlyProducts;
    private T5 endSale;

    // 요청에 성공한 경우
    public BaseResponse5(T ads, T2 howProduct,T3 todaySale,T4 kurlyProducts,T5 endSale) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.ads = ads;
        this.howProduct = howProduct;
        this.todaySale = todaySale;
        this.kurlyProducts = kurlyProducts;
        this.endSale = endSale;
    }

    // 요청에 실패한 경우
    public BaseResponse5(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }


}