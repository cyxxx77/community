package com.cyx.community.strategy;

import com.cyx.community.dto.AccessTokenDTO;
import com.cyx.community.provider.GiteeProvider;
import com.cyx.community.provider.dto.GiteeUser;
import com.cyx.community.provider.dto.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiteeUserStrategy implements UserStrategy{

    @Autowired
    private GiteeProvider giteeProvider;

    @Override
    public LoginUserInfo getUser(String code, String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = giteeProvider.getAccessToken(accessTokenDTO);
        GiteeUser giteeUser = giteeProvider.getUser(accessToken);
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setId(giteeUser.getId());
        loginUserInfo.setName(giteeUser.getName());
        loginUserInfo.setBio(giteeUser.getBio());
        loginUserInfo.setAvatarUrl(giteeUser.getAvatarUrl());
        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "gitee";
    }
}
