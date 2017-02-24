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
    int mTopLayoutSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mContext = this.getApplicationContext();
        mMovieImage = (ImageView) findViewById(R.id.moviePosterImageViewDetail);
        mDescriptionText = (TextView) findViewById(R.id.movieDescriptionTextView);
        mReleaseDateText = (TextView) findViewById(R.id.movieReleaseDate);
        mRatingText = (TextView) findViewById(R.id.ratingText);
        mBackdropImage = (ImageView) findViewById(R.id.bgimage);
        mTopButtonLayouts = (LinearLayout) findViewById(R.id.topbutton_nav);
        mTopLayoutSize = mTopButtonLayouts.getLayoutParams().height;
        pmHelperMethods = new PMHelperMethods();

        pmHelperMethods.collapse(mTopButtonLayouts, 500, 0);
        pmHelperMethods.expand(mTopButtonLayouts, 1000, mTopLayoutSize);
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

        mMovieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + "/sdcard/test.jpg"), "image/*");
                startActivity(intent);
            }
        });

        loadTrailers();
    }

    void loadTrailers(){
        Log.d("Load Trailers" , "Called");

        networkUtility = new NetworkUtility();
        try {
            networkUtility.getTrailersForMovie(mCurrentMovie.getMovieID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkUtility.setCallBack(new NetworkUtility.OnDataCallBack() {
            @Override
            public void onEvent(ArrayList<Movie> movies) {
                swapDataFromRecyclerView(movies);
                Log.d("CallBack" , movies.size() +" trailers returned");
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
