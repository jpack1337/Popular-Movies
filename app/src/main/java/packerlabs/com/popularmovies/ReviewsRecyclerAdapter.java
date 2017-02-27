package packerlabs.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdminJpack on 12/30/16.
 */

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.TrailerHolder> {
    private ArrayList<Reviews> mReviews;
    static Movie mCurrentMovie;
    Context context;
    Reviews mCurrentReview;

    @Override
    public int getItemCount() {
        return mReviews.size();
    }



    public Movie getmCurrentMovie() {
        return mCurrentMovie;
    }

    public void setmCurrentMovie(Movie mCurrentMovie) {
        this.mCurrentMovie = mCurrentMovie;
    }

    @Override
    public void onBindViewHolder(ReviewsRecyclerAdapter.TrailerHolder holder, final int position) {
        final Reviews review = mReviews.get(position);
        context = holder.mReviewAuthor.getContext();
        mCurrentReview = review;
        holder.mReviewAuthor.setText(review.getAuthor());
        holder.mReviewContent.setText(review.getContent());

        holder.bindReview(review);

    }

    @Override
    public void onBindViewHolder(ReviewsRecyclerAdapter.TrailerHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, parent, false);
        return new TrailerHolder(inflatedView);
    }

    public ReviewsRecyclerAdapter(ArrayList<Reviews> trailers) {
        this.mReviews = trailers;
    }

    public static class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mReviewAuthor;
        private TextView mReviewContent;
        private Reviews mCurrentReview;

        public TrailerHolder(View v){
            super(v);

            mReviewAuthor = (TextView) v.findViewById(R.id.reviewAuthor);
            mReviewContent = (TextView) v.findViewById(R.id.review_content);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
        }

        void bindReview(Reviews review){
            mCurrentReview = review;
        }



      }

}

