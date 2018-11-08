package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;

import kr.ac.korea.lecturestalk.kulecturestalk.schedule.ScheduleTabFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ScheduleTabFragment())
                .commit();

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
                                startActivity(new Intent(MainActivity.this, MsgTabActivity.class));
                                return true;

                            case R.id.menu_mypage:
                                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
                                return true;

                            case R.id.menu_test:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new TestTabFragment())
                                        .commit();
                                return true;
                        }
                        return false;
                    }
                });
    }
}
