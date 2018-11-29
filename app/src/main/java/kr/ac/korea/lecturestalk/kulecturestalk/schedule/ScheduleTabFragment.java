package kr.ac.korea.lecturestalk.kulecturestalk.schedule;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import kr.ac.korea.lecturestalk.kulecturestalk.GetPointListener;
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.course.CourseActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Point;

public class ScheduleTabFragment extends Fragment {
    private static final String TAG = ScheduleTabFragment.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private LinearLayout mCourceList;
    private AnimationDrawable d;
    private TextView currPoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_tab, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();

        /*TextView email = (TextView) view.findViewById(R.id.email);
        email.setText("email : " + mFirebaseAuth.getCurrentUser().getEmail());

        TextView uid = (TextView) view.findViewById(R.id.uid);
        uid.setText("uid : " + mFirebaseAuth.getCurrentUser().getUid());*/


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

        currPoint = (TextView) view.findViewById(R.id.currPoint);
        final Point pointModel = new Point();
        pointModel.getPoint(new GetPointListener() {
            @Override
            public int onPointLoaded(int point) {
                currPoint.setText(""+point);
                return 0;
            }
        });

        Button refreshBtn = (Button)view.findViewById(R.id.detail_refresh_btn);
        d = (AnimationDrawable)refreshBtn.getCompoundDrawables()[0];
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.start();
                pointModel.getPoint(new GetPointListener() {
                    @Override
                    public int onPointLoaded(int point) {
                        currPoint.setText(""+point);
                        d.stop();
                        return 0;
                    }
                });
            }
        });

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
        alert_confirm.setPositiveButton("확인", null);
        alert_confirm.setMessage(R.string.point_notice);
        final AlertDialog alert = alert_confirm.create();
        alert.setTitle("ASKU 포인트 정책");


        ImageView pointNotice = (ImageView)view.findViewById(R.id.notice);
        pointNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });

        if (TextUtils.isEmpty(scheduleList)) {
            // 이건 only test용... 필요할 때 enable 해서 쓰세요
            /*SubjectInfo testSubject = new SubjectInfo("소프트웨어공학", "인호", "정보통신관 202호");
            addCourceView(inflater, container, testSubject);*/
        } else {

            String subejcts[] = scheduleList.split("\n");
            Set<String> subjectSet = new HashSet<String>(Arrays.asList(subejcts));
            Log.d(TAG, "subejcts length : " + subejcts.length);

            for (String subject : subjectSet) {
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
