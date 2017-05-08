package com.example.savi.events;

import android.content.Intent;
import android.os.Bundle;
import org.jetbrains.annotations.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

/**
 * Created by savi on 27.04.2017.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG=LoginActivity.class.getName();

    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton =(LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<com.facebook.login.LoginResult>() {
            @Override
            public void onSuccess(com.facebook.login.LoginResult loginResult) {
                launchMainActivity();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                String toastMsg=error.getMessage();
                Toast.makeText(LoginActivity.this,toastMsg,Toast.LENGTH_LONG);
            }
        });

        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();
        if(loginToken!=null) {
            launchMainActivity();
        }

    }

    @Override
    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }


    public void Skip(View view) {
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("skipLogin");
        launchMainActivity();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
