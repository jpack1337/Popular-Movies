package packerlabs.com.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.net.URI;

/**
 * Created by jeremypacker on 2/27/17.
 */

public class MovieProvider extends ContentProvider {

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String AUTHORITY = "packerlabs.com.popularmovies.MovieProvider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/movies");

    static {
        sUriMatcher.addURI("packerlabs.com.popularmovies.MovieProvider", "movies", 1);
        sUriMatcher.addURI("packerlabs.com.popularmovies.MovieProvider", "movies/#", 2);
    }

    private MoviesDBHelper mOpenHelper;

    // Defines the database name
    private static final String DBNAME = "movies";

    // Holds the database object
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());        // the application context
        db = mOpenHelper.getWritableDatabase();
        return false;
    }

    // Implements ContentProvider.query()
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        switch (sUriMatcher.match(uri)) {
            case 1:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;

            default:
                break;
            // If the URI is not recognized, you should do some error handling here.
        }
        // call the code to actually do the query
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnURI = null;
        long newRowId = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(CONTENT_URI + "/" + newRowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
