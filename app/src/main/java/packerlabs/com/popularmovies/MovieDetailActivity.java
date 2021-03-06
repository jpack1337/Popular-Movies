package packerlabs.com.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.button;
import static android.R.attr.data;

public class MovieDetailActivity extends AppCompatActivity {
    Movie mCurrentMovie;
    ImageView mMovieImage;
    TextView mDescriptionText;
    TextView mReleaseDateText;
    TextView mRatingText;
    NetworkUtility networkUtility;
    ArrayList<Trailers> mTrailersArray = new ArrayList<>();
    ArrayList<Reviews> mReviewsArray = new ArrayList<>();
    ImageView mBackdropImage;
    Context mContext;
    LinearLayout mTopButtonLayouts;
    PMHelperMethods pmHelperMethods;
    RelativeLayout mDescCard, mReleaseCard;
    Button mTrailersBtn, mReviewBtn;
    ToggleButton mFavoriteBtn;
    int mTopLayoutSize = 0;
    RecyclerView mRecyvlerViewTemp;
    TrailersRecyclerAdapter trailersRecyclerAdapter;
    ReviewsRecyclerAdapter reviewsRecylerAdapter;
    LinearLayoutManager llm = new LinearLayoutManager(this);
    int viewHeight = 0;
    ScrollView mScrollView;
    boolean isMovieFavorited = false;
    MoviesDBHelper moviesDBHelper;

    boolean isOnline = false;

    boolean isTrailersMenuOpen, isReviewsMenuOpen;
    View parentLayout;
    int mTrailersHeight, mReviewsHeight;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isReviewsOpen", isReviewsMenuOpen);
        outState.putBoolean("isTrailersOpen", isTrailersMenuOpen);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mContext = this.getApplicationContext();
        mMovieImage = (ImageView) findViewById(R.id.moviePosterImageViewDetail);
        mDescriptionText = (TextView) findViewById(R.id.movieDescriptionTextView);
        mReleaseDateText = (TextView) findViewById(R.id.movieReleaseDate);
        mRatingText = (TextView) findViewById(R.id.ratingText);

        mRecyvlerViewTemp = (RecyclerView) findViewById(R.id.RecyclerViewDetail);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        //Add Dividers to Recylerview
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyvlerViewTemp.getContext(), llm.getOrientation());
        mRecyvlerViewTemp.addItemDecoration(dividerItemDecoration);

        parentLayout = findViewById(android.R.id.content);

        mDescCard = (RelativeLayout) findViewById(R.id.desc_relative);
        mReleaseCard = (RelativeLayout) findViewById(R.id.release_relative);

        mFavoriteBtn = (ToggleButton) findViewById(R.id.favoriteBtn);
        mTrailersBtn = (Button) findViewById(R.id.watch_trailer_btn);
        mReviewBtn = (Button) findViewById(R.id.see_reviews_btn);

        viewHeight = getWindow().getDecorView().getHeight();

        mBackdropImage = (ImageView) findViewById(R.id.bgimage);
        mTopButtonLayouts = (LinearLayout) findViewById(R.id.topbutton_nav);
        mTopLayoutSize = mTopButtonLayouts.getLayoutParams().height;

        pmHelperMethods = new PMHelperMethods();
        isOnline = pmHelperMethods.isNetworkAvailable(getApplicationContext());
        moviesDBHelper = new MoviesDBHelper(getApplicationContext());

        mTopButtonLayouts.setAlpha(0.80F);

        PMHelperMethods.expand(mTopButtonLayouts, 500, mTopLayoutSize);

        Bundle data = getIntent().getExtras();
        mCurrentMovie = data.getParcelable("movie");
        isReviewsMenuOpen = data.getBoolean("isReviewsOpen");
        isTrailersMenuOpen = data.getBoolean("isTrailersMenuOpen");

        mReleaseDateText.setText(mCurrentMovie.getDateString());

        getSupportActionBar().setTitle(mCurrentMovie.getTitle());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int value = Math.round(Float.valueOf(mCurrentMovie.getRating()));
        String progress = "This movie earned a " + mCurrentMovie.getRating() + " out of 10";

        Picasso.with(this)
                .load(mCurrentMovie.getPosterImageURL())
                .placeholder(R.drawable.placeholder_drawable)
                .error(R.drawable.frown)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mMovieImage.setImageBitmap(bitmap);
                        Bitmap blurredBitmap = BlurBuilder.blur(mContext, bitmap);
                        mBackdropImage.setImageBitmap(blurredBitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


        isMovieFavorited = doesMovieExists(mCurrentMovie.getMovieID());
        mFavoriteBtn.setChecked(isMovieFavorited);


        mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isMovieFavorited) {
                    getContentResolver().insert(MovieProvider.CONTENT_URI,mCurrentMovie.toContentValues());
                    isMovieFavorited = true;
                    showSnackBar(mCurrentMovie.getTitle() + " has been added to your favorites!");

                } else {
                    String selection = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = "+mCurrentMovie.getMovieID();
                    getContentResolver().delete(MovieProvider.CONTENT_URI, selection, null);
                    isMovieFavorited = false;
                    showSnackBar(mCurrentMovie.getTitle() + " has been removed from favorites!");
                }

                mFavoriteBtn.setChecked(isMovieFavorited);
            }
        });


        mTrailersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isOnline) {
                    if (mTrailersArray.size() > 0) {
                        showTrailers();
                    } else {
                        showSnackBar("No Trailers Available");
                        Log.d("Trailers", "NONE");
                    }
                } else {
                    showSnackBar("Go online to see trailers for " + mCurrentMovie.getTitle());
                }
            }

            });


        mReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOnline) {
                    if (mReviewsArray.size() > 0) {

                       showReviews();
                    } else {
                        showSnackBar("No Reviews Available");
                        Log.d("Reviews", "NONE");
                    }
                }else{
                    showSnackBar("Go online to see reviews for "+mCurrentMovie.getTitle());
                }
            }
        });

        mDescriptionText.setText(mCurrentMovie.getSynopsis());
        mRatingText.setText(progress);

        loadTrailers();
        loadReviews();

        //Check if menu's are open from orientation switch, and reload
        if (isReviewsMenuOpen) {
            showReviews();
            Log.d("Reviews Open", "True");
        }

        if(isTrailersMenuOpen)
            showTrailers();
         Log.d("Trailers Open", "True");
    }

    void showReviews(){
        getSupportActionBar().hide();
        mReviewBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mTrailersBtn.setBackgroundColor(getResources().getColor(R.color.black));
        isReviewsMenuOpen = true;
        mTopButtonLayouts.setAlpha(1.0F);
        reviewsRecylerAdapter = new ReviewsRecyclerAdapter(mReviewsArray);
        reviewsRecylerAdapter.setmCurrentMovie(mCurrentMovie);

        mRecyvlerViewTemp.setLayoutManager(llm);
        mRecyvlerViewTemp.setAdapter(reviewsRecylerAdapter);
        mRecyvlerViewTemp.setVisibility(View.VISIBLE);

        Log.d("Height", mRecyvlerViewTemp.getHeight() + "");
        //if (mRecyvlerViewTemp.getHeight() == 1)
            PMHelperMethods.expand(mRecyvlerViewTemp, 500, mScrollView.getHeight());
    }

    void showTrailers(){
        getSupportActionBar().hide();
        mTrailersBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mReviewBtn.setBackgroundColor(getResources().getColor(R.color.black));
        mTopButtonLayouts.setAlpha(1.0F);
        isTrailersMenuOpen = true;
        trailersRecyclerAdapter = new TrailersRecyclerAdapter(mTrailersArray);
        trailersRecyclerAdapter.setmCurrentMovie(mCurrentMovie);

        mRecyvlerViewTemp.setLayoutManager(llm);
        mRecyvlerViewTemp.setAdapter(trailersRecyclerAdapter);
        mRecyvlerViewTemp.setVisibility(View.VISIBLE);

        Log.d("Height", mRecyvlerViewTemp.getHeight() + "");
      //  if (mRecyvlerViewTemp.getHeight() == 1)
            PMHelperMethods.expand(mRecyvlerViewTemp, 500, mScrollView.getHeight());

    }

    boolean doesMovieExists(String id){
        Cursor c = getContentResolver().query(MovieProvider.CONTENT_URI, null, MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = " + DatabaseUtils.sqlEscapeString(id), null, null);
        if(c != null && c.getCount() == 0) {
            // not found in database
            Log.d("Exists", "Movie Not Found");
            return false;
        }else{
            Log.d("Exists", "Movie Exists");
            return true;
        }
    }


    void loadTrailers() {
        networkUtility = new NetworkUtility();
        try {
            networkUtility.getTrailersForMovie(mCurrentMovie.getMovieID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkUtility.setCallBack(new NetworkUtility.OnTrailersDataCallBack() {
            @Override
            public void onEvent(ArrayList<Trailers> trailers) {
                mTrailersArray.addAll(trailers);
                Log.d("CallBack", trailers.size() + " trailers returned");
            }
        });
    }

    void loadReviews() {
        networkUtility = new NetworkUtility();
        try {
            networkUtility.getReviewsForMovie(mCurrentMovie.getMovieID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkUtility.setCallBack(new NetworkUtility.OnReviewsDataCallBack() {
            @Override
            public void onEvent(ArrayList<Reviews> reviews) {
                mReviewsArray.addAll(reviews);
                Log.d("CallBack", reviews.size() + " reviews returned");
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return true;
    }

    //Dismiss Custom View when back button is pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (isTrailersMenuOpen || isReviewsMenuOpen) {

            if(isTrailersMenuOpen)
                mTrailersHeight = mRecyvlerViewTemp.getHeight();

            if(isReviewsMenuOpen)
                mReviewsHeight = mRecyvlerViewTemp.getHeight();

            isTrailersMenuOpen = false;
            isReviewsMenuOpen = false;

            mReviewBtn.setBackgroundColor(getResources().getColor(R.color.black));
            mTrailersBtn.setBackgroundColor(getResources().getColor(R.color.black));
            mTopButtonLayouts.setAlpha(0.66F);

            PMHelperMethods.collapse(mRecyvlerViewTemp, 500, 1);
            getSupportActionBar().show();
        } else {
            finish();
        }
    }

    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(mRecyvlerViewTemp, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
