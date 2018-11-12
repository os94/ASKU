package kr.ac.korea.lecturestalk.kulecturestalk.schedule;

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

import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.course.CourseActivity;

public class ScheduleTabFragment extends Fragment {
    private static final String TAG = ScheduleTabFragment.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private static final int REQUEST_DODE_WEBVIEW = 162873;
    private LinearLayout mCourceList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_tab, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        TextView email = (TextView) view.findViewById(R.id.email);
        email.setText("email : " + mFirebaseAuth.getCurrentUser().getEmail());

        TextView uid = (TextView) view.findViewById(R.id.uid);
        uid.setText("uid : " + mFirebaseAuth.getCurrentUser().getUid());


        mCourceList = view.findViewById(R.id.cource_listview);


        SharedPreferences setting = getActivity().getSharedPreferences("setting", 0);
        String scheduleList = setting.getString("scheduleList", "");

        Button takeScheduleButton = (Button) view.findViewById(R.id.take_schedule_button);
        takeScheduleButton.setVisibility(TextUtils.isEmpty(scheduleList) ? View.VISIBLE : View.GONE);
        takeScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), WebViewActivity.class));
                }
            }
        });

        String subejcts[] = scheduleList.split("\n");


        // 이건 only test용... 필요할 때 enable 해서 쓰세요
//        SubjectInfo testSubject = new SubjectInfo("testSubject", "testName", "RoomA");
//        addCourceView(inflater, container, testSubject);

        Log.d(TAG, "subejcts length : " + subejcts.length);
        for (String subject : subejcts) {
            Log.d(TAG, "subject: " + subject);
            String[] subejctInfo = subject.split("/");
            try {
                String subjectTitle = subejctInfo[0];
                String professorName = subejctInfo[1];
                String roomName = subejctInfo[2];

                SubjectInfo subjectInfo = new SubjectInfo(subjectTitle, professorName, roomName);
                addCourceView(inflater, container, subjectInfo);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    private void addCourceView(LayoutInflater inflater, @Nullable ViewGroup container, SubjectInfo subject) {
        if (inflater == null || container == null) {
            Log.e(TAG, "Failed to add CourceView. caused inflater or container is null.");
            return;
        }

        final View courseView = inflater.inflate(R.layout.schedule_list_view, container, false);
        final TextView subjectTextView = courseView.findViewById(R.id.subject);
        subjectTextView.setText(subject.getSubject());
        final TextView professorTextView = courseView.findViewById(R.id.professor);
        professorTextView.setText(subject.getProfessor());
        final TextView roomTextView = courseView.findViewById(R.id.room);
        roomTextView.setText(subject.getRoom());


        courseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("subject", subjectTextView.getText());
                intent.putExtra("professor", professorTextView.getText());
                intent.putExtra("room", roomTextView.getText());
                startActivity(intent);
            }
        });

        if (mCourceList != null) {
            mCourceList.addView(courseView);
        }
    }

}
