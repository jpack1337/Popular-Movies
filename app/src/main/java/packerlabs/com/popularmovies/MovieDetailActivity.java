package packerlabs.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {
    Movie mCurrentMovie;
    ImageView mMovieImage;
    TextView mDescriptionText;
    TextView mReleaseDateText;
    TextView mRatingText;
    NetworkUtility networkUtility;
    ArrayList<Movie> trailers = new ArrayList<>();
    ImageView mBackdropImage;
    Context mContext;
    LinearLayout mTopButtonLayouts;
    PMHelperMethods pmHelperMethods;
    RelativeLayout mDescCard, mReleaseCard;
    int mTopLayoutSize = 0, mDescCardSize, mReleaseCardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mContext = this.getApplicationContext();
        mMovieImage = (ImageView) findViewById(R.id.moviePosterImageViewDetail);
        mDescriptionText = (TextView) findViewById(R.id.movieDescriptionTextView);
        mReleaseDateText = (TextView) findViewById(R.id.movieReleaseDate);
        mRatingText = (TextView) findViewById(R.id.ratingText);

        mDescCard = (RelativeLayout) findViewById(R.id.desc_relative);
        mReleaseCard = (RelativeLayout) findViewById(R.id.release_relative);

        mBackdropImage = (ImageView) findViewById(R.id.bgimage);
        mTopButtonLayouts = (LinearLayout) findViewById(R.id.topbutton_nav);

        mTopLayoutSize = mTopButtonLayouts.getLayoutParams().height;
        mDescCardSize = mDescCard.getLayoutParams().height;
        mReleaseCardSize = mReleaseCard.getLayoutParams().height;
        pmHelperMethods = new PMHelperMethods();

        pmHelperMethods.expand(mTopButtonLayouts, 500, mTopLayoutSize);
     //   pmHelperMethods.expand(mDescCard, 500, mDescCardSize);
       // pmHelperMethods.expand(mReleaseCard, 500, mReleaseCardSize);

        BlurBuilder blurBuilder = new BlurBuilder();
        Bundle data = getIntent().getExtras();
        mCurrentMovie = (Movie) data.getParcelable("movie");

        mReleaseDateText.setText(mCurrentMovie.getDateString());

        getSupportActionBar().setTitle(mCurrentMovie.getTitle());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int value = Math.round(Float.valueOf(mCurrentMovie.getRating()));
        String progress = "This movie earned a "+mCurrentMovie.getRating() + " out of 10";

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

        mDescriptionText.setText(mCurrentMovie.getSynopsis());
        mRatingText.setText(progress);

        loadTrailers();
        loadReviews();
    }

    void loadTrailers(){
        networkUtility = new NetworkUtility();
        try {
            networkUtility.getTrailersForMovie(mCurrentMovie.getMovieID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkUtility.setCallBack(new NetworkUtility.OnTrailersDataCallBack() {
            @Override
            public void onEvent(ArrayList<Trailers> trailers) {
                Log.d("CallBack" , trailers.size() +" trailers returned");
            }
        });
    }

    void loadReviews(){
        networkUtility = new NetworkUtility();
        try {
            networkUtility.getReviewsForMovie(mCurrentMovie.getMovieID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkUtility.setCallBack(new NetworkUtility.OnReviewsDataCallBack() {
            @Override
            public void onEvent(ArrayList<Reviews> reviews) {
                Log.d("CallBack" , reviews.size() +" reviews returned");
            }
        });
    }

    void swapDataFromRecyclerView (ArrayList <Movie> newMovies) {
        trailers.clear();
        trailers.addAll(newMovies);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mMovieRecyclerAdapter.notifyDataSetChanged();
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
}
