package packerlabs.com.popularmovies;

/**
 * Created by jeremypacker on 2/26/17.
 */

public class Reviews {
    String content;
    String rating;
    String author;
    String id;

    public Reviews() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reviews(String content, String rating, String author, String id) {
        this.content = content;
        this.rating = rating;
        this.author = author;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
