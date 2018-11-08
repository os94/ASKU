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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.cource.Model.Post;

public class ReadPostFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Post post;
    private final String TAG = "@@@@@Read";
    TextView tv_author, tv_time, tv_view, tv_like, tv_tile, tv_content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_post, container, false);

        String docID = getArguments().getString("id");
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
                                , (List<String>)data.get("likes")
                                , (int) (long) data.get("numView") //firestore에 int로 넣었지만, long으로 들어가고 반납되고 있음. 때문에 int로 형변환
                                , (long)data.get("time")
                                , (int) (long) data.get("numReports")
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
    }

}
