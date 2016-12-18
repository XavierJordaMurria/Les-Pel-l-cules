package cat.jorda.xavier.lespellicules.util;

/**
 * Created by xj on 06/12/2016.
 */

public class Constants
{
    public static final String KEY_ADULT = "adult";
    public static final String KEY_ID = "id";
    public static final String KEY_ORG_TITLE = "original_title";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ORG_LNG = "original_language";
    public static final String KEY_VOTE_CNT = "vote_count";
    public static final String KEY_VOTE_AVG = "vote_average";
    public static final String KEY_POSTER = "poster_path";
    public static final String KEY_BACKDROP = "backdrop_path";
    public static final String KEY_POPULARITY = "popularity";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_REL_DATE = "release_date";
    public static final String KEY_GENERES = "genre_ids";


    /**
     *      TMDB: the movie db
     *
     * Get your onw key from the following link
     * https://www.themoviedb.org/?_dc=1481275050
     */
    public static final String TMDB_KEY = "";
    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String TMDB_BASE_IMG_URL = "http://image.tmdb.org/t/p/w500//";
    public static final String TMDB_NOW_PLAYING_URL = "now_playing";
    public static final String TMDB_POPULAR_URL = "popular";
    public static final String TMDB_TOP_RATED_URL = "top_rated";
    public static final String TMDB_LATEST_URL = "latest";
    public static final String TMDB_UPCOMING_URL = "upcoming";

    /**
     * Intent Key for starting movieDetailActivity
     */
    public static final String MOVIE_POSTER_POSITION = "movie_poster_position";

    /**
     * Movies Types Names
     */
    public static final String POPULAR      = "Popular";
    public static final String TOP_RATED    = "Top Rated";
    public static final String NOW_PLAYING  = "Now Playing";
    public static final String LATEST       = "Latest";
    public static final String UPCOMING     = "Upcoming";

}