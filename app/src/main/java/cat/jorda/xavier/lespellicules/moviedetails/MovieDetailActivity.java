package cat.jorda.xavier.lespellicules.moviedetails;

import android.app.Fragment;
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

import com.squareup.picasso.Picasso;

import java.util.List;

import cat.jorda.xavier.lespellicules.MainApplication;
import cat.jorda.xavier.lespellicules.R;
import cat.jorda.xavier.lespellicules.reviews.ReviewsFragment;
import cat.jorda.xavier.lespellicules.reviews.ReviewsInfo;
import cat.jorda.xavier.lespellicules.trailers.TrailersFragment;
import cat.jorda.xavier.lespellicules.trailers.TrailersInfo;
import cat.jorda.xavier.lespellicules.util.Constants;
import cat.jorda.xavier.lespellicules.util.HttpRequestAsync;
import cat.jorda.xavier.lespellicules.util.IHttpRequestCallback;


/**
 * Created by xj on 18/12/2016.
 */

public class MovieDetailActivity extends FragmentActivity implements IHttpRequestCallback
{
    private static final String TAG = "MovieDetailActivity";

    private ImageView poster;
    private TextView movieTitle, releaseDate, duration, originalLang, score, scoreCount, popularity, adultType, overview;
    private MovieInfo currentMovie;
    private Button  trailersBtn, reviewsBtn;
    private FrameLayout frameLayout;

    private int itemClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        itemClicked = getIntent().getExtras().getInt(Constants.MOVIE_POSTER_POSITION);
        final MovieInfo currentMoviecurrentMovie =  MainApplication.getInstance().mMoviesSArray.get(itemClicked);

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

        fillView(currentMoviecurrentMovie);

        HttpRequestAsync httpReqReviews = new HttpRequestAsync(Constants.TMDB_REQUESTS.REVIEWS_URL, this).setSpecificMovieId(currentMoviecurrentMovie.mID);
        httpReqReviews.execute();

        HttpRequestAsync httpReqTrailes = new HttpRequestAsync(Constants.TMDB_REQUESTS.TRAILERS_URL, this).setSpecificMovieId(currentMoviecurrentMovie.mID);;
        httpReqTrailes.execute();
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
}
