package kr.ac.korea.lecturestalk.kulecturestalk.cource;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.Adapter.PostListAdapter;
import kr.ac.korea.lecturestalk.kulecturestalk.PostListFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.WritePostFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.cource.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.cource.View.EmptyRecyclerView;

public class CourceActivity extends AppCompatActivity {


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
