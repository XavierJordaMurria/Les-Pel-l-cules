package cat.jorda.xavier.lespellicules.util;

/**
 * Created by xj on 08/12/2016.
 */

public interface IHttpRequestCallback
{
    void onTaskDone(String responseData);

    void onError();
}
