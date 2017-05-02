package com.example.savi.events;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by savi on 02.05.2017.
 */

public class CustomEventAdapter extends ArrayAdapter<CustomEvent> {

    public CustomEventAdapter(@NonNull Context context, @NonNull List<CustomEvent> CustomEvents) {
        super(context, 0, CustomEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView=convertView;
        if(listItemView==null) {
            listItemView= LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_view,parent,false);
        }

        CustomEvent currentEvent=getItem(position);

        TextView eventName=(TextView) listItemView.findViewById(R.id.event_name);

        eventName.setText(currentEvent.getEventName());

        TextView eventLocation=(TextView) listItemView.findViewById(R.id.event_location);

        eventLocation.setText(currentEvent.getPlaceName());

        TextView eventStartTime=(TextView) listItemView.findViewById(R.id.event_start_time);

        eventStartTime.setText(currentEvent.getEventStartTime());

        return listItemView;

    }
}
