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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEt, passwordEt;
    private final String grantType = "password";
    private final static  String TAG = "LoginActivity";

    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEt = (EditText) findViewById(R.id.et_username);
        passwordEt = (EditText) findViewById(R.id.et_password);
        // Save an instance with Retrofit for sending request to login
        apiService = APIUtils.getAPIService(APIUtils.HTTPUrls.LOGIN);
    }

    public void btnLogin(View view) {
        String email = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) sendPost(email, password, grantType);
    }

    /**
     * Send post through APIService and handles the request from the API using Retrofit
     */
    private void sendPost(String username, String password, String grantType){
        apiService.savePost(username, password, grantType).enqueue(new Callback<LoginPost>() {
            @Override
            public void onResponse(Call<LoginPost> call, Response<LoginPost> response) {
                // Only move on when there is the response which is needed from the request
                if(response.isSuccessful() && response.body() != null){

                    // Move to chat activity
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class );
                    startActivity(intent);

                } else if (response.body() == null){
                    Log.e(TAG, "Wrong Credentials");
                    // Alert the user about incorrect input
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<LoginPost> call, Throwable t) {
                Log.e(TAG, "Unable to submit login post");
            }
        });
    }
}
