package packerlabs.com.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AdminJpack on 12/30/16.
 */

public class Movie implements Parcelable {
    String title;
    String posterImageLink;
    String synopsis;
    String rating;
    String dateString;

    public Movie(String title, String posterImageLink, String synopsis, String rating, String date) {
        this.title = title;
        this.posterImageLink = posterImageLink;
        this.synopsis = synopsis;
        this.rating = rating;
        this.dateString = date;
    }

    public Movie(){
    }

    public Movie(Parcel inputParcel) {
        String[] data = new String[5];
        inputParcel.readStringArray(data);
        this.title = data[0];
        this.posterImageLink = data[1];
        this.synopsis = data[2];
        this.rating = data[3];
        this.dateString = data[4];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterImageLink() {
        return posterImageLink;
    }

    public String getPosterImageURL() {
        return "http://image.tmdb.org/t/p/w185/" + posterImageLink;
    }

    public void setPosterImageLink(String posterImageLink) {
        this.posterImageLink = posterImageLink;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    /* Parceable Implementation */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.title, this.posterImageLink, this.synopsis, this.rating, this.dateString});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
};

