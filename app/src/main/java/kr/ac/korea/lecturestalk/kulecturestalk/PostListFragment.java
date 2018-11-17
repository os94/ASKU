package kr.ac.korea.lecturestalk.kulecturestalk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.course.Adapter.PostListAdapter;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.course.View.EmptyRecyclerView;

public class PostListFragment extends Fragment implements View.OnClickListener {
    private TextView mAllTab;
    private TextView mNoticeTab;
    private TextView mMaterialsTab;
    private TextView mQnaTab;
    private TextView mEtcTab;
    private FloatingActionButton writeButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "@@@@@ get:";
    private EmptyRecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout Layout;
    private String professor, course;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) { //주의, PostListFragment에 bundle이 들어오지않으면 게시물이 뜨지않을것.
            professor = getArguments().getString("professor");
            course = getArguments().getString("course");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        Layout = view.findViewById(R.id.post_list_view);
        progressBar.setVisibility(view.VISIBLE);
        Layout.setVisibility(view.GONE);

        mAllTab = view.findViewById(R.id.course_tab_all);
        mNoticeTab = view.findViewById(R.id.course_tab_notice);
        mMaterialsTab = view.findViewById(R.id.course_tab_materials);
        mQnaTab = view.findViewById(R.id.course_tab_qna);
        mEtcTab = view.findViewById(R.id.course_tab_etc);

        mAllTab.setOnClickListener(this);
        mNoticeTab.setOnClickListener(this);
        mMaterialsTab.setOnClickListener(this);
        mQnaTab.setOnClickListener(this);
        mEtcTab.setOnClickListener(this);

        // default
        mAllTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        getDB(view.getRootView());

        //setView
        setView(view);

        // Test Data
        /*Post p = new Post(1, "juhan", "Software Engineering", "2018-2",
                "Hoh", "월수5", "공지", "Fuck Fuck Fuck",
                "holy shit somebody help me...", new ArrayList<Integer>(),
                new ArrayList<String>(), 7, System.currentTimeMillis(), 0, null);
        posts.add(p);*/

        writeButton = view.findViewById(R.id.btn_write);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WritePostFragment fragment = new WritePostFragment();
                Bundle bundle = new Bundle();
                bundle.putString("course", course);
                bundle.putString("professor", professor);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.post_container, fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
        mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
        mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
        mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
        mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
        switch (view.getId()) {
            case R.id.course_tab_all:
                mAllTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                getDB(view.getRootView());
                break;
            case R.id.course_tab_notice:
                mNoticeTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                getDB(view.getRootView(), "공지");
                break;
            case R.id.course_tab_materials:
                mMaterialsTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                getDB(view.getRootView(), "수업자료");
                break;
            case R.id.course_tab_qna:
                mQnaTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                getDB(view.getRootView(), "QA");
                break;
            case R.id.course_tab_etc:
                mEtcTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                getDB(view.getRootView(), "기타");
                break;
        }
    }

    private void getDB(final View view, String category) {
        final List<Post> posts = new ArrayList<>();
        db.collection("Post")
                .whereEqualTo("professor", professor)
                .whereEqualTo("course", course)
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, String.valueOf(document.getData()));
                                Map<String, Object> data = document.getData();


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
                                        , (long) data.get("time")
                                        , (List<String>) data.get("numReports")
                                        , (String) data.get("img")
                                );
                                posts.add(post);
                            }
                            recyclerView.setAdapter(new PostListAdapter(posts));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        progressBar.setVisibility(view.GONE);
                        Layout.setVisibility(view.VISIBLE);
                    }
                });
    }

    private void getDB(final View view) {
        final List<Post> posts = new ArrayList<>();
        db.collection("Post")
                .whereEqualTo("professor", professor)
                .whereEqualTo("course", course)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, String.valueOf(document.getData()));
                                Map<String, Object> data = document.getData();


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
                                        , (long) data.get("time")
                                        , (List<String>) data.get("numReports")
                                        , (String) data.get("img")
                                );
                                posts.add(post);
                            }
                            recyclerView.setAdapter(new PostListAdapter(posts));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        progressBar.setVisibility(view.GONE);
                        Layout.setVisibility(view.VISIBLE);
                    }
                });
    }

    private void setView(View view) {
        // Use EmptyRecyclerView
        recyclerView = view.findViewById(R.id.course_posts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Fetch the empty view from the layout and set it on the new recycler view
        View emptyView = view.findViewById(R.id.course_post_empty);
        recyclerView.setEmptyView(emptyView);
    }

}
