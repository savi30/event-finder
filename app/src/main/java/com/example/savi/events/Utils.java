package com.example.savi.events;

import android.util.Log;

import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savi on 08.05.2017.
 */

public class Utils {

    //private constructor
    private Utils() {}

    //Parsing the JSON response from the GraphAPI
    public static List<CustomEvent> parseJson(GraphResponse response) {

        List<CustomEvent> events = new ArrayList<>();

        JSONObject jsonObject = response.getJSONObject();

        if (jsonObject != null) {

            try {
                JSONArray data = jsonObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {

                    JSONObject currentEvent = data.getJSONObject(i);

                    long eventID = currentEvent.getLong("id");
                    String eventName = currentEvent.getString("name");
                    String eventStartTime = currentEvent.getString("start_time");
                    JSONObject place = currentEvent.getJSONObject("place");
                    String placeName = place.getString("name");

                    CustomEvent event = new CustomEvent(eventName, placeName, eventStartTime, eventID);

                    events.add(event);
                }

            } catch (JSONException e) {
                Log.e(MainActivity.class.getName(), e.toString());
            }

        }

        return events;

    }
}
