package kr.ac.korea.lecturestalk.kulecturestalk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import kr.ac.korea.lecturestalk.kulecturestalk.cource.Model.Post;

public class WritePostFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "##### WritePage Log:";
    private Spinner spinner;
    private SpinnerAdapter spinnerAdapter;
    private String[] category;
    private ImageButton write;
    private EditText editTextTitle, editTextContent;
    private Post post = new Post(null
            , null
            , null
            , null
            , null
            , null
            , null
            , null
            , null
            , new ArrayList<Integer>()
            , new ArrayList<String>()
            ,0
            ,0
            ,0
            , null );
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String docID;
    private FirebaseAuth mFirebaseAuth;
    String userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_post, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userid = mFirebaseAuth.getCurrentUser().getUid();

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

        editTextTitle = (EditText)view.findViewById(R.id.editTextTitle);
        editTextContent = (EditText)view.findViewById(R.id.editTextContent);

        write = (ImageButton)view.findViewById(R.id.buttonWrite2);
        write.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        setPost();
    }

    private void setPost() {
        post.setTitle(editTextTitle.getText().toString());
        post.setDescription(editTextContent.getText().toString());
        post.setCourse("SW Engineering");
        post.setProfessor("Peter");
        post.setSemester("18-2");
        post.setTimeTable("월수");
        post.setAuthor(userid);

        long now = System.currentTimeMillis();
        post.setTime(now);

        Log.d("@@@@@log", String.valueOf(now));
        sendPost();
    }

    private void sendPost() {
        db.collection("Post").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                docID = documentReference.getId();
                Toast.makeText(getContext(), "Write !", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.post_container, new PostListFragment()).commit();
                db.collection("Post").document(docID).update(
                        "id", docID
                );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }


}
