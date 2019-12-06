package com.example.photoapp.db;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.photoapp.models.Photo;

/**
 * Application database utility
 */
public class ApplicationDB extends SQLiteOpenHelper {

    private static final String TAG = "com.example.photoapp.db.ApplicationDB";

    private static final String DATABASE_NAME = "photo_app_db";

    private static final String ALBUMS = "Album";
    private static final String PHOTOS = "Photo";
    private static final String TAG_TYPES = "Tag_Type";
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
                                        "CONSTRAINT fk_photos_album FOREIGN KEY(album_id) REFERENCES Album(id) ON DELETE CASCADE" +
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
                                        "photo_id INTEGER," +
                                        "value TEXT," +
                                        "CONSTRAINT fk_tags_tag_type FOREIGN KEY(type_id) REFERENCES Tag_Type(id) ON DELETE CASCADE," +
                                        "CONSTRAINT fk_tags_photo FOREIGN KEY(photo_id) REFERENCES Photo(id) ON DELETE CASCADE" +
                                      ");";

        db.execSQL(createAlbumSql);
        db.execSQL(createPhotoSql);
        db.execSQL(createTagTypesSql);
        db.execSQL(createTagsSql);

        final String insertTagTypesSql = "INSERT INTO Tag_Type (name) VALUES ('Location'), ('Person');";
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

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON");
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

    public boolean createPhoto(int albumId, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("album_id", albumId);
        values.put("path", path);
        values.put("caption", "your caption");

        Log.d(TAG, "Inserting photo " + path + " into album" + albumId);

        long result = db.insert(PHOTOS, null, values);

        db.close();
        return result == -1 ? false: true;
    }

    public void editPhoto(int id, String updatedCaption) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("caption", updatedCaption);

        db.update(PHOTOS, values, "id = ?", new String [] {String.valueOf(id)});
        db.close();
    }

    public void deletePhoto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PHOTOS, "id = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    public Cursor readPhotosByAlbum(int albumId) {
        readableDb = this.getReadableDatabase();
        final String selectByAlbumSql = "SELECT * FROM " + PHOTOS + " WHERE album_id = " + albumId;

        Cursor result = readableDb.rawQuery(selectByAlbumSql, null);

        return result;
    }

    public Cursor readPhotos() {
        readableDb = this.getReadableDatabase();
        final String selectAllSql = "SELECT * FROM " + PHOTOS;

        Cursor result = readableDb.rawQuery(selectAllSql, null);

        return result;
    }

    /**
     * Tag CRUD
     */
    public void createTag(int photoId, String value, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        final String insertTagSql = "INSERT INTO " + TAGS +
                                    "(" +
                                        "type_id," +
                                        "photo_id," +
                                        "value" +
                                    ")" +
                                    "VALUES" +
                                    "(" +
                                        "(SELECT id FROM " + TAG_TYPES + " WHERE name = " + "'" + type + "'" + ")," +
                                         photoId + "," +
                                        "'" + value + "'" +
                                    ");";

        Log.d(TAG, insertTagSql);
        db.execSQL(insertTagSql);
        db.close();
    }

    public void deleteTag(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TAGS, "id = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    public Cursor readTagsByPhoto(int photoId) {
        readableDb = this.getReadableDatabase();
        final String selectPhotoTagsSql = "SELECT t.id, tt.name, t.value FROM " + TAGS + " AS t JOIN " + TAG_TYPES + " AS tt ON t.type_id = tt.id WHERE t.photo_id = " + photoId;

        Cursor data = readableDb.rawQuery(selectPhotoTagsSql, null);
        return data;
    }


    /**
     * Public db utilities
     */
    public void closeReadable() {
        if(readableDb != null) {
            readableDb.close();
        }
    }


}
