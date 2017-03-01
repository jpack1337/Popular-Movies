package packerlabs.com.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by jeremypacker on 2/27/17.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";
        MoviesDBHelper mDbHelper;

        private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + " (" +
                    MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
                    MoviesContract.MovieEntry.COLUMN_IMAGE_URL + " TEXT," +
                    MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT," +
                    MoviesContract.MovieEntry.COLUMN_PLOT + " TEXT," +
                    MoviesContract.MovieEntry.COLUMN_RATING + " TEXT," +
                    MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT)";

        private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME;

        public MoviesDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    void deleteMovie(String id){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { id };
        // Issue SQL statement.
        db.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        Log.d("Movie Removed", "Yes");
    }

    void saveMovie(Movie mCurrentMovie){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, mCurrentMovie.getTitle());
        values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, mCurrentMovie.getMovieID());
        values.put(MoviesContract.MovieEntry.COLUMN_IMAGE_URL, mCurrentMovie.getPosterImageURL());
        values.put(MoviesContract.MovieEntry.COLUMN_PLOT, mCurrentMovie.getSynopsis());
        values.put(MoviesContract.MovieEntry.COLUMN_RATING, mCurrentMovie.getRating());
        values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, mCurrentMovie.getDateString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
        Log.d("Movie Added", newRowId+"");
    }

    boolean isMovieFavorited(String id){
        boolean isFavorited = false;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID
        };

        // Filter results WHERE "id" = currentMoviesID'
        String selection = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(
                MoviesContract.MovieEntry.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0)
            isFavorited = false;
        else
            isFavorited = true;

        cursor.close();
        Log.d("Movie", isFavorited+"");
        return isFavorited;
    }

    void loadMovieById(String id){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
                MoviesContract.MovieEntry.COLUMN_IMAGE_URL,
                MoviesContract.MovieEntry.COLUMN_PLOT,
                MoviesContract.MovieEntry.COLUMN_RATING,
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(
                MoviesContract.MovieEntry.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null // The sort order
        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_MOVIE_ID));
            itemIds.add(itemId);
        }

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            //cursor is empty
        }
        cursor.close();

    }


}

