
/**
 * Created by xj on 08/12/2016.
 */


package cat.jorda.xavier.lespellicules.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cat.jorda.xavier.lespellicules.MainApplication;
import cat.jorda.xavier.lespellicules.moviedetails.MovieInfo;
import cat.jorda.xavier.lespellicules.reviews.ReviewsInfo;
import cat.jorda.xavier.lespellicules.trailers.TrailersInfo;

public class HttpRequestAsync extends AsyncTask<String, Void, String>
{

    private final static String TAG = "HttpRequestAsync";

    private final WeakReference<IHttpRequestCallback> onTaskDoneListener;
    private String urlStr = "";
    private String fullURL = "";
    private int pageNum = 1;
    private String mMovieId = "";
    private Constants.TMDB_REQUESTS mSpecificAPI;

    private List<MovieInfo> tmpMovies = new ArrayList<>();
    private List<TrailersInfo> trailersInfoList = new ArrayList<>();
    private List<ReviewsInfo> reviewsInfoList = new ArrayList<>();

    public HttpRequestAsync(Constants.TMDB_REQUESTS specificAPI, IHttpRequestCallback onTaskDoneListener)
    {
        /**
         * TMDB_BASE_URL = https://api.themoviedb.org/3/movie/
         * specificAPI = now_playing
         * ?api_key=your_key
         * &language=en-US&page=1
         *
         *  https://api.themoviedb.org/3/movie/now_playing?api_key=your_key&language=en-US&page=1
         *  https://api.themoviedb.org/3/movie/157336/videos?api_key=your_key&language=en-US&page=1
         */
        mSpecificAPI = specificAPI;

        this.urlStr = Constants.TMDB_BASE_URL+mSpecificAPI.toString()+"?api_key="+Constants.TMDB_KEY+"&language=en-US";

        this.onTaskDoneListener = new WeakReference<>(onTaskDoneListener);
    }

    /**
     * Add a movie Id if you would like to do an specific request.
     * @param movieId Movie that we want to get the information from.
     * @return
     */
    public HttpRequestAsync setSpecificMovieId(int movieId)
    {
        this.mMovieId = Integer.toString(movieId);
        this.urlStr = Constants.TMDB_BASE_URL+mMovieId+"/"+mSpecificAPI+"?api_key="+Constants.TMDB_KEY+"&language=en-US";

        return this;
    }

    /**
     * Add a page parameter to the HtppRequestAsync obj.
     * @param page number to get from the API
     * @return object containing the new page information.
     */
    public HttpRequestAsync setPage(int page)
    {
        this.pageNum = page;
        return this;
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
                String jsonString = sb.toString();

                JSONObject jObject = new JSONObject(jsonString);

                JSONArray jArray = jObject.getJSONArray("results");

                if(mSpecificAPI == Constants.TMDB_REQUESTS.REVIEWS_URL)
                    parseReview(jArray);
                else if (mSpecificAPI == Constants.TMDB_REQUESTS.TRAILERS_URL)
                    parseTrailers(jArray);
                else
                    addResult2MoviesSArray(jArray);

                return jsonString;
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



        if (onTaskDoneListener.get() == null)
        {
            Log.e(TAG, "TaskDoneListener is null");
            return;
        }

        if (s != null)
        {
            if(mSpecificAPI == Constants.TMDB_REQUESTS.REVIEWS_URL)
                onTaskDoneListener.get().onReviewsDone(reviewsInfoList);
            else if (mSpecificAPI == Constants.TMDB_REQUESTS.TRAILERS_URL)
                onTaskDoneListener.get().onTrailersDone(trailersInfoList);
            else
            {
                MainApplication.getInstance().mMoviesSArray = new ArrayList<>(tmpMovies);
                onTaskDoneListener.get().onTaskDone(s);
            }
        }
        else
            onTaskDoneListener.get().onError();
    }


    private void parseReview(JSONArray jsonArray)
    {
        int restLength = jsonArray.length();

        for (int i=0; i < restLength; i++)
        {
            try
            {
                JSONObject oneObject = jsonArray.getJSONObject(i);

                String id   = oneObject.getString(Constants.KEY_ID);
                String author   = oneObject.getString(Constants.KEY_AUTHOR);
                String content  = oneObject.getString(Constants.KEY_CONTENT);
                String url  = oneObject.getString(Constants.KEY_URL);

                reviewsInfoList.add(new ReviewsInfo(id, author, content, url));
            }
            catch (JSONException e)
            {
                Log.e(TAG, "Something went wrong parsing the Reviews json result array");
            }
        }
    }

    private void parseTrailers(JSONArray jsonArray)
    {
        int restLength = jsonArray.length();

        for (int i=0; i < restLength; i++)
        {
            try
            {
                JSONObject oneObject = jsonArray.getJSONObject(i);

                String id = oneObject.getString(Constants.KEY_ID);
                String key = oneObject.getString(Constants.KEY_KEY);
                String name = oneObject.getString(Constants.KEY_NAME);
                String site = oneObject.getString(Constants.KEY_SITE);
                String type = oneObject.getString(Constants.KEY_TYPE);

                trailersInfoList.add(new TrailersInfo(id, key, name, site, type));
            }
            catch (JSONException e)
            {
                Log.e(TAG, "Something went wrong parsing the trailers json result array");
            }
        }
    }

    private void addResult2MoviesSArray(JSONArray jArray)
    {
        int restLength = jArray.length();

        for (int i=0; i < restLength; i++)
        {
            try
            {
                JSONObject oneObject = jArray.getJSONObject(i);

                int id = oneObject.getInt(Constants.KEY_ID);


                // Retrieve number array from JSON object.
                JSONArray array = oneObject.optJSONArray(Constants.KEY_GENERES);

                // Deal with the case of a non-array value.
                if (array == null) { /*...*/ }

                // Create an int array to accomodate the numbers.
                int[] genArr = new int[array.length()];

                // Extract numbers from JSON array.
                for (int j = 0; j < array.length(); ++j)
                {
                    genArr[j] = array.optInt(j);
                }

                MovieInfo movie = new MovieInfo(id,
                        oneObject.getString(Constants.KEY_TITLE),
                        oneObject.getString(Constants.KEY_ORG_TITLE),
                        oneObject.getString(Constants.KEY_ORG_LNG),
                        oneObject.getString(Constants.KEY_POSTER),
                        oneObject.getString(Constants.KEY_BACKDROP),
                        oneObject.getInt(Constants.KEY_POPULARITY),
                        oneObject.getInt(Constants.KEY_VOTE_CNT),
                        oneObject.getDouble(Constants.KEY_VOTE_AVG),
                        oneObject.getBoolean(Constants.KEY_ADULT),
                        oneObject.getString(Constants.KEY_OVERVIEW),
                        oneObject.getString(Constants.KEY_REL_DATE),
                        genArr);

                tmpMovies.add(movie);
            }
            catch (JSONException e)
            {
                Log.e(TAG, "Something went wrong parsing the result array");
            }
        }
    }
}
