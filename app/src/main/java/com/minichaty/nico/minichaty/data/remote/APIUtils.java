package com.minichaty.nico.minichaty.data.remote;

public class APIUtils {

    // Can't be initialize
    private APIUtils() {}

    public static final String BASE_URL = "https://auth.uppsale.com/";
    public static final String BASE_CHAT_URL = "https://minichat-aa5f3.firebaseio.com/";

    public static APIService getAPIService(){
//        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
        return RetrofitClient.getClient(BASE_CHAT_URL).create(APIService.class); // CHAT TESST
    }

}
