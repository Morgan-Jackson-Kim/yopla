package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Users {
    private int id;
    private String loginId;
    private String password;
    private String userName;
    private String userNickName;
    private String email;
    private String phoneNumber;
    private String address;
    private String rank;
    private String status;
}
