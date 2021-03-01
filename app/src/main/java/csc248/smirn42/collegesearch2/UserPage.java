package csc248.smirn42.collegesearch2;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserPage extends AppCompatActivity {

    public static String username;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        username = getIntent().getExtras().getString("username");
        welcome = findViewById(R.id.welcome_text);
        welcome.setText("Welcome, " + username);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fr);
        ((MyProfileFragment) fragment).insertInfo(username);
        ((MyProfileFragment) fragment).info(this);
    }

    public void flipTab(View v) {
        Fragment fragment = null;

        switch(v.getId()) {
            case R.id.btn_profile:
                welcome.setVisibility(v.VISIBLE);
                fragment = new MyProfileFragment();
                ((MyProfileFragment) fragment).insertInfo(username);
                break;
//            case R.id.btn_edit:
//                //another fragment here later
//                break;
            case R.id.btn_colleges:
                welcome.setVisibility(v.GONE);
                fragment = new CollegeFragment();
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fr, fragment);
        ft.commit();
    }
}
