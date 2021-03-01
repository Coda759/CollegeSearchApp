package csc248.smirn42.collegesearch2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    DatabaseCollege dbCollege;
    DatabaseHelper dbHelper;
    RequestQueue requestQueue;
    private Context context;
    DatabaseHelper.User current_user = null;
    private LinearLayout rellay1;
    private RelativeLayout rellay2;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rellay1 = findViewById(R.id.login);
        rellay2 = findViewById(R.id.login_bottom_nav);

        handler.postDelayed(runnable, 2000);

        Button signup = findViewById(R.id.btn_go_to_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });

        Button readAll = findViewById(R.id.btn_read);
        readAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAllUsers();
            }
        });

        dbHelper = new DatabaseHelper(this);
        dbCollege = new DatabaseCollege(this);

        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        context = this.getApplicationContext();
        requestQueue = Volley.newRequestQueue(this);

        Button doStuff = findViewById(R.id.btn_load_colleges);
        doStuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Please wait ~20 seconds for download", Toast.LENGTH_LONG).show();
                dbCollege.getTotalPages(requestQueue, context);
            }
        });

    }

    public void loginUser() {
        EditText usernameInput = findViewById(R.id.login_username);
        EditText passwordInput = findViewById(R.id.login_password);
        String loginUsername = usernameInput.getText().toString();
        String loginPassword = passwordInput.getText().toString();
        if(loginUsername.matches("") || loginPassword.matches("")) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if((current_user = dbHelper.authenticate(loginUsername, loginPassword)) != null) {

            //open mainUserActivity
            usernameInput.getText().clear();
            passwordInput.getText().clear();
            Intent intent = new Intent(this, UserPage.class);
            intent.putExtra("username", loginUsername);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void openSignup() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void readAllUsers() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(dbHelper.TABLE_USERS, null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(dbHelper.KEY_ID);
            int usernameIndex = cursor.getColumnIndex(dbHelper.KEY_USERNAME);
            int passwordIndex = cursor.getColumnIndex(dbHelper.KEY_PASSWORD);
            int satReadingIndex = cursor.getColumnIndex(dbHelper.KEY_SAT_READING);
            int satMathIndex = cursor.getColumnIndex(dbHelper.KEY_SAT_MATH);
            int actEnglishIndex = cursor.getColumnIndex(dbHelper.KEY_ACT_ENGLISH);
            int actMathIndex = cursor.getColumnIndex(dbHelper.KEY_ACT_MATH);

            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", username = " + cursor.getString(usernameIndex) +
                        ", password = " + cursor.getString(passwordIndex) +
                        ", sat_reading = " + cursor.getInt(satReadingIndex) +
                        ", sat_math = " + cursor.getInt(satMathIndex) +
                        ", act_english = " + cursor.getDouble(actEnglishIndex) +
                        ", act_math = " + cursor.getDouble(actMathIndex));
            } while(cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
            cursor.close();
            dbHelper.close();
        }
    }
}
