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
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.schedule.WebViewActivity;

public class MyInfoTabFragment extends Fragment {
    private static final String TAG = MyInfoTabFragment.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        /*TextView email = (TextView) view.findViewById(R.id.email);
        email.setText("email : " + mFirebaseAuth.getCurrentUser().getEmail());

        TextView uid = (TextView) view.findViewById(R.id.uid);
        uid.setText("uid : " + mFirebaseAuth.getCurrentUser().getUid());*/

        Log.d("getPhotoUrl", "" + mFirebaseAuth.getCurrentUser().getPhotoUrl());
        Log.d("getDisplayName", "" + mFirebaseAuth.getCurrentUser().getDisplayName());
        Log.d("getPhoneNumber", "" + mFirebaseAuth.getCurrentUser().getPhoneNumber());

        Button logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFirebaseAuth.getCurrentUser() != null) {
                    mFirebaseAuth.signOut();
                    if (getActivity() != null) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                }
            }
        });

        Button setting = view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), MyInfoActivity.class));
                }

            }
        });

        return view;
    }
}
