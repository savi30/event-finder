package com.example.savi.events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccessToken accessToken=AccessToken.getCurrentAccessToken();

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
        parameters.putString("pretty", "200");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
