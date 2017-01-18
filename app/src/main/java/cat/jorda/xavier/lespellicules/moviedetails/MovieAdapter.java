/**
 * Created by xj on 09/12/2016.
 */

package cat.jorda.xavier.lespellicules.moviedetails;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cat.jorda.xavier.lespellicules.R;
import cat.jorda.xavier.lespellicules.util.Constants;

public class MovieAdapter extends ArrayAdapter<MovieInfo>
{
    private final String TAG = "MovieAdapter";
    private Context context;

    public MovieAdapter(Context context, List<MovieInfo> moviesSArr)
    {
        super(context, 0, moviesSArr);

        Log.d(TAG, "MovieAdapter Constructor with #elements =" + moviesSArr.size());
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        View gridCell = convertView;

        MovieInfo mvInfo = (MovieInfo)getItem(position);

        if (gridCell == null)
        {
            LayoutInflater vi = LayoutInflater.from(context);
            gridCell = vi.inflate(R.layout.movie_grid_cell, null);
        }

        // set image based on selected text
        ImageView poster = (ImageView) gridCell.findViewById(R.id.movie_poster);

        Picasso.with(context).load(Constants.TMDB_BASE_IMG_URL+mvInfo.mPosterPath).into(poster);

        return gridCell;
    }
}