package cat.jorda.xavier.lespellicules.data;

/**
 * Created by xj1 on 24/01/2017.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesContract
{
    public static final String CONTENT_AUTHORITY = "cat.jorda.xavier.lespelicules.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MoviesEntry implements BaseColumns
    {
        // Constant item for all tables
        public static final String _ID = "_id";

        // table name
        public static final String TABLE_MOVIES = "movies";
        // columns
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORI_TITLE = "original_title";
        public static final String COLUMN_ORI_LAN   = "original_language";
        public static final String COLUMN_POSTER_PATH   = "poster_path";
        public static final String COLUMN_LOCAL_POSTER_PATH    = "poster";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_COUNT    = "vote_count";
        public static final String COLUMN_VOTE_AVG  = "vote_avg";
        public static final String COLUMN_ADULT_TYPE    = "adult_type";
        public static final String COLUMN_OVERVIEW  = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POPULARITY = "release_date";
        public static final String COLUMN_GENERE    = "genere";

        // Comon to all the tables related the movies
        public static final String COLUMN_MOVIES_ID = "movies_id";

        // table REVIEWS| movies 1 - reviews n
        public static final String TABLE_REVIEWS    = "reviews";
        // columns
        public static final String COLUMN_AUTHOR    = "author";
        public static final String COLUMN_CONTENT   = "content";
        public static final String COLUMN_URL   = "url";

        // table TRAILERS| movies 1 - trailers n
        public static final String TABLE_TRAILERS   = "trailers";
        // columns
        public static final String COLUMN_KEY   = "key";
        public static final String COLUMN_NAME  = "name";
        public static final String COLUMN_SITE  = "SITE";
        public static final String COLUMN_TYPE  = "type";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_MOVIES).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        // create content uri
        public static final Uri CONTENT_TRAILERS_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_TRAILERS).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_TRAILERS_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;
        // create cursor of base type item for single entry
        public static final String CONTENT_TRAILERS_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        // create content uri
        public static final Uri CONTENT_REVIEWS_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_REVIEWS).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_REVIEWS_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;
        // create cursor of base type item for single entry
        public static final String CONTENT_REVIEWS_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        // for building URIs on insertion
        public static Uri buildMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewsUri(long id){
            return ContentUris.withAppendedId(CONTENT_REVIEWS_URI, id);
        }

        public static Uri buildTrailersUri(long id){
            return ContentUris.withAppendedId(CONTENT_TRAILERS_URI, id);
        }
    }
}
