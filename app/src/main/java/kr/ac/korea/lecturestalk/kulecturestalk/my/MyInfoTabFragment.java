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

import kr.ac.korea.lecturestalk.kulecturestalk.GetPointListener;
import kr.ac.korea.lecturestalk.kulecturestalk.LoginActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.MainActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Point;
import kr.ac.korea.lecturestalk.kulecturestalk.schedule.WebViewActivity;

import static kr.ac.korea.lecturestalk.kulecturestalk.MainActivity.userEmail;

public class MyInfoTabFragment extends Fragment {
    private static final String TAG = MyInfoTabFragment.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private TextView tv_currPoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        TextView tv_user = (TextView)view.findViewById(R.id.user);
        int index = userEmail.indexOf("@");
        String user = userEmail.substring(0, index);
        tv_user.setText(user);

        tv_currPoint = (TextView) view.findViewById(R.id.currPoint);
        final Point pointModel = new Point();
        pointModel.getPoint(new GetPointListener() {
            @Override
            public int onPointLoaded(int point) {
                tv_currPoint.setText(""+point);
                return 0;
            }
        });

        /*Button logout = (Button) view.findViewById(R.id.logout);
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
        });*/

        return view;
    }
}
