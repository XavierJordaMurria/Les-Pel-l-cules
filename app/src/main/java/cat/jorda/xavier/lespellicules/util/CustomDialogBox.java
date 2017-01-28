package cat.jorda.xavier.lespellicules.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cat.jorda.xavier.lespellicules.MovieSearchTypes;
import cat.jorda.xavier.lespellicules.R;

/**
 * Created by xj on 16/12/2016.
 */

public class CustomDialogBox extends DialogFragment implements  RadioGroup.OnCheckedChangeListener
{
    private final static String TAG = "CustomDialogBox";
    int mNum;

    private RadioGroup radioGroup;
    private Handler handler = new Handler();

    private OnMoviesTypeSearchChanged mCallback;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static CustomDialogBox newInstance(int num)
    {
        Log.d(TAG, "CustomDialogBox new Instance");

        CustomDialogBox f = new CustomDialogBox();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    /* (non-Javadoc)
  * @see android.app.DialogFragment#onAttach(android.app.Activity)
  */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        Log.d(TAG, "onAttach");

        try
        {
            mCallback = (OnMoviesTypeSearchChanged)activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnMoviesTypeSearchChanged.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        mNum = getArguments().getInt("num");
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView");

        View v = inflater.inflate(R.layout.custom_dialog_box, container, false);

        radioGroup = (RadioGroup)v.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        return v;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.popular_type:
                mCallback.onMoviesTChanged(MovieSearchTypes.POPULAR);
                break;
            case R.id.topRated_type:
                mCallback.onMoviesTChanged(MovieSearchTypes.TOP_RATED);
                break;
            case R.id.nowPlaying_type:
                mCallback.onMoviesTChanged(MovieSearchTypes.NOW_PLAYING);
                break;
            case R.id.latest_type:
                mCallback.onMoviesTChanged(MovieSearchTypes.LATEST);
                break;
            case R.id.upcoming_type:
                mCallback.onMoviesTChanged(MovieSearchTypes.UPCOMING);
                break;
            case R.id.favourite_type:
                mCallback.onMoviesTChanged(MovieSearchTypes.FAVOURITES);
                break;
        }

        handler.postDelayed(new MyDismissRunnable(this), 1000);
    }

    private static class MyDismissRunnable implements Runnable
    {
        private WeakReference<DialogFragment> df;

        public MyDismissRunnable(DialogFragment dialogF)
        {
            this.df = new WeakReference<DialogFragment>(dialogF);
        }

        @Override
        public void run()
        {
            if (df.get() != null)
                df.get().dismiss();
        }
    }

    /**
     * An interface containing onTimePicked() method signature.
     * Container Activity must implement this interface.
     */
    public interface OnMoviesTypeSearchChanged
    {
        void onMoviesTChanged(@MovieSearchTypes.IMoviesSerchTypes int currentMovieSearch);
    }
}