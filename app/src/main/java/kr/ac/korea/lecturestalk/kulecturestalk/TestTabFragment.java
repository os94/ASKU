package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Point;
import kr.ac.korea.lecturestalk.kulecturestalk.schedule.WebViewActivity;

public class TestTabFragment extends Fragment implements GetPointListener {
    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testtab, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        TextView email = (TextView) view.findViewById(R.id.email);
        email.setText("email : " + mFirebaseAuth.getCurrentUser().getEmail());

        TextView uid = (TextView) view.findViewById(R.id.uid);
        uid.setText("uid : " + mFirebaseAuth.getCurrentUser().getUid());

        Log.d("getPhotoUrl", "" + mFirebaseAuth.getCurrentUser().getPhotoUrl());
        Log.d("getDisplayName", "" + mFirebaseAuth.getCurrentUser().getDisplayName());
        Log.d("getPhoneNumber", "" + mFirebaseAuth.getCurrentUser().getPhoneNumber());

        Button logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFirebaseAuth.getCurrentUser() != null) {
                    mFirebaseAuth.signOut();
                    if (getActivity() != null) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                }
            }
        });

        Button LaunchWebView = (Button) view.findViewById(R.id.LaunchWebView);
        LaunchWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), WebViewActivity.class));
                }
            }
        });

        Button getBtn = (Button)view.findViewById(R.id.get);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Point pointModel = new Point();
                pointModel.getPoint(new GetPointListener() {
                    @Override
                    public int onPointLoaded(int point) {
                        // do your work here
                        Log.d("@@@@@point", String.valueOf(point));
                        Toast.makeText(getActivity(), "현재 포인트는 "+point+"점입니다.", Toast.LENGTH_SHORT).show();
                        return point;
                    }
                });
            }
        });

        Button addBtn = (Button)view.findViewById(R.id.add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Point pointModel = new Point();
                pointModel.getPoint(new GetPointListener() {
                    @Override
                    public int onPointLoaded(int point) {
                        pointModel.addPoint(point+10);
                        Toast.makeText(getActivity(), "10포인트를 획득했습니다!", Toast.LENGTH_SHORT).show();
                        return 0;
                    }
                });
            }
        });

        return view;
    }

    @Override
    public int onPointLoaded(int point) {
        return 0;
    }
}
