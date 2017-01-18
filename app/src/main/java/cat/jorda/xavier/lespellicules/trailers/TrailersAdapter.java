package cat.jorda.xavier.lespellicules.trailers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cat.jorda.xavier.lespellicules.R;

/**
 * Created by xj1 on 17/01/2017.
 */

public class TrailersAdapter extends ArrayAdapter<TrailersInfo>
{

    ArrayList<TrailersInfo> mTrailers;
    public TrailersAdapter(Context context, ArrayList<TrailersInfo> trailers)
    {
        super(context, 0, trailers);
        mTrailers = trailers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        TrailersInfo trailerInfo = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, parent, false);

        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.trailer_title);
        // Populate the data into the template view using the data object
        title.setText(trailerInfo.mName);
        // Return the completed view to render on screen
        return convertView;
    }

    public int getCount()
    {
        return mTrailers.size();
    }

    public long getItemId(int position)
    {
        return position;
    }

}
