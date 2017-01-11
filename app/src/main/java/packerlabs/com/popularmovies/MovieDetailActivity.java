package packerlabs.com.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {
    Movie mCurrentMovie;
    ImageView mMovieImage;
    TextView mDescriptionText;
    TextView mReleaseDateText;
    TextView mRatingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieImage = (ImageView) findViewById(R.id.moviePosterImageViewDetail);
        mDescriptionText = (TextView) findViewById(R.id.movieDescriptionTextView);
        mReleaseDateText = (TextView) findViewById(R.id.movieReleaseDate);
        mRatingText = (TextView) findViewById(R.id.ratingText);

        Intent intent = getIntent();
        String title = intent.getStringExtra("original_title");
        String poster = intent.getStringExtra("poster");
        String description = intent.getStringExtra("description");
        String rating = intent.getStringExtra("user_rating");

        Date date = new Date(intent.getStringExtra("release_date"));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        String displayDate = formatter.format(date);

        mCurrentMovie = new Movie(title, poster, description, rating, date);

        getSupportActionBar().setTitle(mCurrentMovie.getTitle());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int value = Math.round(Float.valueOf(mCurrentMovie.getRating()));
        String progress = "("+mCurrentMovie.getRating() + " / 10)";

        Log.d("PosterLink", mCurrentMovie.getPosterImageLink());
        Log.d("PosterLink", mCurrentMovie.getPosterImageURL());

        Picasso.with(this)
                    .load(mCurrentMovie.getPosterImageURL())
                    .placeholder(R.drawable.placeholder_drawable)
                    .error(R.drawable.frown)
                    .into(mMovieImage);


        mDescriptionText.setText(mCurrentMovie.getSynopsis());
        mReleaseDateText.setText(displayDate);
        mRatingText.setText(progress);
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
