/**
 * Created by xj on 09/12/2016.
 */

package cat.jorda.xavier.lespellicules;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter
{
    private Context context;
    private final String[] mobileValues;

    public MovieAdapter(Context context, String[] mobileValues)
    {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null)
        {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.movie_grid_cell, null);

            // set value into textview
            TextView title = (TextView) gridView
                    .findViewById(R.id.movie_title);

            title.setText(mobileValues[position]);

            // set value into textview
            TextView rating = (TextView) gridView
                    .findViewById(R.id.movie_rating);

            rating.setText(mobileValues[position]);

            // set image based on selected text
            ImageView poster = (ImageView) gridView
                    .findViewById(R.id.movie_poster);

            String mobile = mobileValues[position];

//            if (mobile.equals("Windows"))
//            {
//                imageView.setImageResource(R.drawable.windows_logo);
//            }
//            else if (mobile.equals("iOS"))
//            {
//                imageView.setImageResource(R.drawable.ios_logo);
//            }
//            else if (mobile.equals("Blackberry"))
//            {
//                imageView.setImageResource(R.drawable.blackberry_logo);
//            }
//            else
//            {
//                imageView.setImageResource(R.drawable.android_logo);
//            }

        }
        else
        {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount()
    {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

}