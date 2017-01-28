package cat.jorda.xavier.lespellicules;

import android.app.Application;
import android.content.res.Configuration;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import cat.jorda.xavier.lespellicules.moviedetails.MovieInfo;

/**
 * Created by xj on 11/12/2016.
 */

public class MainApplication extends Application
{
    private static MainApplication singleton;

    public List<MovieInfo> mMoviesSArray = new ArrayList<>();
    public List<MovieInfo> mMovFavList = new ArrayList<>();

    public static MainApplication getInstance()
    {
        return singleton;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        singleton = this;

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))
                        .build());
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
