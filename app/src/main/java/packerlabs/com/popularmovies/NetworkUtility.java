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
    ArrayList <Movie> movieTrailers = new ArrayList<>();
    OnDataCallBack mDataCallBack;


    public NetworkUtility() {
    }

    public void setCallBack(OnDataCallBack event){
        this.mDataCallBack = event;
    }

    public interface OnDataCallBack {
        public void onEvent(ArrayList<Movie> list);
    }

    /* Method to retrieve trailers by Movie ID */
    ArrayList<Movie> getTrailersForMovie(String movieId) throws  IOException {
        String URL = String.format("http://api.themoviedb.org/3/movie/%s/videos/?api_key=%s", movieId, APIKey);

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
                        if(movieTrailers.size() > 0) movieTrailers.clear();


                    }
                });

        return movieTrailers;
    }

    /* Method to retrieve all Movies */
    ArrayList<Movie> getMovies () throws IOException{
        Log.d("Get Data", "Called");
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

                                sortResultsArrayList.add(movie);
                                Log.d("Movie Added:", tempJSONMovie.get("release_date")+"");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(sortResultsArrayList.size() > 0)
                            mDataCallBack.onEvent(sortResultsArrayList);
                    }
                });
        return sortResultsArrayList;
    }


    public void getDataForCategory(String sortCategory){
        this.URL = String.format("http://api.themoviedb.org/3/movie/%s?api_key=%s", sortCategory, APIKey);
        try {
            getMovies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Movie> getSortResultsArrayList() {
        return sortResultsArrayList;
    }

    public void setSortResultsArrayList(ArrayList<Movie> sortResultsArrayList) {
        this.sortResultsArrayList = sortResultsArrayList;
    }


}
