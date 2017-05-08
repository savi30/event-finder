package com.example.savi.events;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.places.PlaceManager;
import com.facebook.places.model.PlaceFields;
import com.facebook.places.model.PlaceSearchRequestParams;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final int MY_PERMISSIONS_REQUEST_LOCATION=1;

    private CustomEventAdapter mEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (AccessToken.getCurrentAccessToken() != null) {
            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                final AccessToken accessToken = AccessToken.getCurrentAccessToken();

                final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final GraphRequest request = GraphRequest.newGraphPathRequest(
                                accessToken,
                                "/search",
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {

                                        ListView eventListView = (ListView) findViewById(R.id.list);

                                        List<CustomEvent> events=Utils.parseJson(response);

                                        if (!events.isEmpty()) {
                                            fab.setVisibility(View.GONE);
                                        }

                                        mEventAdapter = new CustomEventAdapter(MainActivity.this, events);
                                        eventListView.setAdapter(mEventAdapter);

                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("q", "Cluj");
                        parameters.putString("type", "event");
                        parameters.putString("limit", "1000");
                        parameters.putString("pretty", "0");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }
                });

            } else {
                Profile.fetchProfileForCurrentAccessToken();
            }
        } else {

            //TODO Refractor code for PlacesGraph
            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FacebookSdk.setClientToken(String.valueOf(R.string.APP_CLIENT_TOKEN));

                    PlaceSearchRequestParams.Builder builder = new
                            PlaceSearchRequestParams.Builder();

                    builder.setSearchText("Caffe");
                    builder.setDistance(10000);
                    builder.setLimit(100);
                    builder.addField(PlaceFields.NAME);
                    builder.addField(PlaceFields.LOCATION);
                    builder.addField(PlaceFields.PHONE);

                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            //Show description
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                        }

                    }


                    MainActivity.PlaceSearchRequestCallback callback = new MainActivity.PlaceSearchRequestCallback();

                    PlaceManager.newPlaceSearchRequest(builder.build(), callback);

                }
            });
        }
    }

    public class PlaceSearchRequestCallback implements PlaceManager.OnRequestReadyCallback , GraphRequest.Callback {

        @Override
        public void onCompleted(GraphResponse response) {

            Log.v(LOG_TAG,response.toString());

        }

        @Override
        public void onLocationError(PlaceManager.LocationError error) {

            Log.e(LOG_TAG,"LocationError "+error);

        }

        @Override
        public void onRequestReady(GraphRequest graphRequest) {

            graphRequest.setCallback(this);
            graphRequest.executeAsync();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {

                    return;

                }else {
                    //Permission denied
                }
                return;
        }
    }
}
