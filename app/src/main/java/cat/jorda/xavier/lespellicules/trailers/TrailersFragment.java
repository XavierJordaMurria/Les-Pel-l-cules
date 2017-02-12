/**
 @file TrailersFragment.java
 @author Xavier Jorda
 @date January 2017
 @brief Class TrailersFragment of the LesPel•licules app shows a list of the loaded trailers

 (c) Jorda_Xavier_Ltd., 2010.  All rights reserved.

 This software is the property of Jorda_Xavier_Ltd and may not be
 copied or reproduced otherwise than on to a single hard disk for
 backup or archival purposes. The source code is confidential
 information and must not be disclosed to third parties or used
 without the express written permission of Jorda_Xavier_Ltd.
 */

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
import android.widget.TextView;

import java.util.ArrayList;

import cat.jorda.xavier.lespellicules.MainApplication;
import cat.jorda.xavier.lespellicules.R;

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

        Log.d(TAG, "onCreate");

        context = getActivity().getApplicationContext();
        Bundle args = getArguments();
        mMovieindex = args.getInt("index", 0);
        MainApplication.getInstance().mMoviesSArray.get(mMovieindex);

        arrayOfTrailers = new ArrayList<>(MainApplication.getInstance().mMoviesSArray.get(mMovieindex).mTrailersInfoList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView");

        final View trailersListView = inflater.inflate(R.layout.trailers_list_view,container, false);

        // Create the adapter to convert the array to views
        TrailersAdapter adapter = new TrailersAdapter(context, arrayOfTrailers);

        // Attach the adapter to a ListView
        ListView listView = (ListView)trailersListView.findViewById(R.id.trailers_list_view);

        View emptyView = (View) getActivity().findViewById(R.id.no_data_trailers);

        listView.setEmptyView(emptyView);

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
