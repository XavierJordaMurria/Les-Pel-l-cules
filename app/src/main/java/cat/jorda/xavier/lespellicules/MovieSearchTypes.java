/**
 @file MovieSearchTypes.java
 @author Xavier Jorda
 @date January 2017
 @brief Class providing Movie search enum type

 (c) Jorda_Xavier_Ltd., 2010.  All rights reserved.

 This software is the property of Jorda_Xavier_Ltd and may not be
 copied or reproduced otherwise than on to a single hard disk for
 backup or archival purposes. The source code is confidential
 information and must not be disclosed to third parties or used
 without the express written permission of Jorda_Xavier_Ltd.
 */

package cat.jorda.xavier.lespellicules;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MovieSearchTypes
{
    public static final int POPULAR         = 0;
    public static final int TOP_RATED       = 1;
    public static final int NOW_PLAYING     = 2;
    public static final int LATEST          = 3;
    public static final int UPCOMING        = 4;
    public static final int FAVOURITES      = 5;

    @IntDef({POPULAR, TOP_RATED,NOW_PLAYING,LATEST,UPCOMING,FAVOURITES})
    @Retention(RetentionPolicy.SOURCE)

    public @interface IMoviesSerchTypes {}
}
