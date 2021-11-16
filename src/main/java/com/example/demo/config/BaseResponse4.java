package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.demo.config.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result" , "relatedProducts","trendsRanks","kurlyProducts","overReviews"})
public class BaseResponse4<T,T2,T3,T4> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
    private T2 relatedProducts;
    private T3 trendsRanks;
    private T4 overReviews;


    // 요청에 성공한 경우
    public BaseResponse4(T result, T2 relatedProducts, T3 trendsRanks, T4 overReviews) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
        this.relatedProducts = relatedProducts;
        this.trendsRanks = trendsRanks;
        this.overReviews = overReviews;

    }

    // 요청에 실패한 경우
    public BaseResponse4(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }


}