package com.example.demo.src.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserInfo {
    private int userId;
    private String userNickName;
    private String loginId;
    private String lastPassword;
    private String newPassword;
    private String email;
    private String address;
}
