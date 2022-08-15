package com.cyx.community.controller;

import com.cyx.community.dto.AccessTokenDTO;
import com.cyx.community.provider.dto.GithubUser;
import com.cyx.community.model.User;
import com.cyx.community.provider.GithubProvider;
import com.cyx.community.service.UserService;
import com.cyx.community.strategy.LoginUserInfo;
import com.cyx.community.strategy.UserStrategy;
import com.cyx.community.strategy.UserStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private UserStrategyFactory userStrategyFactory;

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser==null);
        if(githubUser!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            //登录成功，写入cookie和session
            response.addCookie(new Cookie("token",token));
            request.getSession().setAttribute("user",githubUser);
            System.out.println("登录成功");
            return "redirect:/";
        }else {
            log.error("callback get github error,{}",githubUser);
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/callback/{type}")
    public String newCallback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state",required = false)String state,
                           @PathVariable(name = "type")String type,
                           HttpServletRequest request,
                           HttpServletResponse response){
        UserStrategy userStrategy = userStrategyFactory.getStrategy(type);
        LoginUserInfo loginUserInfo = userStrategy.getUser(code, state);

        if(loginUserInfo!=null ){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(loginUserInfo.getName());
            user.setAccountId(String.valueOf(loginUserInfo.getId()));
            user.setAvatarUrl(loginUserInfo.getAvatarUrl());
            user.setType(type);
            userService.createOrUpdate(user);
            //登录成功，写入cookie和session
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60*60*24*30*6);
            cookie.setPath("/");
            response.addCookie(cookie);
            request.getSession().setAttribute("user",loginUserInfo);
            System.out.println("登录成功");
            return "redirect:/";
        }else {
            log.error("callback get github error,{}",loginUserInfo);
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie token = new Cookie("token", null);
        token.setMaxAge(0);
        response.addCookie(token);
        return "redirect:/";
    }
}
