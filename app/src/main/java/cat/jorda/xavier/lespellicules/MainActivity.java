package cat.jorda.xavier.lespellicules;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import cat.jorda.xavier.lespellicules.util.*;
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

        httpReq = new HttpRequestAsync(Constants.TMDB_POPULAR_URL, this);
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
    //endregion

    public void onMoviesTChanged(@MovieSearchTypes.IMoviesSerchTypes int currentMovieSearch)
    {
        Log.d(TAG, "currentMovieSearch" + currentMovieSearch);
        switch (currentMovieSearch)
        {
            case POPULAR:
                searchType.setText(Constants.POPULAR);
                Log.d(TAG, "We got this new movie search:" + Constants.POPULAR);
                httpReq = new HttpRequestAsync(Constants.TMDB_POPULAR_URL, this);
                break;

            case TOP_RATED:
                searchType.setText(Constants.TOP_RATED);
                Log.d(TAG, "We got this new movie search:" + Constants.TOP_RATED);
                httpReq = new HttpRequestAsync(Constants.TMDB_TOP_RATED_URL, this);
                break;

            case NOW_PLAYING:
                searchType.setText(Constants.NOW_PLAYING);
                Log.d(TAG, "We got this new movie search:" + Constants.NOW_PLAYING);
                httpReq = new HttpRequestAsync(Constants.TMDB_NOW_PLAYING_URL, this);
                break;

            case LATEST:
                searchType.setText(Constants.LATEST);
                Log.d(TAG, "We got this new movie search:" + Constants.LATEST);
                httpReq = new HttpRequestAsync(Constants.TMDB_LATEST_URL, this);
                break;

            case UPCOMING:
                searchType.setText(Constants.UPCOMING);
                Log.d(TAG, "We got this new movie search:" + Constants.UPCOMING);
                httpReq = new HttpRequestAsync(Constants.TMDB_UPCOMING_URL, this);
                break;

            default:
                Log.e(TAG, "No possible someting when wrong");
        }
        httpReq.execute();
    }
}
