
/**
 * Created by xj on 08/12/2016.
 */


package cat.jorda.xavier.lespellicules.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestAsync extends AsyncTask<String, Void, String>
{

    private final static String TAG = "HttpRequestAsync";

    private Context mContext;
    private IHttpRequestCallback onTaskDoneListener;
    private String urlStr = "";
    private String fullURL = "";
    private int pageNum = 1;

    public HttpRequestAsync(Context context, String specificAPI, IHttpRequestCallback onTaskDoneListener, int pNum)
    {
        this(context, specificAPI, onTaskDoneListener);
        this.pageNum = pNum;
    }

    public HttpRequestAsync(Context context, String specificAPI, IHttpRequestCallback onTaskDoneListener)
    {
        this.mContext = context;
        this.urlStr = Constants.TMDB_BASE_URL+specificAPI+"?api_key="+Constants.TMDB_KEY+"&language=en-US";
        this.onTaskDoneListener = onTaskDoneListener;
    }

    /**
     * This method will increase by one the page num reques and trigger an asyncTask to the
     * REST API defined in the constructor.
     */
    public void getNewPage()
    {
        this.pageNum = this.pageNum + 1;
        this.fullURL = this.urlStr + "&page="+pageNum;
        this.execute();
    }

    /**
     * ex:API request for:
     * NowPlaying movies:
     *      https://api.themoviedb.org/3/movie/now_playing?api_key=your_key&language=en-US&page=1
     * Popular:
     *      https://api.themoviedb.org/3/movie/popular?api_key=your_key&language=en-US&page=1
     * TopRated:
     *      https://api.themoviedb.org/3/movie/top_rated?api_key=your_key&language=en-US&page=1
     **/

     @Override
    protected String doInBackground(String... params)
    {
        try
        {
            this.fullURL = this.urlStr + "&page="+pageNum;

            URL mUrl = new URL(fullURL);
            HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Content-length", "0");
            httpConnection.setUseCaches(false);
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setConnectTimeout(100000);
            httpConnection.setReadTimeout(100000);

            Log.d(TAG, "Doing async request to:" +httpConnection.getURL().toString());

            httpConnection.connect();

            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                Log.d(TAG, "responseCode HTTP_OK");
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                }

                br.close();
                return sb.toString();
            }
            else
            {
                Log.e(TAG, "Something went wrong, response code " + responseCode);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);

        if (onTaskDoneListener != null && s != null)
            onTaskDoneListener.onTaskDone(s);
        else
            onTaskDoneListener.onError();
    }
}
