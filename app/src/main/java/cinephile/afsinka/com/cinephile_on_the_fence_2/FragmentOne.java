package cinephile.afsinka.com.cinephile_on_the_fence_2;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Pattern;

import cinephile.afsinka.com.cinephile_on_the_fence_2.model.Film;
import cinephile.afsinka.com.cinephile_on_the_fence_2.util.Constants;
import cinephile.afsinka.com.cinephile_on_the_fence_2.util.Utils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by afsin on 19.2.2016.
 */
public class FragmentOne extends Fragment {

    private final static String path = Environment.getDataDirectory().getAbsolutePath();
    private SeekBar seekBar;
    private TextView seekBarValue;
    private RatingBar ratingBar;
    private TextView ratingBarValue;
    private Button button;
    private TextView result;
    private TextView resultLink;
    private RelativeLayout progressBar;
    private String tempFilmName;
    private String tempFilmYear;

    public FragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getFromOmdbApi(Constants.initialFilm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_one, container, false);

        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        seekBarValue = (TextView) v.findViewById(R.id.seekBarValue);
        seekBar.setOnSeekBarChangeListener(seekBarListener());

        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        ratingBarValue = (TextView) v.findViewById(R.id.ratingBarValue);
        ratingBar.setOnRatingBarChangeListener(ratingBarListener());

        button = (Button) v.findViewById(R.id.findByRatingButton);
        button.setOnClickListener(buttonListener());

        result = (TextView) v.findViewById(R.id.result);
        resultLink = (TextView) v.findViewById(R.id.resultLink);

        progressBar = (RelativeLayout) v.findViewById(R.id.loadingPanel);
        progressBar.setVisibility(View.INVISIBLE);

        return v;
    }

    private SeekBar.OnSeekBarChangeListener seekBarListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = normalizeProgress(progress);
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private RatingBar.OnRatingBarChangeListener ratingBarListener() {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBarValue.setText(String.valueOf(rating));
            }
        };
    }

    private View.OnClickListener buttonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                findMovieByRateAndVote();
            }
        };
    }

    private void findMovieByRateAndVote() {
        float rate = ratingBar.getRating();
        int votes = seekBar.getProgress();
        find(rate, normalizeProgress(votes));
    }


    //not use because of legal issues
/*    @Deprecated
    private void getFromImdbApi(String movieName) {
        String query = movieName.toLowerCase().trim();
        query = query.replace(" ", "+");
        query = query.replace(",", "");
        query = query.replace("\"", "");

        String urlStr = "http://www.imdb.com/xml/find?json=1&tt=1&q=" + query;

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

                if (!headers[1].getValue().equals("HTTPDaemon")) {
                    //302 redirect but status code 200
                    Log.e("debug", "opps: " + Arrays.toString(headers));
                    handleError();
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(str);
                    String jsonParam = null;

                    if (jsonObject.has("title_popular")) {
                        jsonParam = jsonObject.getString("title_popular");
                    } else if (jsonObject.has("title_exact")) {
                        jsonParam = jsonObject.getString("title_exact");
                    } else if (jsonObject.has("title_approx")) {
                        jsonParam = jsonObject.getString("title_approx");
                    } else if (jsonObject.has("title_substring")) {
                        jsonParam = jsonObject.getString("title_substring");
                    } else {
                        Log.e("error", "unexpected JSON: " + str);
                        handleError();
                        return;
                    }

                    JSONArray jsonArray = new JSONArray(jsonParam);

                    if (jsonArray != null && jsonArray.length() > 0) {
                        String id = jsonArray.getJSONObject(0).getString("id");
                        String title = jsonArray.getJSONObject(0).getString("title");
                        String desc = jsonArray.getJSONObject(0).getString("description");
                        String year = "";
                        if (desc.contains(",")) {
                            year = desc.substring(0, desc.indexOf(","));
                        }

                        //html char decoder like & and '
                        title = Html.fromHtml(title).toString();

                        StringBuilder sb = new StringBuilder();
                        sb.append(title);
                        if (year != null && !year.isEmpty()) {
                            sb.append(", ").append(year);
                        }

                        result.setText(sb.toString());
                        resultLink.setText("http://www.imdb.com/title/" + id);
                        Linkify.addLinks(resultLink, Linkify.WEB_URLS);
                    }
                } catch (Exception e) {
                    handleError();
                    return;
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("error", "onFailure " + statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }*/

    /**
     * http://www.omdbapi.com/
     */
    private void getFromOmdbApi(String movieName) {
        String query = movieName.replace("\"", "").trim();
        String filmName = query.substring(0, query.indexOf("(")).trim().replace(" ", "+");
        String filmYear = query.substring(query.indexOf("(") + 1, query.length() - 1);

        //keep for case of not found on OMDb
        tempFilmName = filmName.replace("+", " ");
        tempFilmYear = filmYear;

        String urlStr = Constants.omdbUrl + filmName + Constants.omdbUrlYearParam + filmYear;

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

                checkResult();

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

    }

    private void checkResult() {
        if (resultLink.getText().toString().isEmpty()) {
            //not found on imdb page
            result.setText(tempFilmName + ", " + tempFilmYear);

            Pattern pattern = Pattern.compile(".*");
            resultLink.setText(R.string.imdbClick);
            Linkify.addLinks(resultLink, pattern, Constants.imdbFind + tempFilmName + Constants.imdbFindAll);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void handleError(String msg) {
        result.setText(msg);
        resultLink.setText("");
    }

    private void find(double rate, int votes) {
        progressBar.setVisibility(View.VISIBLE);

        Film film = Utils.getFilm(((GlobalApp) this.getActivity().getApplication()).getAllFilms(), rate, votes);

        if (film == null) {
            handleError(getResources().getString(R.string.opps));
            return;
        }

        String movieName = film.getName();
        getFromOmdbApi(movieName);
    }

    private int normalizeProgress(int progress) {
        int seekBarStep = 1000;
        progress = progress / seekBarStep;
        progress = progress * seekBarStep;
        return progress;
    }


}
