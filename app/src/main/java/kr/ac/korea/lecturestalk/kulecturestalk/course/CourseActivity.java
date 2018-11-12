package kr.ac.korea.lecturestalk.kulecturestalk.course;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;


import kr.ac.korea.lecturestalk.kulecturestalk.PostListFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.R;

public class CourseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back); // 화살표 아이콘 바꾸고싶으면 설정

        if (getIntent() != null) {
            // todo 이거 세개 조합해서 게시판 key로 쓰세요
            String subject = getIntent().getStringExtra("subject");
            String professor = getIntent().getStringExtra("professor");
            String room = getIntent().getStringExtra("room");

            if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(professor)) {
                setTitle(subject + "(" + professor + ")");
            }
        }

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.post_container, new PostListFragment());
        fragmentTransaction.commit();*/
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.post_container, new PostListFragment())
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 상단 액션바의 '<-' 버튼 눌렀을 때.
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
