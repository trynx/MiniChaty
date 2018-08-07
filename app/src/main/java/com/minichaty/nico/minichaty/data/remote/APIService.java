package com.minichaty.nico.minichaty.data.remote;


import com.minichaty.nico.minichaty.data.LoginPost;

import java.util.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Which types of calls to make for the auth API
 */
public interface APIService {

    // In the URL , will use the "/token" path
    // https://auth.uppsale.com/token.
    @POST("/token")
    @FormUrlEncoded
    Call<ResponseBody> savePost(@Field("username") String username,
                                @Field("password") String password,
                                @Field("grant_type") String grantType);

    @GET("/chat/messages.json")
    Call<ResponseBody> getChat();

    @GET("/chat/messages.json")
    rx.Observable<ResponseBody> getChatFlow();
}
