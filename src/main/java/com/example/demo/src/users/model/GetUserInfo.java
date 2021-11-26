package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfo {
    private int userId;
    private String profileImage;
    private String userNickName;
    private String loginId;
    private int rankPoints;
    private String email;
    private String phoneNumber;
    private String address;
}
