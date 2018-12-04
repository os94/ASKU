package kr.ac.korea.lecturestalk.kulecturestalk;

import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.course.Adapter.PostListAdapter;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.course.View.EmptyRecyclerView;

public class PostListFragment extends Fragment implements View.OnClickListener {
    private TextView mAllTab, mNoticeTab, mMaterialsTab, mQnaTab, mEtcTab;
    private FloatingActionButton writeButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "@@@@@ get:";
    private EmptyRecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout Layout;
    private String professor, course;
    private String sort_selected = "시간순";
    private String category_selected = null;
    private Spinner spinner;
    private SpinnerAdapter spinnerAdapter;
    private String[] sort;
    private Comparator<Post> cmpPost;

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

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
        alert_confirm.setPositiveButton(R.string.point_notice_ok, null);
        alert_confirm.setMessage(R.string.point_notice);
        final AlertDialog alert = alert_confirm.create();
        alert.setTitle(R.string.point_notice_body);

        ImageView pointNotice = (ImageView)view.findViewById(R.id.ps1);
        TextView pointNotice2 = (TextView)view.findViewById(R.id.ps2);
        pointNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });
        pointNotice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });

        sort = getResources().getStringArray(R.array.sort);
        spinner = (Spinner)view.findViewById(R.id.sort);
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sort_selected = sort[i];
                if(view!=null) { //spinner아이템 눌렀을땐 view값 들어옴
                    Log.d("@@@@@log1:", ""+view);
                    getDB(view);
                } else { //뒤로버튼을 통해 진입시 view값 null임. getView를 통해 구함
                    Log.d("@@@@@log2:", ""+getView());
                    getDB(getView());
                }
                //getDB(view);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // default
        mAllTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        getDB(view);

        //setView
        setView(view);

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
        switch (view.getId()) {
            case R.id.course_tab_all:
                category_selected = null;
                break;
            case R.id.course_tab_notice:
                category_selected = "공지";
                break;
            case R.id.course_tab_materials:
                category_selected = "수업자료";
                break;
            case R.id.course_tab_qna:
                category_selected = "QA";
                break;
            case R.id.course_tab_etc:
                category_selected = "기타";
                break;
        }
        getDB(view.getRootView());
    }

    public void setCategoryColor() {
        mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
        mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
        mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
        mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
        mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
        if(category_selected == null) {
            mAllTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        } else if(category_selected.equals("공지")) {
            mNoticeTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        } else if(category_selected.equals("수업자료")) {
            mMaterialsTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        } else if(category_selected.equals("QA")) {
            mQnaTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        } else if(category_selected.equals("기타")) {
            mEtcTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        }
    }

    private void getDB(final View view) {
        final ArrayList<Post> posts = new ArrayList<>();
        setCategoryColor();

        if(sort_selected.equals("시간순")) {
            cmpPost = new Comparator<Post>() {
                @Override
                public int compare(Post post, Post t1) {
                    if(post.getTime() > t1.getTime())
                        return -1;
                    else if(post.getTime() == t1.getTime())
                        return 0;
                    else
                        return 1;
                }
            };
        } else if (sort_selected.equals("추천순")) {
            cmpPost = new Comparator<Post>() {
                @Override
                public int compare(Post post, Post t1) {
                    if(post.getLikes().size() > t1.getLikes().size())
                        return -1;
                    else if(post.getLikes().size() == t1.getLikes().size())
                        return 0;
                    else
                        return 1;
                }
            };
        } else if (sort_selected.equals("조회순")) {
            cmpPost = new Comparator<Post>() {
                @Override
                public int compare(Post post, Post t1) {
                    if(post.getNumView() > t1.getNumView())
                        return -1;
                    else if(post.getNumView() == t1.getNumView())
                        return 0;
                    else
                        return 1;
                }
            };
        } else if (sort_selected.equals("댓글순")) {
            cmpPost = new Comparator<Post>() {
                @Override
                public int compare(Post post, Post t1) {
                    if(post.getComments().size() > t1.getComments().size())
                        return -1;
                    else if(post.getComments().size() == t1.getComments().size())
                        return 0;
                    else
                        return 1;
                }
            };
        }

        if(category_selected == null) {
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
                                ArrayList<Post> posts_new = posts;
                                Collections.sort(posts_new, cmpPost);
                                recyclerView.setAdapter(new PostListAdapter(posts_new));
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                            progressBar.setVisibility(view.GONE);
                            Layout.setVisibility(view.VISIBLE);
                        }
                    });
        } else {
            db.collection("Post")
                    .whereEqualTo("professor", professor)
                    .whereEqualTo("course", course)
                    .whereEqualTo("category", category_selected)
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
                                ArrayList<Post> posts_new = posts;
                                Collections.sort(posts_new, cmpPost);
                                recyclerView.setAdapter(new PostListAdapter(posts_new));
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                            progressBar.setVisibility(view.GONE);
                            Layout.setVisibility(view.VISIBLE);
                        }
                    });
        }

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
