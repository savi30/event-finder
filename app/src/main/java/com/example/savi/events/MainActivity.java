package com.example.savi.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

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

                                        ListView eventListView= (ListView) findViewById(R.id.list);

                                        List<CustomEvent> events = new ArrayList<CustomEvent>();

                                        JSONObject jsonObject = response.getJSONObject();

                                        if(jsonObject!=null) {


                                            try {
                                                JSONArray data= jsonObject.getJSONArray("data");

                                                for (int i=0;i<data.length();i++) {

                                                    JSONObject currentEvent=data.getJSONObject(i);

                                                    long eventID=currentEvent.getLong("id");
                                                    String eventName=currentEvent.getString("name");
                                                    String eventStartTime=currentEvent.getString("start_time");
                                                    JSONObject place = currentEvent.getJSONObject("place");
                                                    String placeName = place.getString("name");

                                                    CustomEvent event = new CustomEvent(eventName,placeName,eventStartTime,eventID);

                                                    events.add(event);

                                                }

                                            } catch (JSONException e) {
                                                Log.e(MainActivity.class.getName(),e.toString());
                                            }

                                        }
                                        if(!events.isEmpty()){
                                            fab.setVisibility(View.GONE);
                                        }
                                        mEventAdapter=new CustomEventAdapter(MainActivity.this,events);
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

            }
            else {
                Profile.fetchProfileForCurrentAccessToken();
            }
        }
        else {
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {

                    FacebookSdk.setClientToken(String.valueOf(R.string.APP_CLIENT_TOKEN));


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
