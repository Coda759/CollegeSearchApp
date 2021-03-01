package csc248.smirn42.collegesearch2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DatabaseCollege extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "collegesDB";
    public static final String TABLE_COLLEGES = "colleges";
    public static final String KEY_ID = "_id";
    public static final String KEY_COLLEGE_ID = "college_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CITY = "city";
    public static final String KEY_ZIP = "zip";
    public static final String KEY_STATE = "state";
    public static final String KEY_WEB_ADDRESS = "college_web";
    public static final String KEY_ADMISSIONS_WEB = "admissions_web";
    public static final String KEY_COST_ATTENDANCE_IN = "attendance_in";
    public static final String KEY_COST_ATTENDANCE_OUT = "attendance_out";
    public static final String KEY_SAT_25_READING = "sat_25_reading";
    public static final String KEY_SAT_25_MATH = "sat_25_math";
    public static final String KEY_SAT_75_READING = "sat_75_reading";
    public static final String KEY_SAT_75_MATH = "sat_75_math";
    public static final String KEY_ACT_25_ENGLISH = "act_25_english";
    public static final String KEY_ACT_25_MATH = "act_25_math";
    public static final String KEY_ACT_75_ENGLISH = "act_75_english";
    public static final String KEY_ACT_75_MATH = "act_75_math";
    private RequestQueue mRequestQueue;
    private int maxPageNumber;
    SQLiteDatabase db;
    private final String URL = "https://api.data.gov/ed/collegescorecard/v1/schools.json?api_key=6HIcCE5oP0vCR48Q6wic5A4PsAb34FrK3LDdhsRI&per_page=100&_fields=id,school.name,school.city,school.zip,school.state,school.school_url,school.price_calculator_url,latest.cost.tuition.in_state,latest.cost.tuition.out_of_state,latest.admissions.sat_scores.25th_percentile.critical_reading,latest.admissions.sat_scores.75th_percentile.critical_reading,latest.admissions.sat_scores.25th_percentile.math,latest.admissions.sat_scores.75th_percentile.math,latest.admissions.act_scores.25th_percentile.english,latest.admissions.act_scores.75th_percentile.english,latest.admissions.act_scores.25th_percentile.math,latest.admissions.act_scores.75th_percentile.math";
    private String urlPages = URL;

    public DatabaseCollege(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_COLLEGES
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_COLLEGE_ID + " TEXT, "
                + KEY_NAME + " TEXT, "
                + KEY_CITY + " TEXT, "
                + KEY_ZIP + " TEXT, "
                + KEY_STATE + " TEXT, "
                + KEY_WEB_ADDRESS + " TEXT, "
                + KEY_ADMISSIONS_WEB + " TEXT, "
                + KEY_COST_ATTENDANCE_IN + " TEXT, "
                + KEY_COST_ATTENDANCE_OUT + " TEXT, "
                + KEY_SAT_25_READING + " TEXT, "
                + KEY_SAT_25_MATH + " TEXT, "
                + KEY_SAT_75_READING + " TEXT, "
                + KEY_SAT_75_MATH + " TEXT, "
                + KEY_ACT_25_ENGLISH + " TEXT, "
                + KEY_ACT_25_MATH + " TEXT, "
                + KEY_ACT_75_ENGLISH + " TEXT, "
                + KEY_ACT_75_MATH + " TEXT "
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLEGES);

        onCreate(db);
    }

    public void parseJSON(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            ContentValues contentValues = new ContentValues();

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String collegeId = hit.getString("id");
                                String collegeName = hit.getString("school.name");
                                String city = hit.getString("school.city");
                                String zip = hit.getString("school.zip");
                                String state = hit.getString("school.state");
                                String webAddress = hit.getString("school.school_url");
                                String admissionsWeb = hit.getString("school.price_calculator_url");
                                String tuitionIn = hit.getString("latest.cost.tuition.in_state");
                                String tuitionOut = hit.getString("latest.cost.tuition.out_of_state");
                                String sat25Reading = hit.getString("latest.admissions.sat_scores.25th_percentile.critical_reading");
                                String sat75Reading = hit.getString("latest.admissions.sat_scores.75th_percentile.critical_reading");
                                String sat25Math = hit.getString("latest.admissions.sat_scores.25th_percentile.math");
                                String sat75Math = hit.getString("latest.admissions.sat_scores.75th_percentile.math");
                                String act25Eng = hit.getString("latest.admissions.act_scores.25th_percentile.english");
                                String act75Eng = hit.getString("latest.admissions.act_scores.75th_percentile.english");
                                String act25Math = hit.getString("latest.admissions.act_scores.25th_percentile.math");
                                String act75Math = hit.getString("latest.admissions.act_scores.75th_percentile.math");
                                //System.out.println(i);
                                contentValues.put(KEY_COLLEGE_ID, collegeId);
                                contentValues.put(KEY_NAME, collegeName);
                                contentValues.put(KEY_CITY, city);
                                contentValues.put(KEY_ZIP, zip);
                                contentValues.put(KEY_STATE, state);
                                contentValues.put(KEY_WEB_ADDRESS, webAddress);
                                contentValues.put(KEY_ADMISSIONS_WEB, admissionsWeb);
                                contentValues.put(KEY_COST_ATTENDANCE_IN, tuitionIn);
                                contentValues.put(KEY_COST_ATTENDANCE_OUT, tuitionOut);
                                contentValues.put(KEY_SAT_25_READING, sat25Reading);
                                contentValues.put(KEY_SAT_75_READING, sat75Reading);
                                contentValues.put(KEY_SAT_25_MATH, sat25Math);
                                contentValues.put(KEY_SAT_75_MATH, sat75Math);
                                contentValues.put(KEY_ACT_25_ENGLISH, act25Eng);
                                contentValues.put(KEY_ACT_75_ENGLISH, act75Eng);
                                contentValues.put(KEY_ACT_25_MATH, act25Math);
                                contentValues.put(KEY_ACT_75_MATH, act75Math);

                                db.insert(TABLE_COLLEGES, null, contentValues);
//                                Long result = db.insert(TABLE_COLLEGES, null, contentValues);
//
//                                if(result == -1) {
//                                    Log.d("result ", "bad");
//                                } Log.d("result ", "good");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    public void getTotalPages(RequestQueue requestQueue, final Context context) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_COLLEGES, null, null);
        System.out.println("Database Colleges successfully cleared");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject metadata = response.getJSONObject("metadata");
                    int i = metadata.getInt("total");
                    double d = (i / 100);
                    i = (int) (d + 1);
                    maxPageNumber = i;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request1) {

                mRequestQueue = Volley.newRequestQueue(context);
                System.out.println("total pages: " + maxPageNumber);
                for(int i = 0; i <= maxPageNumber; i++) {
                    Log.d("Page #" , "" +i);
                    parseJSON(urlPages+"&page=" + i);
                }
            }
        });
        System.out.println("done");
    }

    public int getNumOfColleges() {
        int numColleges = 0;
        String query = "SELECT COUNT(college_id) FROM " + TABLE_COLLEGES;
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            numColleges = cursor.getInt(0);
        }
        cursor.close();
        return numColleges;
    }

    public ArrayList<CollegeItem> getCollegeData() {
        ArrayList<CollegeItem> collegeItemList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_COLLEGES; // + " WHERE " + KEY_ID + "=?";
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            collegeItemList.add(new CollegeItem(cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        return collegeItemList;
    }

    public Cursor getCollegeDetails(String ID) {
        String query = "SELECT name, college_id, city, state, zip, college_web, attendance_in, attendance_out, sat_25_reading, sat_75_reading, sat_25_math, sat_75_math, act_25_english, act_75_english, act_25_math, act_75_math FROM " + TABLE_COLLEGES + " WHERE " + KEY_COLLEGE_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(ID)});
        return cursor;
    }
}
