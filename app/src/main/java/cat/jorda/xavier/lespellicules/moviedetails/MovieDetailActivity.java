/**
 @file MovieDetailActivity.java
 @author Xavier Jorda
 @date January 2017
 @brief Class MovieDetailActivity show the details for the clicked movie.

 (c) Jorda_Xavier_Ltd., 2010.  All rights reserved.

 This software is the property of Jorda_Xavier_Ltd and may not be
 copied or reproduced otherwise than on to a single hard disk for
 backup or archival purposes. The source code is confidential
 information and must not be disclosed to third parties or used
 without the express written permission of Jorda_Xavier_Ltd.
 */


package cat.jorda.xavier.lespellicules.moviedetails;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cat.jorda.xavier.lespellicules.MainApplication;
import cat.jorda.xavier.lespellicules.R;
import cat.jorda.xavier.lespellicules.data.MoviesContract;
import cat.jorda.xavier.lespellicules.reviews.ReviewsFragment;
import cat.jorda.xavier.lespellicules.reviews.ReviewsInfo;
import cat.jorda.xavier.lespellicules.trailers.TrailersFragment;
import cat.jorda.xavier.lespellicules.trailers.TrailersInfo;
import cat.jorda.xavier.lespellicules.util.Constants;
import cat.jorda.xavier.lespellicules.util.HttpRequestAsync;
import cat.jorda.xavier.lespellicules.util.IHttpRequestCallback;

public class MovieDetailActivity extends FragmentActivity implements IHttpRequestCallback, LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = "MovieDetailActivity";

    private ImageView poster,favStar;
    private TextView movieTitle, releaseDate, duration, originalLang, score, scoreCount, popularity, adultType, overview;
    private MovieInfo currentMovie;
    private Button  trailersBtn, reviewsBtn;
    private FrameLayout frameLayout;

    private int itemClicked;
    private boolean favStartClicked = false;
    private static final int CURSOR_LOADER_ID = 0;

    private ContentValues movieValue;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE =
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        itemClicked = getIntent().getExtras().getInt(Constants.MOVIE_POSTER_POSITION);

        if( getIntent().getExtras().getString(Constants.MOVIE_LIST).equals(Constants.FAVOURITES))
            currentMovie =  MainApplication.getInstance().mMovFavList.get(itemClicked);
        else
            currentMovie =  MainApplication.getInstance().mMoviesSArray.get(itemClicked);

        Log.d(TAG, "Loading MOVIE: ===> " + currentMovie.mTitle);
        setContentView(R.layout.movie_detail);

        poster = (ImageView)findViewById(R.id.movie_details_poster);
        movieTitle = (TextView) findViewById(R.id.movie_details_title);
        releaseDate = (TextView) findViewById(R.id.movie_details_release_date);
        duration = (TextView) findViewById(R.id.movie_details_duration);
        originalLang = (TextView) findViewById(R.id.movie_details_org_lang);
        score = (TextView) findViewById(R.id.movie_details_score);
        scoreCount = (TextView) findViewById(R.id.movie_details_score_count);

        popularity = (TextView) findViewById(R.id.movie_details_popularity);
        adultType = (TextView) findViewById(R.id.movie_details_adult_type);

        overview = (TextView)findViewById(R.id.movie_details_overview);

        trailersBtn = (Button)findViewById(R.id.trailers_btn);
        trailersBtn.setOnClickListener((View v) ->
        {
            setFragment(new TrailersFragment());
        });

        reviewsBtn = (Button)findViewById(R.id.reviews_btn);
        reviewsBtn.setOnClickListener((View v) ->
        {
            setFragment(new ReviewsFragment());
        });

        frameLayout = (FrameLayout)findViewById(R.id.details_frame);

        favStar = (ImageView)findViewById(R.id.fav_star);

        //Updating the favStart accordingly to the latest state.
        if(contains(MainApplication.getInstance().mMovFavList, currentMovie.mID))
        {
            Log.d(TAG, "Current movie is in the FavMoviesList");
            favStar.setImageResource(R.drawable.fav_start_1);
            favStartClicked = true;
        }
        else
        {
            favStartClicked = false;
            favStar.setImageResource(R.drawable.fav_start_0);
        }

        favStar.setOnClickListener((View v) ->
        {
            if(!favStartClicked)
            {
                Log.d(TAG, "FAV START CLICKED");

                if(!verifyStoragePermissions(this))
                    return;

                //add to the DB
                favStar.setImageResource(R.drawable.fav_start_1);
                Picasso.with(getApplicationContext()).load(Constants.TMDB_BASE_IMG_URL+currentMovie.mPosterPath).into(new CustomTarget(this, currentMovie.mTitle+currentMovie.mID));
                MainApplication.getInstance().mMoviesSArray.get(itemClicked).setIsInFavouriteDBFlag(true);
            }
            else
            {
                Log.d(TAG, "FAV START UN-CLICKED");
                favStar.setImageResource(R.drawable.fav_start_0);
                MainApplication.getInstance().mMoviesSArray.get(itemClicked).setIsInFavouriteDBFlag(false);
                //remove to the DB
//                getContentResolver()..insert(MoviesContract.MoviesEntry.CONTENT_URI, movieValue);
                getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,"_ID=?", new String[]{String.valueOf(currentMovie.mID)});
            }

            favStartClicked = !favStartClicked;
        });

        fillView(currentMovie);

        HttpRequestAsync httpReqReviews = new HttpRequestAsync(Constants.TMDB_REQUESTS.REVIEWS_URL, this).setSpecificMovieId(currentMovie.mID);
        httpReqReviews.execute();

        HttpRequestAsync httpReqTrailes = new HttpRequestAsync(Constants.TMDB_REQUESTS.TRAILERS_URL, this).setSpecificMovieId(currentMovie.mID);;
        httpReqTrailes.execute();

        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume");

        frameLayout.setVisibility(View.GONE);
    }

    // This could be moved into an abstract BaseActivity
    // class for being re-used by several instances
    protected void setFragment(Fragment fragment)
    {
        Bundle args = new Bundle();
        args.putInt("index", itemClicked);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.details_frame, fragment);
        fragmentTransaction.commit();
        frameLayout.setVisibility(View.VISIBLE);
    }

    private void fillView(MovieInfo movInf)
    {
        Picasso.with(getApplicationContext()).load(Constants.TMDB_BASE_IMG_URL+movInf.mPosterPath).into(poster);
        movieTitle.setText(movInf.mTitle);
        releaseDate.setText(movInf.mReleaseDate);
        duration.setText("");
        originalLang.setText("Lng: " + movInf.mOriginalLanguage);
        score.setText("VoteAvg: " + movInf.mVoteAvg.toString());
        scoreCount.setText(String.valueOf("#Votes: "+ movInf.mVoteCount));
        popularity.setText(String.valueOf("Popularity: " + movInf.mPopularityIndex));
        adultType.setText(movInf.mAdultType?"Adult Type: YES":"Adult Type: NO");
        overview.setText(movInf.mOverveiw);
    }

    //region IHttpRequestCallback Methods
    @Override
    public void onTaskDone(String responseData)
    {}

    @Override
    public void onReviewsDone(List<ReviewsInfo> revInfo)
    {
        Log.d(TAG, "onReviewsDone callback received");
        MainApplication.getInstance().mMoviesSArray.get(itemClicked).addReview(revInfo);
    }

    @Override
    public void onTrailersDone(List<TrailersInfo> traiInfo)
    {
        Log.d(TAG, "onTrailersDone callback received");
        MainApplication.getInstance().mMoviesSArray.get(itemClicked).addTrailes(traiInfo);
    }

    @Override
    public void onError()
    {
        Log.w(TAG, "onError callback received");
    }
    //endregion


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this,
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
//        mFlavorAdapter.swapCursor(data);
    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
//        mFlavorAdapter.swapCursor(null);
    }

    // insert data into database
    public void insertData(String localPosterPath)
    {
        Log.d(TAG, "insertData movie: " + currentMovie.mTitle);

        movieValue = new ContentValues();
        movieValue.put(MoviesContract.MoviesEntry._ID, currentMovie.mID);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_TITLE, currentMovie.mTitle);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_ORI_TITLE, currentMovie.mOriginalTitle);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_ORI_LAN, currentMovie.mOriginalLanguage);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, currentMovie.mPosterPath);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_LOCAL_POSTER_PATH, localPosterPath);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, currentMovie.mBackdropPath);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, currentMovie.mPopularityIndex);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, currentMovie.mVoteCount);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVG, currentMovie.mVoteAvg);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_ADULT_TYPE, currentMovie.mAdultType);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, currentMovie.mOverveiw);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, currentMovie.mReleaseDate);
        movieValue.put(MoviesContract.MoviesEntry.COLUMN_GENERE, currentMovie.mGenereIDs[0]);

        getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, movieValue);
    }

    //target to save
    private class CustomTarget implements Target
    {
        WeakReference<MovieDetailActivity> activityRef;
        String mImageName;

        public CustomTarget(MovieDetailActivity activity, String imgName)
        {
            activityRef = new WeakReference<MovieDetailActivity>(activity);
            mImageName = imgName;
        }

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String localPosterPath = Environment.getExternalStorageDirectory().getPath() + "/" + mImageName + ".jpg";
                        File file = new File(localPosterPath);

                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                        ostream.close();

                        Log.d(TAG, "Saved imaged: " + mImageName);

                        insertData(localPosterPath);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable)
        {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable)
        {
            if (placeHolderDrawable != null)
            {
            }
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    private boolean verifyStoragePermissions(Activity activity)
    {
        boolean permissionCheck = false;
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            permissionCheck = false;
        }
        else
        {
            permissionCheck = true;
        }
        return permissionCheck;
    }

    boolean contains(List<MovieInfo> list, int id)
    {
        for (MovieInfo item : list)
        {
            if (item.mID == id)
                return true;
        }
        return false;
    }
}
