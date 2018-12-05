package kr.ac.korea.lecturestalk.kulecturestalk.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import kr.ac.korea.lecturestalk.kulecturestalk.LoginActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.MainActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.MyPostListFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.PostListFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.schedule.WebViewActivity;

public class MyInfoTabFragment extends AppCompatActivity {
    private static final String TAG = MyInfoTabFragment.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_myinfo);

        mFirebaseAuth = FirebaseAuth.getInstance();

        String myName = mFirebaseAuth.getCurrentUser().getDisplayName();
        Log.d("getPhotoUrl", "" + mFirebaseAuth.getCurrentUser().getPhotoUrl());
        Log.d("getDisplayName", "" + mFirebaseAuth.getCurrentUser().getDisplayName());
        Log.d("getPhoneNumber", "" + mFirebaseAuth.getCurrentUser().getPhoneNumber());

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFirebaseAuth.getCurrentUser() != null) {
                    mFirebaseAuth.signOut();

                    startActivity(new Intent(MyInfoTabFragment.this, LoginActivity.class));
                    MyInfoTabFragment.this.finish();
                }
            }
        });

        Button setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(MyInfoTabFragment.this, MyInfoActivity.class));
            }
        });

        MyPostListFragment fragment = new MyPostListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("author", myName);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.post_container, fragment, "fragmentTag").commit();
    }
}
