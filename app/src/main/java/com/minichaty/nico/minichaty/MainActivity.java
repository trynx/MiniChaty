package com.minichaty.nico.minichaty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.minichaty.nico.minichaty.data.LoginPost;
import com.minichaty.nico.minichaty.data.remote.APIService;
import com.minichaty.nico.minichaty.data.remote.APIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEt, passwordEt;
    private final String grantType = "password";
    private final static  String TAG = "MAIN_ACTIVITY";

    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEt = (EditText) findViewById(R.id.et_username);
        passwordEt = (EditText) findViewById(R.id.et_password);

        apiService = APIUtils.getAPIService();
    }


    public void btnLogin(View view) {
        String email = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        /*if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            sendPost(email, password, grantType);
        }*/

//        getChatMsg();

        getChatFlowMsg();
    }


    /**
     * Send post through APIService and handles the request from the API
     */
    private void sendPost(String username, String password, String grantType){
        apiService.savePost(username, password, grantType).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null){
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        Log.i(TAG, "Successfully logged in " + json);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    /*Intent intent = new Intent(getApplicationContext(), ChatActivity.class );
                    startActivity(intent);*/
                } else if (response.body() == null){
                    Log.e(TAG, "Wrong Credentials");
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit login post");
            }
        });
    }

    private void getChatMsg(){
        apiService.getChat().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null){
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        Log.i(TAG, "Successfully logged in " + json);

                        String msgKey;
                        Iterator<String> jsonIT = json.keys();
                        while (jsonIT.hasNext()){
                            msgKey = jsonIT.next();
                            Log.i(TAG, json.getString(msgKey));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    /*Intent intent = new Intent(getApplicationContext(), ChatActivity.class );
                    startActivity(intent);*/
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getChatFlowMsg(){


    }
}
