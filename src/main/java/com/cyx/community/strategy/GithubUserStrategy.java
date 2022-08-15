package com.cyx.community.strategy;


import com.cyx.community.dto.AccessTokenDTO;
import com.cyx.community.provider.dto.GithubUser;
import com.cyx.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GithubUserStrategy implements UserStrategy {

    @Autowired
    private GithubProvider githubProvider;

    @Override
    public LoginUserInfo getUser(String code, String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setId(githubUser.getId());
        loginUserInfo.setName(githubUser.getName());
        loginUserInfo.setBio(githubUser.getBio());
        loginUserInfo.setAvatarUrl(githubUser.getAvatarUrl());
        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "github";
    }
}
