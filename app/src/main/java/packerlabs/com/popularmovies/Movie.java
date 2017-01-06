package packerlabs.com.popularmovies;

import java.util.Date;

/**
 * Created by AdminJpack on 12/30/16.
 */

public class Movie {
    String title;
    String posterImageLink;
    String synopsis;
    String rating;
    Date date;
    String posterImageURL;

    public Movie(String title, String posterImageLink, String synopsis, String rating, Date date) {
        this.title = title;
        this.posterImageLink = posterImageLink;
        this.synopsis = synopsis;
        this.rating = rating;
        this.date = date;
    }

    public Movie() {
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

    public String getPosterImageURL () {
        return "http://image.tmdb.org/t/p/w185/"+ posterImageLink;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
