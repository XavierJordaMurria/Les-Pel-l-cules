package cat.jorda.xavier.lespellicules.reviews;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cat.jorda.xavier.lespellicules.R;
import cat.jorda.xavier.lespellicules.trailers.TrailersInfo;

/**
 * Created by xj1 on 17/01/2017.
 */

public class ReviewsAdapter extends ArrayAdapter<ReviewsInfo>
{

    ArrayList<ReviewsInfo> mReviews;
    public ReviewsAdapter(Context context, ArrayList<ReviewsInfo> reviews)
    {
        super(context, 0, reviews);
        mReviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        ReviewsInfo reviewInfo = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);

        // Lookup view for data population
        TextView content = (TextView) convertView.findViewById(R.id.review_text);
        content.setText(reviewInfo.mContent);

        TextView author = (TextView) convertView.findViewById(R.id.review_author);
        author.setText(reviewInfo.mAuthor);

        // Return the completed view to render on screen
        return convertView;
    }

    public int getCount()
    {
        return mReviews.size();
    }

    public long getItemId(int position)
    {
        return position;
    }

}
