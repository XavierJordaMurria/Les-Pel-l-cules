/**
 * Created by xj1 on 24/01/2017.
 */


package cat.jorda.xavier.lespellicules.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PeliculesDB extends SQLiteOpenHelper
{
    public static final String LOG_TAG = PeliculesDB.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "Pel•licules.db";
    private static final int DATABASE_VERSION = 13;

    public PeliculesDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.MoviesEntry.TABLE_MOVIES + "(" + MoviesContract.MoviesEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_ORI_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_ORI_LAN + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_LOCAL_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_VOTE_AVG + " REAL NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_ADULT_TYPE + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_GENERE + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " +
                MoviesContract.MoviesEntry.TABLE_TRAILERS + "(" + MoviesContract.MoviesEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIES_ID + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_TYPE+ " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " +
                MoviesContract.MoviesEntry.TABLE_REVIEWS + "(" + MoviesContract.MoviesEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_AUTHOR + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_URL + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_MOVIES);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.MoviesEntry.TABLE_MOVIES + "'");

        // re-create database
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_TRAILERS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.MoviesEntry.TABLE_TRAILERS + "'");

        // re-create database
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_REVIEWS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.MoviesEntry.TABLE_REVIEWS + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
