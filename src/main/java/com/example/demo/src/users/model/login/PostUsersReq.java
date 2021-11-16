package com.example.demo.src.users.model.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class PostUsersReq {
    private String loginId;
    private String password;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
}
