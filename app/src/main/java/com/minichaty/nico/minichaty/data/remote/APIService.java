package com.minichaty.nico.minichaty.data.remote;


import com.minichaty.nico.minichaty.data.LoginPost;
import com.minichaty.nico.minichaty.data.MessagePost;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Which types of calls to make for the auth API
 */
public interface APIService {

    // In the URL , will use the "token" path
    // https://auth.uppsale.com/token.
    @POST("token")
    @FormUrlEncoded
    Call<LoginPost> savePost(@Field("username") String username,
                             @Field("password") String password,
                             @Field("grant_type") String grantType);

    // Must add .json for the REST API of firebase to accept the request
    @POST("chat.json")
    Call<ResponseBody> sendMsg(@Body MessagePost message);
}
