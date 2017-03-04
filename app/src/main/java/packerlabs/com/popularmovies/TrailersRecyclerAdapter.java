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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdminJpack on 12/30/16.
 */

public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.TrailerHolder> {
    private ArrayList<Trailers> mTrailers;
    private String mCurrentMovieImageURL;
    static Movie mCurrentMovie;
    Context context;
    @Override
    public int getItemCount() {
        return mTrailers.size();
    }



    public Movie getmCurrentMovie() {
        return mCurrentMovie;
    }

    public void setmCurrentMovie(Movie mCurrentMovie) {
        TrailersRecyclerAdapter.mCurrentMovie = mCurrentMovie;
    }

    @Override
    public void onBindViewHolder(TrailersRecyclerAdapter.TrailerHolder holder, final int position) {
        final Trailers trailer = mTrailers.get(position);
        context = holder.mTrailerImage.getContext();

        Picasso.with(context)
                .load(getmCurrentMovie().getPosterImageURL())
               .placeholder(R.drawable.placeholder_drawable)
                .error(R.drawable.frown)
                .into(holder.mTrailerImage);


        holder.mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Share Button", "Hit at position "+ position);
                shareTrailer(trailer, v.getContext());
            }
        });

        holder.mTrailerTitle.setText(trailer.getName());
        holder.bindTrailer(trailer);
    }

    @Override
    public void onBindViewHolder(TrailersRecyclerAdapter.TrailerHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_layout, parent, false);
        return new TrailerHolder(inflatedView);
    }

    public TrailersRecyclerAdapter(ArrayList<Trailers> trailers) {
        this.mTrailers = trailers;
    }

    public static class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mTrailerImage;
        private TextView mTrailerTitle;
        private Button mShareButton;
        private Trailers mCurrentTrailer;

        public TrailerHolder(View v){
            super(v);

            mTrailerImage = (ImageView) v.findViewById(R.id.mTrailerImage);
            mTrailerTitle = (TextView) v.findViewById(R.id.trailerNameTxt);
            mShareButton = (Button) v.findViewById(R.id.shareBtn);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            openTrailerIntent(mCurrentTrailer, v.getContext());
        }

        void bindTrailer(Trailers trailer){
            mCurrentTrailer = trailer;
        }

        void openTrailerIntent(Trailers trailer, Context mContext){
            String videoLink = String.format("vnd.youtube:%s", trailer.getKey());
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink)));
            Log.d("youtube", videoLink);

        }

      }
    static void shareTrailer(Trailers trailer, Context context){
        String videoLink = String.format("https://www.youtube.com/watch?v=%s", trailer.getKey());

        StringBuilder shareString = new StringBuilder(140);
        shareString.append("Watch this movie trailer for ");
        shareString.append(mCurrentMovie.getTitle());
        shareString.append(" via PopularMovies: "+videoLink);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Watch this movie trailer!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareString.toString());
        context.startActivity(Intent.createChooser(sharingIntent, "Share this trailer with your friends..."));
    }

}

