package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import kr.ac.korea.lecturestalk.kulecturestalk.View.Fragment.CourseFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();

        TextView email = (TextView) findViewById(R.id.email);
        email.setText("email : " + mFirebaseAuth.getCurrentUser().getEmail());

        TextView uid = (TextView) findViewById(R.id.uid);
        uid.setText("uid : " + mFirebaseAuth.getCurrentUser().getUid());

        Log.d("getPhotoUrl", "" + mFirebaseAuth.getCurrentUser().getPhotoUrl());
        Log.d("getDisplayName", "" + mFirebaseAuth.getCurrentUser().getDisplayName());
        Log.d("getPhoneNumber", "" + mFirebaseAuth.getCurrentUser().getPhoneNumber());

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFirebaseAuth.getCurrentUser() != null) {
                    mFirebaseAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        Button LaunchWebView = (Button) findViewById(R.id.LaunchWebView);
        LaunchWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, WebViewActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.menu_lectures:
//                                Toast.makeText(MainActivity.this, "수강과목", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new CourseFragment())
                                        .commit();
                                return true;

                            case R.id.menu_messages:
                                Toast.makeText(MainActivity.this, "쪽지함", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, MsgTabActivity.class));
                                return true;

                            case R.id.menu_mypage:
                                Toast.makeText(MainActivity.this, "마이페이지", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
                                return true;
                        }
                        return false;
                    }
                });
    }
}
