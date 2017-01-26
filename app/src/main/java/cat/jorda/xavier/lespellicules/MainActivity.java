package cat.jorda.xavier.lespellicules;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cat.jorda.xavier.lespellicules.data.MoviesContract;
import cat.jorda.xavier.lespellicules.data.PeliculesDB;
import cat.jorda.xavier.lespellicules.moviedetails.MovieAdapter;
import cat.jorda.xavier.lespellicules.moviedetails.MovieDetailActivity;
import cat.jorda.xavier.lespellicules.moviedetails.MovieInfo;
import cat.jorda.xavier.lespellicules.reviews.ReviewsInfo;
import cat.jorda.xavier.lespellicules.trailers.TrailersInfo;
import cat.jorda.xavier.lespellicules.util.*;

import static android.R.id.list;
import static cat.jorda.xavier.lespellicules.MovieSearchTypes.*;

public class MainActivity extends AppCompatActivity implements IHttpRequestCallback,
        CustomDialogBox.OnMoviesTypeSearchChanged
{
    private final String TAG = "MainActivity";

    private HttpRequestAsync httpReq;
    private GridView gridView;
    private TextView searchType;

    //region MainActivity LIFE CYCLE Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpReq = new HttpRequestAsync(Constants.TMDB_REQUESTS.POPULAR_URL, this);
        httpReq.execute();

        gridView = (GridView) findViewById(R.id.movie_grid);

        // Set an item click listener for GridView widget
        gridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)->
        {
            Log.d(TAG,"Clicked item at position:" + position);
            Intent mIntent = new Intent(this, MovieDetailActivity.class);
            mIntent.putExtra(Constants.MOVIE_POSTER_POSITION, position);

            startActivity(mIntent);
        });

        searchType = (TextView) findViewById(R.id.search_type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case (R.id.sort_by_title):
                Collections.sort(MainApplication.getInstance().mMoviesSArray,
                        (p1, p2) -> p1.mTitle.compareToIgnoreCase(p2.mTitle));
                break;

            case (R.id.sort_by_score):
                Collections.sort(MainApplication.getInstance().mMoviesSArray,
                        (p1, p2) -> p1.mVoteAvg.compareTo(p2.mVoteAvg));
                break;

            case (R.id.search):
                showDialog();
                break;

            case (R.id.favourites):
                loadFavouritesMovies();
                break;
        }

        gridView.setAdapter(new MovieAdapter(this, MainApplication.getInstance().mMoviesSArray));

        return super.onOptionsItemSelected(item);
    }

    //region IHtppRequestCallBack methods
    public void onTaskDone(String responseData)
    {
        Log.d(TAG, "onTaskDone received responseDate =>"+ responseData);
        gridView.setAdapter(new MovieAdapter(this, MainApplication.getInstance().mMoviesSArray));
    }

    @Override
    public void onReviewsDone(List<ReviewsInfo> revInfo)
    {}

    @Override
    public void onTrailersDone(List<TrailersInfo> traiInfo)
    {}

    public void onError()
    {}
    //endregion

    //region MainActivity PRIVATE methods
    private void printMoviewTitles(List<MovieInfo> mInfoList)
    {
        for (MovieInfo mInfo : mInfoList)
            Log.d(TAG,"Movie Title: " + mInfo.mTitle);
    }

    private void showDialog()
    {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        if (prev != null)
            ft.remove(prev);

        ft.addToBackStack(null);

        // Create and show the dialog. 2, 4
        DialogFragment newFragment = CustomDialogBox.newInstance(4);
        newFragment.show(ft, "dialog");
    }

    private void loadFavouritesMovies()
    {
        Log.d(TAG, "loadFavouritesMovies");

        List<MovieInfo> tmpMovieList = new ArrayList<>();

        // Run query
        Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
        String[] projection = new String[] {MoviesContract.MoviesEntry._ID,
                MoviesContract.MoviesEntry.COLUMN_TITLE,
                MoviesContract.MoviesEntry.COLUMN_ORI_TITLE,
                MoviesContract.MoviesEntry.COLUMN_ORI_LAN,
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
                MoviesContract.MoviesEntry.COLUMN_LOCAL_POSTER_PATH,
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,
                MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT,
                MoviesContract.MoviesEntry.COLUMN_VOTE_AVG,
                MoviesContract.MoviesEntry.COLUMN_ADULT_TYPE,
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
                MoviesContract.MoviesEntry.COLUMN_POPULARITY,
                MoviesContract.MoviesEntry.COLUMN_GENERE};

        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        if (cursor != null)
        {
            cursor.moveToFirst();
            MovieInfo movieTmp;
            for (int i = 0; i < cursor.getCount(); i++)
            {
                movieTmp = new MovieInfo(cursor.getInt(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_ORI_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_ORI_LAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_POPULARITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_VOTE_AVG)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_ADULT_TYPE)) > 0,
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE)),
                        new int[]{cursor.getInt(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_GENERE))})
                        .setMovieLocalPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COLUMN_LOCAL_POSTER_PATH)));

                tmpMovieList.add(movieTmp);

                cursor.moveToNext();
            }

            // always close the cursor
            cursor.close();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Favourite DB is empty",Toast.LENGTH_LONG);
        }

        //replace main movies list.
    }
    //endregion

    public void onMoviesTChanged(@MovieSearchTypes.IMoviesSerchTypes int currentMovieSearch)
    {
        Log.d(TAG, "currentMovieSearch" + currentMovieSearch);
        switch (currentMovieSearch)
        {
            case POPULAR:
                searchType.setText(Constants.POPULAR);
                Log.d(TAG, "We got this new movie search:" + Constants.POPULAR);
                httpReq = new HttpRequestAsync(Constants.TMDB_REQUESTS.POPULAR_URL, this);
                break;

            case TOP_RATED:
                searchType.setText(Constants.TOP_RATED);
                Log.d(TAG, "We got this new movie search:" + Constants.TOP_RATED);
                httpReq = new HttpRequestAsync(Constants.TMDB_REQUESTS.TOP_RATED_URL, this);
                break;

            case NOW_PLAYING:
                searchType.setText(Constants.NOW_PLAYING);
                Log.d(TAG, "We got this new movie search:" + Constants.NOW_PLAYING);
                httpReq = new HttpRequestAsync(Constants.TMDB_REQUESTS.NOW_PLAYING_URL, this);
                break;

            case LATEST:
                searchType.setText(Constants.LATEST);
                Log.d(TAG, "We got this new movie search:" + Constants.LATEST);
                httpReq = new HttpRequestAsync(Constants.TMDB_REQUESTS.LATEST_URL, this);
                break;

            case UPCOMING:
                searchType.setText(Constants.UPCOMING);
                Log.d(TAG, "We got this new movie search:" + Constants.UPCOMING);
                httpReq = new HttpRequestAsync(Constants.TMDB_REQUESTS.UPCOMING_URL, this);
                break;

            default:
                Log.e(TAG, "No possible someting when wrong");
        }
        httpReq.execute();
    }
}
