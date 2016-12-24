package bett.blib.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bett on 7/11/16.
 */
public class DatabaseUtils extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SettingsManager";

    // Contacts table name
    private static final String TABLE_SETTINGS = "settings";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_KEY = "key";
    private static final String KEY_VALUE = "value";

    public DatabaseUtils(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_KEY + " TEXT,"
                + KEY_VALUE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);

        // Create tables again
        onCreate(db);
    }

    /**
     * @param key
     * @return jsonString
     */
    public String getByKey(String key) {
        String result = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_SETTINGS, new String[] { KEY_ID,
                            KEY_KEY, KEY_VALUE }, KEY_KEY + "=?",
                    new String[] { key }, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getColumnCount() > 0) {
                    result = cursor.getString(2);
                }
            } else {
                result = "";
            }
            db.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param key
     * @param jsonString
     */
    public void setByKey(String key, String jsonString) {
        deleteByKey(key);
        SQLiteDatabase db = this.getWritableDatabase();
        if (StringUtils.isEmpty(getByKey(key))) {
            ContentValues values = new ContentValues();
            values.put(KEY_KEY, key);
            values.put(KEY_VALUE, jsonString);

            // Inserting Row
            db.insert(TABLE_SETTINGS, null, values);
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_KEY, key);
            values.put(KEY_VALUE, jsonString);

            // updating row
            db.update(TABLE_SETTINGS, values, KEY_KEY + " = ?",
                    new String[] { jsonString });
        }
        db.close();
    }

    public void deleteByKey(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SETTINGS, KEY_KEY + " = ?",
                new String[]{key});
        db.close();
    }
}
