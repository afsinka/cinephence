package cinephile.afsinka.com.cinephile_on_the_fence_2;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cinephile.afsinka.com.cinephile_on_the_fence_2.util.Constants;
import cinephile.afsinka.com.cinephile_on_the_fence_2.util.Utils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by afsin on 19.2.2016.
 */
public class FragmentTwo extends Fragment {

    private TextView result;
    private TextView resultLink;
    private TextView lucky;
    private TextView luckyResult;
    private Button button;
    private RelativeLayout progressBar;
    private String selectedWord;

    public FragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_two, container, false);

        result = (TextView) v.findViewById(R.id.result);
        resultLink = (TextView) v.findViewById(R.id.resultLink);

        lucky = (TextView) v.findViewById(R.id.lucky);
        luckyResult = (TextView) v.findViewById(R.id.luckyResult);

        button = (Button) v.findViewById(R.id.wordButton);
        button.setOnClickListener(buttonListener());

        progressBar = (RelativeLayout) v.findViewById(R.id.loadingPanel);
        progressBar.setVisibility(View.INVISIBLE);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        getRandomMovie(getRandomWord());
    }

    private View.OnClickListener buttonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getRandomMovie(getRandomWord());
            }
        };
    }

    private String getRandomWord() {
        progressBar.setVisibility(View.VISIBLE);
        String word = Utils.getRandomWord(((GlobalApp) this.getActivity().getApplication()).getAllWords());
        return word;
    }

    private void getRandomMovie(String word) {
        getFromOmdbApi(word);
    }

    /**
     * http://www.omdbapi.com/
     */
    private void getFromOmdbApi(String movieName) {

        final String tempMovieName = movieName;

        String urlStr = Constants.omdbLuckyUrl + movieName;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlStr, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String str = new String(response);

                try {
                    JSONObject jsonObject = new JSONObject(str);

                    JSONArray jsonArray = new JSONArray(jsonObject.getString(Constants.omdbSearch));

                    if (jsonArray != null && jsonArray.length() > 0) {
                        String id = jsonArray.getJSONObject(0).getString(Constants.omdbImdbId);
                        String title = jsonArray.getJSONObject(0).getString(Constants.omdbTitle);
                        String year = jsonArray.getJSONObject(0).getString(Constants.omdbYear);

                        //html char decoder like & and '
                        //title = Html.fromHtml(title).toString();

                        StringBuilder sb = new StringBuilder();
                        sb.append(title);
                        if (year != null && !year.isEmpty()) {
                            sb.append(", ").append(year);
                        }

                        result.setText(sb.toString());
                        resultLink.setText(Constants.imdb + id);
                        Linkify.addLinks(resultLink, Linkify.WEB_URLS);


                    }
                } catch (Exception e) {
                    handleError(getResources().getString(R.string.nomatch));
                }

                //in any case
                luckyResult.setText(tempMovieName);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

        //wait 1 second for progress bar show
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 1000);


    }

    private void handleError(String msg) {
        result.setText(msg);
        resultLink.setText("");
    }
}
