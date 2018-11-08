package kr.ac.korea.lecturestalk.kulecturestalk.cource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.ac.korea.lecturestalk.kulecturestalk.Adapter.PostListAdapter;
import kr.ac.korea.lecturestalk.kulecturestalk.cource.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.cource.View.EmptyRecyclerView;

public class CourceActivity extends AppCompatActivity {
    private TextView mAllTab;
    private TextView mNoticeTab;
    private TextView mMaterialsTab;
    private TextView mQnaTab;
    private TextView mEtcTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back); // 화살표 아이콘 바꾸고싶으면 설정

        if (getIntent() != null) {
            String subject = getIntent().getStringExtra("subject");
            String professor = getIntent().getStringExtra("professor");
            if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(professor)) {
                setTitle(subject + "(" + professor + ")");
            }
        }


        mAllTab = findViewById(R.id.course_tab_all);
        mNoticeTab = findViewById(R.id.course_tab_notice);
        mMaterialsTab = findViewById(R.id.course_tab_materials);
        mQnaTab = findViewById(R.id.course_tab_qna);
        mEtcTab = findViewById(R.id.course_tab_etc);

        mAllTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
                Toast.makeText(CourceActivity.this, "All Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mNoticeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mNoticeTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
                Toast.makeText(CourceActivity.this, "Notice Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mMaterialsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mMaterialsTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
                Toast.makeText(CourceActivity.this, "Materials Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mQnaTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mQnaTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
                Toast.makeText(CourceActivity.this, "Q&A Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mEtcTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mEtcTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                Toast.makeText(CourceActivity.this, "Etc Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: Initialize with 'All' tab clicked

        // Use EmptyRecyclerView
        EmptyRecyclerView recyclerView = findViewById(R.id.course_posts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(CourceActivity.this));

        // Fetch the empty view from the layout and set it on the new recycler view
        View emptyView = findViewById(R.id.course_post_empty);
        recyclerView.setEmptyView(emptyView);

        List<Post> posts = new ArrayList<>();
        // TODO: Fetch list of posts from the database

        // Test Data
//        Post p = new Post(1, "juhan", "Software Engineering", "2018-2",
//                "Hoh", "월수5", "공지", "Fuck Fuck Fuck",
//                "holy shit somebody help me...", new ArrayList<Integer>(),
//                new ArrayList<String>(), 7, System.currentTimeMillis(), 0, null);
//        posts.add(p);

        PostListAdapter adapter = new PostListAdapter(posts);
        recyclerView.setAdapter(adapter);

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
