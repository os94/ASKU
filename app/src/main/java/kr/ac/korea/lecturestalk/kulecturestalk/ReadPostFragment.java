package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.cource.Model.Post;

import static kr.ac.korea.lecturestalk.kulecturestalk.MainActivity.userid;

public class ReadPostFragment extends Fragment implements View.OnClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Post post;
    private final String TAG = "@@@@@Read";
    TextView tv_author, tv_time, tv_view, tv_like, tv_tile, tv_content;
    private String docID;
    Button btn_like, btn_msg, btn_report;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_post, container, false);

        docID = getArguments().getString("id");
        DocumentReference docRef = db.collection("Post").document(docID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();
                        post = new Post( (String)data.get("id")
                                , (String)data.get("author")
                                , (String)data.get("course")
                                , (String)data.get("semester")
                                , (String)data.get("professor")
                                , (String)data.get("timetable")
                                , (String)data.get("category")
                                , (String)data.get("title")
                                , (String)data.get("description")
                                , (List<Integer>)data.get("comments")
                                , (ArrayList<String>)data.get("likes")
                                , (int) (long) data.get("numView") //firestore에 int로 넣었지만, long으로 들어가고 반납되고 있음. 때문에 int로 형변환
                                , (long)data.get("time")
                                , (List<String>)data.get("numReports")
                                , (String)data.get("img")
                        );
                        Log.d(TAG, "why!!!"+(String)data.get("id"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                setView(post);
            }
        });

        tv_author = (TextView)view.findViewById(R.id.tv_author);
        tv_time = (TextView)view.findViewById(R.id.tv_time);
        tv_view = (TextView)view.findViewById(R.id.tv_view);
        tv_like = (TextView)view.findViewById(R.id.tv_like);
        tv_tile = (TextView)view.findViewById(R.id.tv_title);
        tv_content = (TextView)view.findViewById(R.id.tv_content);

        btn_like = view.findViewById(R.id.btn_like);
        btn_msg = view.findViewById(R.id.btn_msg);
        btn_report = view.findViewById(R.id.btn_report);
        btn_like.setOnClickListener(this);
        btn_msg.setOnClickListener(this);
        btn_report.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    void setView(Post post) {
        Log.d(TAG, "why???"+(String)post.getId());
        tv_author.setText(post.getAuthor());
        tv_time.setText(post.getFormattedTime());
        tv_view.setText(String.valueOf(post.getNumView()));
        tv_like.setText(String.valueOf(post.getLikes().size()));
        tv_tile.setText(post.getTitle());
        tv_content.setText(post.getDescription());

        db.collection("Post").document(docID).update(
                "numView", (int)post.getNumView()+1
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_like:
                if(post.getLikes().contains(userid)) {
                    Toast.makeText(getContext(), "이미 추천했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    post.getLikes().add(userid);
                    db.collection("Post").document(docID).update(
                            "likes", post.getLikes()
                    );
                    Toast.makeText(getContext(), "이 글을 추천했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_msg:
                Toast.makeText(getContext(), "msg btn clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_report:
                if(post.getNumReports().contains(userid)) {
                    Toast.makeText(getContext(), "이미 신고했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    post.getNumReports().add(userid);
                    db.collection("Post").document(docID).update(
                            "numReports", post.getNumReports()
                    );
                    Toast.makeText(getContext(), "이 글을 신고했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
