package com.example.savi.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (AccessToken.getCurrentAccessToken() != null) {
            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                final AccessToken accessToken = AccessToken.getCurrentAccessToken();

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GraphRequest request = GraphRequest.newGraphPathRequest(
                                accessToken,
                                "/search",
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        Log.v(LOG_TAG,response.toString());
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("q", "Roma");
                        parameters.putString("type", "event");
                        parameters.putString("limit", "2");
                        parameters.putString("pretty", "0");
                        request.setParameters(parameters);
                        request.executeAsync();

                        Toast.makeText(MainActivity.this,"Graph API queryed",Toast.LENGTH_LONG).show();

                    }
                });

            }
            else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }
        }
        else {
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {

                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    String toastMessage = accountKitError.getErrorType().getMessage();
                    Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                }
            });
        }



    }
}
