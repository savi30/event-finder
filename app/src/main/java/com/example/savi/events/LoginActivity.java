package com.example.savi.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
/**
 * Created by savi on 27.04.2017.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG=LoginActivity.class.getName();

    private static final int APP_REQUEST_CODE =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if(accessToken!=null) {
            launchMainActivity();
        }

    }

    @Override
    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(loginResult.getError()!=null) {
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this,toastMessage,Toast.LENGTH_LONG).show();
            }else if(loginResult.getAccessToken()!=null){
                launchMainActivity();
            }
        }
    }

    private void onLogin(final LoginType loginType) {
        final Intent intent =new Intent(this, AccountKitActivity.class);

        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder=
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType, AccountKitActivity.ResponseType.TOKEN);
        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configuration);
        startActivityForResult(intent,APP_REQUEST_CODE);
    }


    public void onPhoneLogin(View view) {
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("onPhoneLogin");
        onLogin(LoginType.PHONE);
    }

    public void onEmailLogin(View view) {
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("onEmailLogin");
        onLogin(LoginType.EMAIL);
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
