/**
 @file ReviewsFragment.java
 @author Xavier Jorda
 @date January 2017
 @brief Class ReviewsFragment show the reviews for the clicked movie.

 (c) Jorda_Xavier_Ltd., 2010.  All rights reserved.

 This software is the property of Jorda_Xavier_Ltd and may not be
 copied or reproduced otherwise than on to a single hard disk for
 backup or archival purposes. The source code is confidential
 information and must not be disclosed to third parties or used
 without the express written permission of Jorda_Xavier_Ltd.
 */

package cat.jorda.xavier.lespellicules.reviews;

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
import cat.jorda.xavier.lespellicules.trailers.TrailersAdapter;
import cat.jorda.xavier.lespellicules.trailers.TrailersInfo;

public class ReviewsFragment extends Fragment
{
    private static final String TAG = "TrailersFragment";
    private Context context;
    private int mMovieindex;
    private ArrayList<ReviewsInfo> arrayOfReviews;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        context = getActivity().getApplicationContext();


        Bundle args = getArguments();
        mMovieindex = args.getInt("index", 0);
        MainApplication.getInstance().mMoviesSArray.get(mMovieindex);

        arrayOfReviews = new ArrayList<>(MainApplication.getInstance().mMoviesSArray.get(mMovieindex).mReviewsInfoList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView");

        final View trailersListView = inflater.inflate(R.layout.trailers_list_view,container, false);

        // Create the adapter to convert the array to views
        ReviewsAdapter adapter = new ReviewsAdapter(context, arrayOfReviews);

        // Attach the adapter to a ListView
        ListView listView = (ListView)trailersListView.findViewById(R.id.trailers_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d(TAG,"listView item click on position:" + position);
            }
        });


        return trailersListView;
    }
}
