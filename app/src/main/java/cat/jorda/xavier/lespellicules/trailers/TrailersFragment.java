package cat.jorda.xavier.lespellicules.trailers;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cat.jorda.xavier.lespellicules.MainApplication;
import cat.jorda.xavier.lespellicules.R;

/**
 * Created by xj1 on 17/01/2017.
 */

public class TrailersFragment extends Fragment
{
    private static final String TAG = "TrailersFragment";
    private Context context;
    private int mMovieindex;
    private ArrayList<TrailersInfo> arrayOfTrailers;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();


        Bundle args = getArguments();
        mMovieindex = args.getInt("index", 0);
        MainApplication.getInstance().mMoviesSArray.get(mMovieindex);

        arrayOfTrailers = new ArrayList<>(MainApplication.getInstance().mMoviesSArray.get(mMovieindex).mTrailersInfoList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        final View trailersListView = inflater.inflate(R.layout.trailers_list_view,container, false);

        // Create the adapter to convert the array to views
        TrailersAdapter adapter = new TrailersAdapter(context, arrayOfTrailers);

        // Attach the adapter to a ListView
        ListView listView = (ListView)trailersListView.findViewById(R.id.trailers_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d(TAG,"listView item click on position:" + position);
                watchYoutubeVideo(arrayOfTrailers.get(position).mKey);

            }
        });


        return trailersListView;
    }

    private void watchYoutubeVideo(String id)
    {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PackageManager packageManager = getActivity().getPackageManager();

        if (appIntent.resolveActivity(packageManager) != null)
        {
            startActivity(appIntent);
        }
        else
        {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d(TAG, "No Intent available to handle action");
            context.startActivity(webIntent);
        }
    }
}
