package com.cyx.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cyx.community.dto.AccessTokenDTO;
import com.cyx.community.provider.dto.GithubUser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

   public String getAccessToken(AccessTokenDTO accessTokenDTO){
       accessTokenDTO.setRedirect_uri(redirectUri);
       accessTokenDTO.setClient_id(clientId);
       accessTokenDTO.setClient_secret(clientSecret);
       MediaType mediaType = MediaType.get("application/json; charset=utf-8");
       OkHttpClient client = new OkHttpClient();

       RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
       Request request = new Request.Builder()
               .url("https://github.com/login/oauth/access_token")
               .post(body)
               .build();
       try (Response response = client.newCall(request).execute()) {
           String string = response.body().string();
           String token = string.split("&")[0].split("=")[1];
           return token;
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
   }

   public GithubUser getUser(String accessToken){
       OkHttpClient client = new OkHttpClient();
       Request request = new Request.Builder()
               .url("https://api.github.com/user")
               .header("Authorization","token "+accessToken)
               .build();
       try {
           Response response = client.newCall(request).execute();
           String string = response.body().string();
           GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
           return githubUser;
       }catch (Exception e){
           e.printStackTrace();
       }

       return null;
   }
}
