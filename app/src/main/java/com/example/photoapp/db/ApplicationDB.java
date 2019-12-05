package com.example.photoapp.db;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Application database utility
 */
public class ApplicationDB extends SQLiteOpenHelper {

    private static final String TAG = "com.example.photoapp.db.ApplicationDB";

    private static final String DATABASE_NAME = "photo_app_db";

    private static final String ALBUMS = "Album";
    private static final String PHOTOS = "Photo";
    private static final String TAG_TYPES = "Tag_Types";
    private static final String TAGS = "Tag";

    private SQLiteDatabase readableDb;

    public ApplicationDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        final String createAlbumSql = "CREATE TABLE " +
                                       ALBUMS+ "(" +
                                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                        "name TEXT," +
                                        "CONSTRAINT unique_name UNIQUE (name)" +
                                       ");";

        final String createPhotoSql = "CREATE TABLE " +
                                       PHOTOS+ "(" +
                                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                        "album_id INTEGER," +
                                        "path TEXT," +
                                        "caption TEXT," +
                                        "CONSTRAINT fk_photos_album FOREIGN KEY(album_id) REFERENCES Albums(id) ON DELETE CASCADE" +
                                      ");";

        final String createTagTypesSql = "CREATE TABLE " +
                                         TAG_TYPES +"(" +
                                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                            "name VARCHAR(30)" +
                                         ");";

        final String createTagsSql = "CREATE TABLE " +
                                      TAGS +"(" +
                                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                        "type_id INTEGER," +
                                        "value TEXT," +
                                        "CONSTRAINT fk_tags_tag_type FOREIGN KEY(id) REFERENCES Tag_Type(id) ON DELETE CASCADE" +
                                      ");";

        db.execSQL(createAlbumSql);
        db.execSQL(createPhotoSql);
        db.execSQL(createTagTypesSql);
        db.execSQL(createTagsSql);

        final String insertTagTypesSql = "INSERT INTO Tag_Types (name) VALUES ('Location'), ('Person');";
        db.execSQL(insertTagTypesSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ALBUMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PHOTOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TAGS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TAG_TYPES);

        onCreate(sqLiteDatabase);
    }

    /**
     * Album CRUD
     */
    public boolean createAlbum(String albumName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", albumName);

        Log.d(TAG, "Inserting " + albumName + " into Albums");
        long result = db.insert(ALBUMS, null, values);

        db.close();
        return result == -1 ? false : true;
    }

    public void editAlbum(int id, String updatedName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", updatedName);

        Log.d(TAG, "Updating " + updatedName + " where id = " + id);

        db.update(ALBUMS, values, "id = ?", new String [] {String.valueOf(id)});
        db.close();
    }

    public void deleteAlbum(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(TAG, "Deleting album where id = " + id);

        db.delete(ALBUMS, "id = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    public Cursor readAllAlbums() {
        readableDb = this.getReadableDatabase();
        final String selectAllSql = "SELECT * FROM " + ALBUMS;
        Cursor result = readableDb.rawQuery(selectAllSql, null);

        return result;
    }

    /**
     * Photo CRUD
     */


    /**
     * Public db utilities
     */
    public void closeReadable() {
        if(readableDb != null) {
            readableDb.close();
        }
    }


}
