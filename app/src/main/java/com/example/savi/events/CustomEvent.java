package com.example.savi.events;

/**
 * Created by savi on 02.05.2017.
 */

public class CustomEvent {

    private String mEventName;
    private String mPlaceName;
    private String mEventStartTime;
    private long mEventId;

    public CustomEvent(String eventName,String placeName,String eventStartTime,long eventId) {
        mEventName=eventName;
        mPlaceName=placeName;
        mEventId=eventId;
        mEventStartTime=eventStartTime;

    }


    public String getEventName()
    {
        return mEventName;
    }
    public  String getPlaceName()
    {
        return mPlaceName;
    }
    public long getEventId(){return mEventId;}
    public String getEventStartTime(){return mEventStartTime;}


}
