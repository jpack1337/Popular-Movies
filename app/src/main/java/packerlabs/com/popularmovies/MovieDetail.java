package packerlabs.com.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetail extends AppCompatActivity {
    Movie mCurrentMovie;
    Toolbar mToolBar;
    TextView mMovieDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mMovieDescription = (TextView) findViewById(R.id.movieDescription);

        Intent intent = getIntent();
        String title = intent.getStringExtra("original_title");
        String poster = intent.getStringExtra("poster");
        String description = intent.getStringExtra("description");
        String rating = intent.getStringExtra("user_rating");
        Date date = new Date(intent.getStringExtra("release_date"));

        mCurrentMovie = new Movie(title, poster, description, rating, date);

        mToolBar.setTitle(mCurrentMovie.getTitle());
        mMovieDescription.setText(mCurrentMovie.getSynopsis());
        setSupportActionBar(mToolBar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, mCurrentMovie.getTitle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
