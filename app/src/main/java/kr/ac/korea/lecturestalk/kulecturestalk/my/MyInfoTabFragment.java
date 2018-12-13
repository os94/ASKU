package kr.ac.korea.lecturestalk.kulecturestalk.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.GetPointListener;
import kr.ac.korea.lecturestalk.kulecturestalk.LoginActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.MainActivity;
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Adapter.PostListAdapter;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Adapter.PostListAdapter2;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Point;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.course.View.EmptyRecyclerView;
import kr.ac.korea.lecturestalk.kulecturestalk.schedule.WebViewActivity;

import static kr.ac.korea.lecturestalk.kulecturestalk.MainActivity.userEmail;

public class MyInfoTabFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = MyInfoTabFragment.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private TextView tv_currPoint;
    private ViewGroup myQuestion, myComment;
    private View underlineQuestion, underlineComment;
    private FirebaseFirestore db;
    private String user;
    private EmptyRecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout Layout;
    private static final int PARAM_Q = 1;
    private static final int PARAM_C = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressBar = view.findViewById(R.id.progressBar);
        Layout = view.findViewById(R.id.post_list_view);
        progressBar.setVisibility(view.VISIBLE);
        Layout.setVisibility(view.GONE);

        recyclerView = view.findViewById(R.id.course_posts_recycler_view);

        TextView tv_user = (TextView)view.findViewById(R.id.user);
        int index = userEmail.indexOf("@");
        user = userEmail.substring(0, index);
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

        ImageView setting = view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), MyInfoActivity.class));
                }
            }
        });

        myQuestion = view.findViewById(R.id.btn_myQ);
        myComment = view.findViewById(R.id.btn_myC);
        myQuestion.setOnClickListener(this);
        myComment.setOnClickListener(this);

        underlineQuestion = view.findViewById(R.id.myQ_Selected);
        underlineComment = view.findViewById(R.id.myC_Selected);

        myQuestion.callOnClick();
        getDB(view, PARAM_Q);
        setView(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(view.VISIBLE);
        Layout.setVisibility(view.GONE);
        switch (view.getId()) {
            case R.id.btn_myQ:
                underlineQuestion.setVisibility(View.VISIBLE);
                underlineComment.setVisibility(View.GONE);
                getDB(view.getRootView(), PARAM_Q);
                break;
            case R.id.btn_myC:
                underlineQuestion.setVisibility(View.GONE);
                underlineComment.setVisibility(View.VISIBLE);
                getDB(view.getRootView(), PARAM_C);
                break;
        }
    }


    private void getDB(final View view, int param) {
        final ArrayList<Post> posts = new ArrayList<>();
        final ArrayList<String> comments = new ArrayList<>();

        if(param == 1) {
            db.collection("Post")
                    .whereEqualTo("author", user)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, String.valueOf(document.getData()));
                                    Map<String, Object> data = document.getData();

                                    long time = System.currentTimeMillis();
                                    if (data.get("time") != null) {
                                        time = (long) data.get("time");
                                    }

                                    Post post = new Post((String) data.get("id")
                                            , (String) data.get("author")
                                            , (String) data.get("authorID")
                                            , (String) data.get("course")
                                            , (String) data.get("semester")
                                            , (String) data.get("professor")
                                            , (String) data.get("timetable")
                                            , (String) data.get("category")
                                            , (String) data.get("title")
                                            , (String) data.get("description")
                                            , (List<String>) data.get("comments")
                                            , (ArrayList<String>) data.get("likes")
                                            , (int) (long) data.get("numView") //firestore에 int로 넣었지만, long으로 들어가고 반납되고 있음. 때문에 int로 형변환
                                            , time
                                            , (List<String>) data.get("numReports")
                                            , (String) data.get("img")
                                    );
                                    posts.add(post);
                                }
                                ArrayList<Post> posts_new = posts;
                                //Collections.sort(posts_new, cmpPost);
                                recyclerView.setAdapter(new PostListAdapter2(posts_new));
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                            progressBar.setVisibility(view.GONE);
                            Layout.setVisibility(view.VISIBLE);
                        }
                    });
        } else {
            db.collection("Comment")
                    .whereEqualTo("author", user)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, String.valueOf(document.getData()));
                                    Map<String, Object> data = document.getData();
                                    Log.d("@@@@@postid:", String.valueOf(data.get("postId")));
                                    comments.add((String)data.get("postId"));
                                }
                                HashSet<String> distinctData = new HashSet<String>(comments);           // 중복
                                ArrayList<String> comments_new = new ArrayList<String>(distinctData);   // 제거
                                Log.d("@@@@@comments", String.valueOf(comments_new));
                                if(comments_new.isEmpty()) {
                                    ArrayList<Post> posts_new = posts;
                                    //Collections.sort(posts_new, cmpPost);
                                    recyclerView.setAdapter(new PostListAdapter2(posts_new));
                                    progressBar.setVisibility(view.GONE);
                                    Layout.setVisibility(view.VISIBLE);
                                }
                                for (String tmp : comments_new) { // comments_new가 비어있으면 for문 실행안됨
                                    db.collection("Post")
                                            .whereEqualTo("id", tmp)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, String.valueOf(document.getData()));
                                                            Map<String, Object> data = document.getData();

                                                            long time = System.currentTimeMillis();
                                                            if (data.get("time") != null) {
                                                                time = (long) data.get("time");
                                                            }

                                                            Post post = new Post((String) data.get("id")
                                                                    , (String) data.get("author")
                                                                    , (String) data.get("authorID")
                                                                    , (String) data.get("course")
                                                                    , (String) data.get("semester")
                                                                    , (String) data.get("professor")
                                                                    , (String) data.get("timetable")
                                                                    , (String) data.get("category")
                                                                    , (String) data.get("title")
                                                                    , (String) data.get("description")
                                                                    , (List<String>) data.get("comments")
                                                                    , (ArrayList<String>) data.get("likes")
                                                                    , (int) (long) data.get("numView") //firestore에 int로 넣었지만, long으로 들어가고 반납되고 있음. 때문에 int로 형변환
                                                                    , time
                                                                    , (List<String>) data.get("numReports")
                                                                    , (String) data.get("img")
                                                            );
                                                            posts.add(post);
                                                        }
                                                        ArrayList<Post> posts_new = posts;
                                                        //Collections.sort(posts_new, cmpPost);
                                                        recyclerView.setAdapter(new PostListAdapter2(posts_new));
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                    progressBar.setVisibility(view.GONE);
                                                    Layout.setVisibility(view.VISIBLE);
                                                }
                                            });
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

    }

    private void setView(View view) {
        // Use EmptyRecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Fetch the empty view from the layout and set it on the new recycler view
        View emptyView = view.findViewById(R.id.course_post_empty);
        recyclerView.setEmptyView(emptyView);
    }

}
