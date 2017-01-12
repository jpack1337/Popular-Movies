package packerlabs.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdminJpack on 12/30/16.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieHolder> {
    private ArrayList<Movie> mMovies;

    @Override
    public int getItemCount() {
        return mMovies.size();
    }



    @Override
    public void onBindViewHolder(MovieRecyclerAdapter.MovieHolder holder, int position) {
        Movie mMovie = mMovies.get(position);
        Context context = holder.mMovieImage.getContext();
        Picasso.with(context)
                .load(mMovie.getPosterImageURL())
                .placeholder(R.drawable.placeholder_drawable)
                .error(R.drawable.frown)
                .into(holder.mMovieImage);

        holder.bindMovie(mMovie);
    }

    @Override
    public void onBindViewHolder(MovieRecyclerAdapter.MovieHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_frame_layout, parent, false);
        return new MovieHolder(inflatedView);
    }

    public MovieRecyclerAdapter(ArrayList<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    public static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mMovieImage;
        private Movie mCurrentMovie;

        public MovieHolder(View v){
            super(v);

            mMovieImage = (ImageView) v.findViewById(R.id.mMoviePosterImage);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            //TODO Set onClick Listener to Segue to detail View
            Log.d("Hit", "Clicked" + mCurrentMovie.getTitle());
            Context mContext = v.getContext();
            navigateToDetailActivity(mContext);

        }

        void bindMovie(Movie movie){
            mCurrentMovie = movie;
        }

        void navigateToDetailActivity(Context mContext){
            Intent intent = new Intent(mContext, MovieDetailActivity.class);
            intent.putExtra("movie", new Movie(mCurrentMovie.getTitle(), mCurrentMovie.getPosterImageLink(), mCurrentMovie.getSynopsis(),  mCurrentMovie.getRating(), mCurrentMovie.getDateString()));
            mContext.startActivity(intent);
        }
    }
}
