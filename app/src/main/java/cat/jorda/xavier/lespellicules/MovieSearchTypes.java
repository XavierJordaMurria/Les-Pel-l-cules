package cat.jorda.xavier.lespellicules;

import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Switch;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cat.jorda.xavier.lespellicules.util.Constants;

/**
 * Created by xj on 17/12/2016.
 */

public class MovieSearchTypes
{
    public static final String TAG = "MovieSearchTypes";

    public static final int POPULAR       = 0;
    public static final int TOP_RATED     = 1;
    public static final int NOW_PLAYING   = 2;
    public static final int LATEST        = 3;
    public static final int UPCOMING      = 4;

    @IntDef({POPULAR, TOP_RATED,NOW_PLAYING,LATEST,UPCOMING})
    @Retention(RetentionPolicy.SOURCE)

    public @interface IMoviesSerchTypes {}
}
