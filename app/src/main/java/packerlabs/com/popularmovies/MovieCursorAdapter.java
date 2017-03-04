package packerlabs.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdminJpack on 12/30/16.
 */

public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.MovieHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;


    public MovieCursorAdapter(Context mContext, Cursor cursor) {
        this.mContext = mContext;
        this.mCursor = cursor;
    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item_frame_layout, parent, false);

        return new MovieHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID);
        int plotIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_PLOT);
        int urlIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_IMAGE_URL);
        int ratingIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING);
        int titleIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE);
        int releaseIndex = mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE);

        mCursor.moveToPosition(position); // get to the right location in the cursor
        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String index = mCursor.getString(idIndex);
        String plot = mCursor.getString(plotIndex);
        String url = mCursor.getString(urlIndex);
        String title = mCursor.getString(titleIndex);
        String rating = mCursor.getString(ratingIndex);
        String release = mCursor.getString(releaseIndex);

        Movie tempMovie = new Movie(title, url, plot, rating, release , index);

        //Set values
        holder.bindMovie(tempMovie);
        Context context = holder.mMovieImage.getContext();
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder_drawable)
                .error(R.drawable.frown)
                .into(holder.mMovieImage);
    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    // Inner class for creating ViewHolders
    class MovieHolder extends RecyclerView.ViewHolder {

        private ImageView mMovieImage;
        private Movie mCurrentMovie;

        public MovieHolder(View v){
            super(v);

            mMovieImage = (ImageView) v.findViewById(R.id.mTrailerImage);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context mContext = v.getContext();
                    navigateToDetailActivity(mContext);
                    Log.d("Hit", "Clicked" + mCurrentMovie.getTitle());

                }
            });
        }


        void bindMovie(Movie movie){
            mCurrentMovie = movie;
        }

        void navigateToDetailActivity(Context mContext){
            Intent intent = new Intent(mContext, MovieDetailActivity.class);
            intent.putExtra("movie", new Movie(mCurrentMovie.getTitle(), mCurrentMovie.getPosterImageLink(), mCurrentMovie.getSynopsis(),  mCurrentMovie.getRating(), mCurrentMovie.getDateString(), mCurrentMovie.getMovieID()));
            mContext.startActivity(intent);
        }
    }
}