package com.minichaty.nico.minichaty.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {

    /**
     * Save various options to later on limit which type of URLS to use
     */
    public enum HTTPUrls {
        LOGIN, CHAT
    }

    // All URLS which can be used
    private static final String BASE_LOGIN_URL = "https://auth.uppsale.com/";
    private static final String BASE_CHAT_URL = "https://minichat-aa5f3.firebaseio.com/";

    // Don't let it initialize
    private APIUtils() {}


    public static APIService getAPIService(HTTPUrls type) {
        String baseURL = "";
        // Depend on what the type it was choose, it will change the URL for itt
        switch (type) {
            case CHAT:
                baseURL = BASE_CHAT_URL;
                break;
            case LOGIN:
                baseURL = BASE_LOGIN_URL;
        }

        return getClient(baseURL).create(APIService.class);
    }

    /**
     * Depend on the URL that was choosen, it will return the Retrofit instance for the
     * selected option
     * @param baseUrl URL to use in Retrofit
     * @return Retrofit instance with the selected URL
     */
    private static Retrofit getClient(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



}
