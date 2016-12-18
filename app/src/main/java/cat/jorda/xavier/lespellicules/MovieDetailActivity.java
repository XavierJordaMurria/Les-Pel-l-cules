package cat.jorda.xavier.lespellicules;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cat.jorda.xavier.lespellicules.util.Constants;


/**
 * Created by xj on 18/12/2016.
 */

public class MovieDetailActivity extends Activity
{
    private static final String TAG = "MovieDetailActivity";

    private ImageView poster;
    private TextView movieTitle, releaseDate, duration, originalLang, score, scoreCount, popularity, adultType, overview;
    private MovieInfo currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        int itemClicked = getIntent().getExtras().getInt(Constants.MOVIE_POSTER_POSITION);
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

        fillView(currentMoviecurrentMovie);
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
}
