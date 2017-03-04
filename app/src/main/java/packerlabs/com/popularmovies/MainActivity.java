package packerlabs.com.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.ArrayList;

import static java.lang.reflect.Modifier.STATIC;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MovieRecyclerAdapter mMovieRecyclerAdapter;
    private ArrayList<Movie> mMovieList = new ArrayList<Movie>();
    private CoordinatorLayout mCoordinatorLayout;
    private NetworkUtility networkUtility;
    private int TASK_LOADER_ID = 1337;
    private TextView mEmptyText;
    private PMHelperMethods pmHelperMethods;


    private boolean isDeviceOnline = true;
    private boolean isUsingContentProvider = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);
        mEmptyText = (TextView) findViewById(R.id.empty_view);

        pmHelperMethods = new PMHelperMethods();

        mGridLayoutManager = new GridLayoutManager(this, 3);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                mRecyclerView.setHasFixedSize(true);
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        }        mRecyclerView.setHasFixedSize(true);

        SpacesItemDecoration spacingDecoration = new SpacesItemDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(spacingDecoration);

        setCorrectAdapter(pmHelperMethods.isNetworkAvailable(getApplicationContext()));

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }


    void setCorrectAdapter(boolean isDeviceOnline){
        this.isDeviceOnline = isDeviceOnline;

        if(isDeviceOnline){
            setDefaultAdapter();
            loadMovies(NetworkUtility.ENDPOINTS.TOP_RATED);
            showSnackBar(getResources().getString(R.string.menu_now_highest_rated_text));
            mEmptyText.setVisibility(View.INVISIBLE);
        }else{
            loadSavedMovies();
            showSnackBar(getResources().getString(R.string.no_internet));
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_favorited_title));
        }
    }

    void loadSavedMovies(){
        MoviesDBHelper handler = new MoviesDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor movieCursor = db.rawQuery("SELECT  * FROM movies", null);

        MovieCursorAdapter movieCursorAdapter = new MovieCursorAdapter(this, movieCursor);
        mRecyclerView.setAdapter(movieCursorAdapter);

        Log.d("Cursor", movieCursor.getCount()+"");

        if(movieCursor.getCount() == 0) {
            if(isDeviceOnline)
                mEmptyText.setText(R.string.no_saved_movies_text);
            else
                mEmptyText.setText(R.string.no_saved_movies_no_internet);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyText.setVisibility(View.VISIBLE);
        }

        isUsingContentProvider = true;
    }

    void setDefaultAdapter(){
        mMovieRecyclerAdapter = new MovieRecyclerAdapter(mMovieList);
        mRecyclerView.setAdapter(mMovieRecyclerAdapter);
    }


    void loadMovies(String category){
        Log.d("Load Movies" , "Called");

        if(isUsingContentProvider)
            setDefaultAdapter();

        if(mRecyclerView.getVisibility() == View.INVISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.INVISIBLE);
        }

        networkUtility = new NetworkUtility();
        networkUtility.getDataForCategory(category);
        networkUtility.setCallBack(new NetworkUtility.OnMovieDataCallBack() {
                @Override
                public void onEvent(ArrayList <Movie> movies) {
                    swapDataFromRecyclerView(movies);
                    Log.d("CallBack" , movies.size() +" movies returned");
                    isUsingContentProvider = false;
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
                if(isDeviceOnline) {
                    loadMovies(NetworkUtility.ENDPOINTS.TOP_RATED);
                    showSnackBar(getResources().getString(R.string.menu_now_highest_rated_text));
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_toprated_title));
                }else{
                    showSnackBar(getResources().getString(R.string.no_internet));
                }
                return true;

            case R.id.nowPlayingSort:
                if(isDeviceOnline) {
                    loadMovies(NetworkUtility.ENDPOINTS.NOW_PLAYING);
                    showSnackBar(getResources().getString(R.string.menu_now_playing_text));
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_nowplaying_title));
                }else{
                    showSnackBar(getResources().getString(R.string.no_internet));
                }
                return true;

            case R.id.popularMoviesSort:
                if(isDeviceOnline) {
                    loadMovies(NetworkUtility.ENDPOINTS.MOST_POPULAR);
                    showSnackBar(getResources().getString(R.string.menu_popular_text));
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_mostpopular_title));
                }else{
                    showSnackBar(getResources().getString(R.string.no_internet));
                }

                return true;

            case R.id.upcomingMoviesSort:
                if(isDeviceOnline) {
                    loadMovies(NetworkUtility.ENDPOINTS.UPCOMING);
                    showSnackBar(getResources().getString(R.string.menu_upcoming_text));
                    getSupportActionBar().setTitle(getResources().getString(R.string.menu_upcoming_title));
                }else{
                    showSnackBar(getResources().getString(R.string.no_internet));
                }
                return true;

            case R.id.favoritedMoviesSort:
                loadSavedMovies();
                showSnackBar(getResources().getString(R.string.menu_favorited_text));
                getSupportActionBar().setTitle(getResources().getString(R.string.menu_favorited_title));
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData = null;


            @Override
            protected void onStartLoading() {
                if(mTaskData != null){
                    deliverResult(mTaskData);
                }else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try{
                    return getContentResolver().query(MovieProvider.CONTENT_URI, null, null, null, null);

                }catch (Exception e){
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




}
