package packerlabs.com.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MovieRecyclerAdapter mMovieRecyclerAdapter;
    private ArrayList<Movie> mMovieList = new ArrayList<Movie>();

    private NetworkUtility networkUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);
        mGridLayoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        SpacesItemDecoration spacingDecoration = new SpacesItemDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(spacingDecoration);

        mMovieRecyclerAdapter = new MovieRecyclerAdapter(mMovieList);
        mRecyclerView.setAdapter(mMovieRecyclerAdapter);

    }



    @Override
    public void onStart(){
        super.onStart();

        if(mMovieList.size() == 0)
            loadMovies();
    }

    private int getLastVisibleItemPosition() {
        return mGridLayoutManager.findLastVisibleItemPosition();
    }

    void loadMovies(){
        Log.d("Load Movies" , "Called");

        networkUtility = new NetworkUtility();
            networkUtility.setCallBack(new NetworkUtility.OnDataCallBack() {
                @Override
                public void onEvent(ArrayList <Movie> movies) {
                    swapDataFromRecyclerView(movies);
                    Log.d("CallBack" , movies.size() +" movies returned");
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.highestRatingSort:
                networkUtility.getDataForCategory("top_rated");
                swapDataFromRecyclerView(networkUtility.getSortResultsArrayList());
                return true;

            case R.id.nowPlayingSort:
                networkUtility.getDataForCategory("now_playing");
                swapDataFromRecyclerView(networkUtility.getSortResultsArrayList());
                return true;

            case R.id.popularMoviesSort:
                networkUtility.getDataForCategory("popular");
                swapDataFromRecyclerView(networkUtility.getSortResultsArrayList());
                return true;

            case R.id.upcomingMoviesSort:
                networkUtility.getDataForCategory("upcoming");
                swapDataFromRecyclerView(networkUtility.getSortResultsArrayList());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void swapDataFromRecyclerView (ArrayList <Movie> newMovies) {
        mMovieList.clear();
        mMovieList.addAll(newMovies);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMovieRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
}
