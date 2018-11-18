package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;

import kr.ac.korea.lecturestalk.kulecturestalk.message.MsgTabFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.my.MyInfoTabFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.schedule.ScheduleTabFragment;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    public static String userid; //다른 클래스들에서 import해서 쓸수있도록. (현재 사용자 uid)
    public static String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userid = mFirebaseAuth.getCurrentUser().getUid();
        userEmail = mFirebaseAuth.getCurrentUser().getEmail();

        Intent intent = getIntent();
        if (intent != null) {
            String initTabName = intent.getStringExtra("initTab");
            initTab(initTabName);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_lectures:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ScheduleTabFragment())
                                        .commit();
                                return true;

                            case R.id.menu_messages:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new MsgTabFragment())
                                        .commit();
                                return true;

                            case R.id.menu_mypage:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new MyInfoTabFragment())
                                        .commit();
                                return true;

/*                            case R.id.menu_test:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new TestTabFragment())
                                        .commit();
                                return true;*/
                        }
                        return false;
                    }
                });
    }

    private void initTab(String tabName) {
        Fragment initFragment = null;
        if ("message".equals(tabName)) {
            initFragment =  new MsgTabFragment();
        } else if ("my_page".equals(tabName)) {
            initFragment =  new MyInfoTabFragment();
        } else {
            initFragment =  new ScheduleTabFragment(); // default is ScheduleFragment
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, initFragment)
                .commit();
    }
}
