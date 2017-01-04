package packerlabs.com.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetail extends AppCompatActivity {
    Movie mCurrentMovie;
    Toolbar mToolBar;
    TextView mMovieDescription;
    CollapsingToolbarLayout mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mMovieDescription = (TextView) findViewById(R.id.movieDescription);
        mLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent intent = getIntent();
        String title = intent.getStringExtra("original_title");
        String poster = intent.getStringExtra("poster");
        String description = intent.getStringExtra("description");
        String rating = intent.getStringExtra("user_rating");
        Date date = new Date(intent.getStringExtra("release_date"));

        mCurrentMovie = new Movie(title, poster, description, rating, date);

        Picasso.with(this).load(mCurrentMovie.getPosterImageLink()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                mLayout.setBackground(drawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mLayout.setBackground(placeHolderDrawable);
            }
        });

        mToolBar.setTitle(mCurrentMovie.getTitle());
        mMovieDescription.setText(mCurrentMovie.getSynopsis());
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
