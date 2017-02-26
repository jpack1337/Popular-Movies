package packerlabs.com.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MovieRecyclerAdapter mMovieRecyclerAdapter;
    private ArrayList<Movie> mMovieList = new ArrayList<Movie>();
    private CoordinatorLayout mCoordinatorLayout;
    private NetworkUtility networkUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);
        mGridLayoutManager = new GridLayoutManager(this, 3);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        }        mRecyclerView.setHasFixedSize(true);

        SpacesItemDecoration spacingDecoration = new SpacesItemDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(spacingDecoration);

        mMovieRecyclerAdapter = new MovieRecyclerAdapter(mMovieList);
        mRecyclerView.setAdapter(mMovieRecyclerAdapter);
        loadMovies();
    }


    void loadMovies(){
        Log.d("Load Movies" , "Called");

        networkUtility = new NetworkUtility();
        networkUtility.getDataForCategory("now_playing");
        networkUtility.setCallBack(new NetworkUtility.OnMovieDataCallBack() {
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
                showSnackBar("Movies sorted by Top Rated");
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_toprated_title));
                return true;

            case R.id.nowPlayingSort:
                networkUtility.getDataForCategory("now_playing");
                showSnackBar("Movies sorted by Now Playing");
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_nowplaying_title));
                return true;

            case R.id.popularMoviesSort:
                networkUtility.getDataForCategory("popular");
                showSnackBar("Movies sorted by Most Popular");
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_mostpopular_title));

                return true;

            case R.id.upcomingMoviesSort:
                networkUtility.getDataForCategory("upcoming");
                showSnackBar("Movies sorted by Upcoming");
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_upcoming_title));
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

    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(mRecyclerView, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
