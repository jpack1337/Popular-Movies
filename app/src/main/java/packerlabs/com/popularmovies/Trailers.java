package packerlabs.com.popularmovies;

/**
 * Created by jeremypacker on 2/26/17.
 */

public class Trailers {
    String id;
    String type;
    String key;
    String name;

    public Trailers() {
    }

    public Trailers(String id, String type, String key, String name) {
        this.id = id;
        this.type = type;
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
