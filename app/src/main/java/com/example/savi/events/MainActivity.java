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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccessToken accessToken=null;

                GraphRequest request = GraphRequest.newGraphPathRequest(
                        accessToken,
                        "/search",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                Log.e(LOG_TAG,response.toString());
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("q", "Cluj");
                parameters.putString("type", "event");
                parameters.putString("limit", "1000");
                parameters.putString("pretty", "0");
                request.setParameters(parameters);
                request.executeAsync();

                Toast.makeText(MainActivity.this,"Graph API queryed",Toast.LENGTH_SHORT).show();

            }
        });



    }
}
