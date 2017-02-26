package packerlabs.com.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by AdminJpack on 1/1/17.
 */

public class NetworkUtility {
    OkHttpClient client = new OkHttpClient();

    //TODO Add Your Own API Key
    //www.themodiedb.org
    String APIKey = "ea4c651a09961d8d50e27ff0b17ed370";
    String URL = String.format("http://api.themoviedb.org/3/movie/%s?api_key=%s", "upcoming", APIKey);


    ArrayList <Movie> sortResultsArrayList = new ArrayList<>();
    ArrayList <Trailers> trailersArrayList = new ArrayList<>();
    ArrayList <Reviews> reviewsArrayList = new ArrayList<>();

    OnMovieDataCallBack mDataCallBack;
    OnReviewsDataCallBack mReviewCallback;
    OnTrailersDataCallBack mTrailersCallback;

    public NetworkUtility() {
    }

    /* Callbacks */


    public void setCallBack(OnMovieDataCallBack event){
        this.mDataCallBack = event;
    }

    public void setCallBack(OnTrailersDataCallBack event){
        this.mTrailersCallback = event;
    }

    public void setCallBack(OnReviewsDataCallBack event){
        this.mReviewCallback = event;
    }


    public interface OnMovieDataCallBack {
        public void onEvent(ArrayList<Movie> list);
    }

    public interface OnReviewsDataCallBack {
        public void onEvent(ArrayList<Reviews> list);
    }

    public interface OnTrailersDataCallBack {
        public void onEvent(ArrayList<Trailers> list);
    }

    /* Method to retrieve reviews by Movie ID */
    ArrayList<Trailers> getReviewsForMovie(String movieId) throws  IOException {
        String URL = String.format("http://api.themoviedb.org/3/movie/%s/reviews?api_key=%s", movieId, APIKey);
        Log.d("Review URL", URL);
        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (trailersArrayList.size() > 0)
                            trailersArrayList.clear();

                        if (response != null && response.body() != null) {

                            String res = response.body().string();
                            try {
                                JSONObject bodyResponse = new JSONObject(res);
                                JSONArray sortResults = bodyResponse.getJSONArray("results");

                                for (int i = 0; i < sortResults.length(); i++) {
                                    JSONObject tempJSONMovie = sortResults.getJSONObject(i);
                                    Reviews reviews = new Reviews();

                                    reviews.setId(tempJSONMovie.getString("id"));
                                    reviews.setAuthor(tempJSONMovie.getString("author"));
                                    reviews.setContent(tempJSONMovie.getString("content"));
                                   // reviews.setRating(tempJSONMovie.getString("rating"));

                                    reviewsArrayList.add(reviews);
                                    Log.d("Review Added:", tempJSONMovie.get("id") + "");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if(reviewsArrayList.size() > 0)
                            mReviewCallback.onEvent(reviewsArrayList);
                    }
                });

        return trailersArrayList;
    }

    /* Method to retrieve trailers by Movie ID */
    ArrayList<Trailers> getTrailersForMovie(String movieId) throws  IOException {
        String URL = String.format("http://api.themoviedb.org/3/movie/%s/videos?api_key=%s", movieId, APIKey);
        Log.d("Trailer URL", URL);
        Request request = new Request.Builder()
                .url(URL)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (trailersArrayList.size() > 0)
                            trailersArrayList.clear();

                        if (response != null && response.body() != null) {

                            String res = response.body().string();
                            try {
                                JSONObject bodyResponse = new JSONObject(res);
                                JSONArray sortResults = bodyResponse.getJSONArray("results");

                                for (int i = 0; i < sortResults.length(); i++) {
                                    JSONObject tempJSONMovie = sortResults.getJSONObject(i);
                                    Trailers trailer = new Trailers();

                                    trailer.setName(tempJSONMovie.getString("name"));
                                    trailer.setKey(tempJSONMovie.getString("key"));
                                    trailer.setId(tempJSONMovie.getString("id"));
                                    trailer.setType(tempJSONMovie.getString("type"));

                                    trailersArrayList.add(trailer);
                                    Log.d("Trailer Added:", tempJSONMovie.get("name") + "");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if(trailersArrayList.size() > 0)
                            mTrailersCallback.onEvent(trailersArrayList);
                    }
                });

        return trailersArrayList;
    }

    /* Method to retrieve all Movies */
    ArrayList<Movie> getMovies () throws IOException{
        Log.d("Get Data", "Called");
        Log.d("Get Data", this.URL);

        Request request = new Request.Builder()
                .url(this.URL)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if(sortResultsArrayList.size() > 0) sortResultsArrayList.clear();

                        if(response != null && response.body() != null) {
                            String res = response.body().string();
                            try {
                                JSONObject bodyResponse = new JSONObject(res);
                                JSONArray sortResults = bodyResponse.getJSONArray("results");

                                for (int i = 0; i < sortResults.length(); i++) {
                                    JSONObject tempJSONMovie = sortResults.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.setTitle(tempJSONMovie.getString("original_title"));
                                    movie.setPosterImageLink(tempJSONMovie.getString("poster_path"));
                                    movie.setSynopsis(tempJSONMovie.getString("overview"));
                                    movie.setRating(tempJSONMovie.getString("vote_average"));
                                    movie.setDateString(tempJSONMovie.getString("release_date"));
                                    movie.setMovieID(tempJSONMovie.getString("id"));

                                    sortResultsArrayList.add(movie);
                                    Log.d("Movie Added:", tempJSONMovie.get("id") + "");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(sortResultsArrayList.size() > 0)
                            mDataCallBack.onEvent(sortResultsArrayList);
                    }
                });
        return sortResultsArrayList;
    }

    /* Method to retrieve all Movies sorted by categories */
    public void getDataForCategory(String sortCategory){
        this.URL = String.format("http://api.themoviedb.org/3/movie/%s?api_key=%s", sortCategory, APIKey);
        try {
            getMovies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
