package csc248.smirn42.collegesearch2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usersDB";
    public static final String TABLE_USERS = "users";
    public static final String KEY_ID = "_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SAT_READING = "sat_reading";
    public static final String KEY_SAT_MATH = "sat_math";
    public static final String KEY_ACT_ENGLISH = "act_english";
    public static final String KEY_ACT_MATH = "act_math";
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_USERNAME + " TEXT, "
                + KEY_PASSWORD + " TEXT, "
                + KEY_SAT_READING + " TEXT, "
                + KEY_SAT_MATH + " TEXT, "
                + KEY_ACT_ENGLISH + " TEXT, "
                + KEY_ACT_MATH + " TEXT "
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public User authenticate(String username, String password) {
        User currentUser = null;
         Cursor cursor = db.query(
                TABLE_USERS,
                null,
                KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null
        );
        if(cursor.moveToFirst()) {
            currentUser = new User();
            currentUser.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            currentUser.setUsername(username);
            currentUser.setPassword("");
            currentUser.setSatReading(cursor.getString(cursor.getColumnIndex(KEY_SAT_READING)));
            currentUser.setSatMath(cursor.getString(cursor.getColumnIndex(KEY_SAT_MATH)));
            currentUser.setActReading(cursor.getString(cursor.getColumnIndex(KEY_ACT_ENGLISH)));
            currentUser.setActMath(cursor.getString(cursor.getColumnIndex(KEY_ACT_MATH)));
        }
        cursor.close();
        return currentUser;
    }

    public Cursor getScores(String username, SQLiteDatabase db) {
        String query = "SELECT sat_reading, sat_math, act_english, act_math FROM " + TABLE_USERS + " WHERE " + KEY_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(username)});
        return cursor;
    }

    public boolean checkDupUsername(String username, SQLiteDatabase db) {
        String query = "SELECT username FROM " + TABLE_USERS + " WHERE " + KEY_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(username)});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            if(username.matches(cursor.getString(0))) {
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return false;
    }

    public class User {
        public long userId;
        public String username;
        public String password;
        public String satReading;
        public String satMath;
        public String actReading;
        public String actMath;

        public User() {
        }

        public User(String name, String password) {
            this(-1L, name, password, null, null, null, null);
        }

        public User(long id, String name, String password, String satReading, String satMath, String actReading, String actMath) {
            this.userId = id;
            this.username = name;
            this.password = password;
            this.satReading = satReading;
            this.satMath = satMath;
            this.actReading = actReading;
            this.actMath = actMath;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSatReading() {
            return satReading;
        }

        public void setSatReading(String satReading) {
            this.satReading = satReading;
        }

        public String getSatMath() {
            return satMath;
        }

        public void setSatMath(String satMath) {
            this.satMath = satMath;
        }

        public String getActReading() {
            return actReading;
        }

        public void setActReading(String actReading) {
            this.actReading = actReading;
        }

        public String getActMath() {
            return actMath;
        }

        public void setActMath(String actMath) {
            this.actMath = actMath;
        }
    }
}
