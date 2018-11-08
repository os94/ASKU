package kr.ac.korea.lecturestalk.kulecturestalk.schedule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import kr.ac.korea.lecturestalk.kulecturestalk.LoginActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.cource.CourceActivity;

public class ScheduleTabFragment extends Fragment {
    private static final String TAG = ScheduleTabFragment.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private static final int REQUEST_DODE_WEBVIEW = 162873;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_tab, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        TextView email = (TextView) view.findViewById(R.id.email);
        email.setText("email : " + mFirebaseAuth.getCurrentUser().getEmail());

        TextView uid = (TextView) view.findViewById(R.id.uid);
        uid.setText("uid : " + mFirebaseAuth.getCurrentUser().getUid());

        TextView scheduleText = view.findViewById(R.id.schedule_list);
        SharedPreferences setting = getActivity().getSharedPreferences("setting", 0);
        scheduleText.setText(setting.getString("scheduleList", ""));

        Button takeScheduleButton = (Button) view.findViewById(R.id.take_schedule_button);
        takeScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
//                    startActivityForResult(new Intent(getActivity(), WebViewActivity.class), REQUEST_DODE_WEBVIEW);
                    startActivity(new Intent(getActivity(), WebViewActivity.class));
                }
            }
        });

        LinearLayout courceList = view.findViewById(R.id.cource_listview);

        View testCource = inflater.inflate(R.layout.schedule_list_view, container, false);
        testCource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CourceActivity.class));
            }
        });
        courceList.addView(testCource);

        return view;
    }


/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DODE_WEBVIEW:
                    String extraText = data.getStringExtra("scheduleList");
                    Log.d(TAG, "schedule list : " + extraText);
                    if (!TextUtils.isEmpty(extraText) && getView() != null) {
                        TextView scheduleText = getView().findViewById(R.id.schedule_list);
                        scheduleText.setText(extraText);
                    }
                    break;
            }
        }
    }*/
}
