package csc248.smirn42.collegesearch2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private DatabaseHelper dbHelper;
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
        setContentView(R.layout.activity_sign_up);

        rellay1 = findViewById(R.id.sign_up);
        rellay2 = findViewById(R.id.signup_bottom_nav);
        handler.postDelayed(runnable, 150);

        Button createUser = findViewById(R.id.btn_signup);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser();
            }
        });

        dbHelper = new DatabaseHelper(this);

        Button clearAll = findViewById(R.id.btn_clear);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDatabase();
            }
        });
    }

    public void clearDatabase() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(dbHelper.TABLE_USERS, null, null);
        System.out.println("Database successfully cleared");
    }

    public void newUser() {
        EditText usernameInput = findViewById(R.id.signup_username);
        EditText passwordInput = findViewById(R.id.signup_password);
        EditText confirmPasswordInput = findViewById(R.id.signup_confirtm_password);
        EditText satScoreReadingInput = findViewById(R.id.signup_sat_reading);
        EditText satScoreMathInput = findViewById(R.id.signup_sat_math);
        EditText actScoreReadingInput = findViewById(R.id.signup_act_reading);
        EditText actScoreMathInput = findViewById(R.id.signup_act_math);

        String usersUsername = usernameInput.getText().toString();
        String usersPassword = passwordInput.getText().toString();
        String usersConfirmPassword = confirmPasswordInput.getText().toString();
        String usersSatReadingScore = satScoreReadingInput.getText().toString();
        String usersSatMathScore = satScoreMathInput.getText().toString();
        String usersActReadingScore = actScoreReadingInput.getText().toString();
        String usersActMathScore = actScoreMathInput.getText().toString();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        if(usersUsername.matches("") || usersPassword.matches("") || usersConfirmPassword.matches("") || usersSatReadingScore.matches("") || usersSatMathScore.matches("") || usersActReadingScore.matches("") || usersActMathScore.matches("")) {
            Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show();
        } else if(dbHelper.checkDupUsername(usersUsername, database)) {
            Toast.makeText(this, "this username is unavailable", Toast.LENGTH_SHORT).show();
        } else {
            int satReadingScoreValue = Integer.parseInt(usersSatReadingScore);
            int satMathScoreValue = Integer.parseInt(usersSatMathScore);
            double actReadingScoreValue = Double.parseDouble(usersActReadingScore);
            double actMathScoreValue = Double.parseDouble(usersActMathScore);
            if(usersPassword.equals(usersConfirmPassword) && actReadingScoreValue < 36.1 && actMathScoreValue < 36.1 && satReadingScoreValue < 801 && satMathScoreValue < 801) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(dbHelper.KEY_USERNAME, usersUsername);
                contentValues.put(dbHelper.KEY_PASSWORD, usersPassword);
                contentValues.put(dbHelper.KEY_SAT_READING, usersSatReadingScore);
                contentValues.put(dbHelper.KEY_SAT_MATH, usersSatMathScore);
                contentValues.put(dbHelper.KEY_ACT_ENGLISH, usersActReadingScore);
                contentValues.put(dbHelper.KEY_ACT_MATH, usersActMathScore);

                database.insert(dbHelper.TABLE_USERS, null, contentValues);
                dbHelper.close();

                Toast.makeText(this, "User " + usersUsername + " created", Toast.LENGTH_SHORT).show();
                usernameInput.getText().clear();
                passwordInput.getText().clear();
                confirmPasswordInput.getText().clear();
                satScoreReadingInput.getText().clear();
                satScoreMathInput.getText().clear();
                actScoreReadingInput.getText().clear();
                actScoreMathInput.getText().clear();

            } else {
                Toast.makeText(this, "make sure password matches and test scores are within range", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
