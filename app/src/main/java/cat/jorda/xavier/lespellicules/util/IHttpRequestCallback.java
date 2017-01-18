package cat.jorda.xavier.lespellicules.util;

import java.util.List;

import cat.jorda.xavier.lespellicules.reviews.ReviewsInfo;
import cat.jorda.xavier.lespellicules.trailers.TrailersInfo;

/**
 * Created by xj on 08/12/2016.
 */

public interface IHttpRequestCallback
{
    void onTaskDone(String responseData);

    void onReviewsDone(List<ReviewsInfo> revInfo);

    void onTrailersDone(List<TrailersInfo> traiInfo);

    void onError();
}
