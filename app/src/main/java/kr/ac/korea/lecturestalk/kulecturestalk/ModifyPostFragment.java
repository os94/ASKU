package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Post;

import static android.app.Activity.RESULT_OK;

public class ModifyPostFragment extends Fragment implements View.OnClickListener {
    private EditText et_title, et_content;
    private ImageButton btn_write;
    private String docID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StorageReference storageRef2;
    private final String TAG = "@@@@@ Modify";
    private Spinner spinner;
    private SpinnerAdapter spinnerAdapter;
    private String[] category;
    private Post post = new Post(null
            , null
            , null
            , null
            , null
            , null
            , null
            , null
            , null
            , null
            , new ArrayList<String>()
            , new ArrayList<String>()
            ,0
            ,0
            , new ArrayList<String>()
            , null );
    private ProgressBar progressBar;
    private LinearLayout layout;
    private ImageView iv_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_modify_post, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        layout = view.findViewById(R.id.modify_post_view);
        progressBar.setVisibility(view.VISIBLE);
        layout.setVisibility(view.GONE);

        et_title = view.findViewById(R.id.editTextTitle);
        et_content = view.findViewById(R.id.editTextContent);
        btn_write = view.findViewById(R.id.buttonWrite2);

        category = getResources().getStringArray(R.array.category);
        spinner = (Spinner)view.findViewById(R.id.category);
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.category, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                post.setCategory(category[i]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btn_write = (ImageButton)view.findViewById(R.id.buttonWrite2);
        btn_write.setOnClickListener(this);

        iv_img = view.findViewById(R.id.iv_img);

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
                                , (String)data.get("authorID")
                                , (String)data.get("course")
                                , (String)data.get("semester")
                                , (String)data.get("professor")
                                , (String)data.get("timetable")
                                , (String)data.get("category")
                                , (String)data.get("title")
                                , (String)data.get("description")
                                , (List<String>)data.get("comments")
                                , (ArrayList<String>)data.get("likes")
                                , (int) (long) data.get("numView")
                                , (long)data.get("time")
                                , (List<String>)data.get("numReports")
                                , (String)data.get("img")
                        );
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                setView(post);
                switch (post.getCategory()) {
                    case "공지" :
                        spinner.setSelection(0);
                        break;
                    case "수업자료" :
                        spinner.setSelection(1);
                        break;
                    case "QA" :
                        spinner.setSelection(2);
                        break;
                    case "기타" :
                        spinner.setSelection(3);
                        break;
                }
                storageRef2 = storageRef.child("images/" + post.getImg());
                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef2)
                        .centerCrop()
                        .into(iv_img);
                progressBar.setVisibility(view.GONE);
                layout.setVisibility(view.VISIBLE);
            }
        });

        return view;
    }

    private void setView(Post post) {
        et_title.setText(post.getTitle());
        et_content.setText(post.getDescription());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonWrite2:
                setPost();
                break;
        }
    }

    private void setPost() {
        post.setTitle(et_title.getText().toString());
        post.setDescription(et_content.getText().toString());
        sendPost();
    }

    private void sendPost() {
        db.collection("Post").document(docID).update(
                "title", post.getTitle(),
                "description", post.getDescription(),
                "category", post.getCategory()
        );
        Toast.makeText(getContext(), "수정완료!", Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().remove(ModifyPostFragment.this).commit();
        getFragmentManager().popBackStack();
    }
}
