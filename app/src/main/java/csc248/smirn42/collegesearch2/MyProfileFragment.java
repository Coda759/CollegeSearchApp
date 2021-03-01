package csc248.smirn42.collegesearch2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyProfileFragment extends Fragment {

    DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    private String username;
    private TextView satReading, satMath, actReading, actMath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        satReading = myFragmentView.findViewById(R.id.my_sat_reading);
        satMath = myFragmentView.findViewById(R.id.my_sat_math);
        actReading = myFragmentView.findViewById(R.id.my_act_reading);
        actMath = myFragmentView.findViewById(R.id.my_act_math);

        Button signOut = myFragmentView.findViewById(R.id.user_sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Logged out");
                logOut();
            }
        });

        info(getActivity());

        return myFragmentView;
    }

    public void logOut() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void insertInfo(String username) {
        this.username = username;
        Log.d("name ", username);
    }

    public void info(Context context) {
        dbHelper = new DatabaseHelper(context);
        sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getScores(username, sqLiteDatabase);
        if (cursor.moveToFirst()) {
            String satReadingScore = cursor.getString(0);
            String satMathScore = cursor.getString(1);
            String actReadingScore = cursor.getString(2);
            String actMathScore = cursor.getString(3);
            cursor.close();
            if(satReadingScore.matches("0") && satMathScore.matches("0")) {
                satReading.setText("not taken");
                satMath.setText("not taken");
            } else {
                satReading.setText(satReadingScore);
                satMath.setText(satMathScore);
            }
            if(actReadingScore.matches("0") && actReadingScore.matches("0")) {
                actReading.setText("not taken");
                actMath.setText("not taken");
            } else {
                actReading.setText(actReadingScore);
                actMath.setText(actMathScore);
            }
        } else {
            Log.d("Error ", "filling out information");
        }
    }


}
