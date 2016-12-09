package cat.jorda.xavier.lespellicules;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cat.jorda.xavier.lespellicules.util.HttpRequestAsync;
import cat.jorda.xavier.lespellicules.util.IHttpRequestCallback;

import cat.jorda.xavier.lespellicules.util.Constants;

public class MainActivity extends AppCompatActivity implements IHttpRequestCallback
{

    private HttpRequestAsync httpReq;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpReq = new HttpRequestAsync(getApplicationContext(),Constants.TMDB_POPULAR_URL, this);
        httpReq.execute();
    }


    public void onTaskDone(String responseData)
    {
        Log.d(TAG, "onTaskDone received responseDate =>"+ responseData);
    }

    public void onError()
    {}
}
