package cat.jorda.xavier.lespellicules;

import android.app.Application;
import android.content.res.Configuration;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xj on 11/12/2016.
 */

public class MainApplication extends Application
{
    private static MainApplication singleton;

    public List<MovieInfo> mMoviesSArray = new ArrayList<>();

    public static MainApplication getInstance()
    {
        return singleton;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        singleton = this;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
    }
}
