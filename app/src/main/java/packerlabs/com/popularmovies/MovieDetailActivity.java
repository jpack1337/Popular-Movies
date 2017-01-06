package packerlabs.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {
    Movie mCurrentMovie;
    ImageView mMovieImage;
    RatingBar mRatingBar;
    TextView mDescriptionText;
    TextView mReleaseDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail2);

        mMovieImage = (ImageView) findViewById(R.id.moviePosterImageViewDetail);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mDescriptionText = (TextView) findViewById(R.id.movieDescriptionTextView);
        mReleaseDateText = (TextView) findViewById(R.id.movieReleaseDate);

        Intent intent = getIntent();
        String title = intent.getStringExtra("original_title");
        String poster = intent.getStringExtra("poster");
        String description = intent.getStringExtra("description");
        String rating = intent.getStringExtra("user_rating");
        Date date = new Date(intent.getStringExtra("release_date"));


        mCurrentMovie = new Movie(title, poster, description, rating, date);

        getSupportActionBar().setTitle(mCurrentMovie.getTitle());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int value = Math.round(Float.valueOf(mCurrentMovie.getRating()));
        Picasso.with(this).load(mCurrentMovie.getPosterImageURL()).into(mMovieImage);

        mRatingBar.setMax(10);
        Log.d("Value", value+"");
        Log.d("Value", mCurrentMovie.getPosterImageLink());
        mRatingBar.setProgress(value);
        mDescriptionText.setText(mCurrentMovie.getSynopsis());
        mReleaseDateText.setText(mCurrentMovie.getDate().toString());
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
