package packerlabs.com.popularmovies;

import android.provider.BaseColumns;

/**
 * Created by jeremypacker on 2/27/17.
 */

public class MoviesContract {

        private MoviesContract() {}

        /* Inner class that defines the table contents */
        public static class MovieEntry implements BaseColumns {
            public static final String TABLE_NAME = "movies";

            public static final String COLUMN_IMAGE_URL = "url";
            public static final String COLUMN_URI = "uri";
            public static final String COLUMN_PLOT = "plot";
            public static final String COLUMN_RATING = "rating";
            public static final String COLUMN_RELEASE_DATE = "release";
            public static final String COLUMN_MOVIE_ID = "id";
            public static final String COLUMN_MOVIE_TITLE = "title";

        }
    }


