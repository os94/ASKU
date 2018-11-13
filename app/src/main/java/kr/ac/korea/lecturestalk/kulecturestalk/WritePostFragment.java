package kr.ac.korea.lecturestalk.kulecturestalk;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Post;

import static android.app.Activity.RESULT_OK;
import static kr.ac.korea.lecturestalk.kulecturestalk.MainActivity.userEmail;
import static kr.ac.korea.lecturestalk.kulecturestalk.MainActivity.userid;

public class WritePostFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "##### WritePage Log:";
    private Spinner spinner;
    private SpinnerAdapter spinnerAdapter;
    private String[] category;
    private ImageButton write, img;
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
            , null
            , new ArrayList<Integer>()
            , new ArrayList<String>()
            ,0
            ,0
            , new ArrayList<String>()
            , null );
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String docID;
    private static final int GALLERY_CODE = 1112;
    private static final int MY_PERMISSIONS = 101;
    private String[] permissons = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Uri file = null;
    private StorageReference imageRef;
    private UploadTask uploadTask;
    private ImageView iv_img;
    private String professor, course;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_post, container, false);

        professor = getArguments().getString("professor");
        course = getArguments().getString("course");

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

        img = (ImageButton)view.findViewById(R.id.buttonImage);
        img.setOnClickListener(this);
        write = (ImageButton)view.findViewById(R.id.buttonWrite2);
        write.setOnClickListener(this);

        iv_img = view.findViewById(R.id.iv_img);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonImage:
                checkPermission();
                break;
            case R.id.buttonWrite2:
                setPost();
                break;
        }
    }

    private void checkPermission() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for(String pm : permissons) {
            result = ContextCompat.checkSelfPermission(getContext(), pm);
            if(result == PackageManager.PERMISSION_DENIED) {
                permissionList.add(pm);
            } else {
                getImg();
            }
        }
        if(!permissionList.isEmpty()) {
            requestPermissions( permissionList.toArray(new String[permissionList.size()]), MY_PERMISSIONS );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "권한을 허용했습니다.", Toast.LENGTH_SHORT).show();
                    getImg();
                } else {
                    Toast.makeText(getContext(), "권한을 허용해야합니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void getImg() {
        Intent intentImg = new Intent(Intent.ACTION_PICK);
        intentImg.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentImg.setType("image/*");
        startActivityForResult(intentImg, GALLERY_CODE);
    }
    @Override // onActivityResult로 intent의 결과값을 처리하고, data.getData로 사진URI를 가져온다
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            file = data.getData();
            Glide.with(getContext())
                    .load(file)
                    .centerCrop()
                    .into(iv_img);
        }
    }

    private void setPost() {
        post.setTitle(editTextTitle.getText().toString());
        post.setDescription(editTextContent.getText().toString());
        post.setCourse(course);
        post.setProfessor(professor);
        post.setSemester("18-2");
        post.setTimeTable("월수");

        int index = userEmail.indexOf("@");
        String author = userEmail.substring(0, index);
        post.setAuthor(author);
        post.setAuthorID(userid);

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
                Toast.makeText(getContext(), "작성완료!", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().remove(WritePostFragment.this).commit();
                getFragmentManager().popBackStack();
                db.collection("Post").document(docID).update(
                        "id", docID
                );
                if(file != null) {
                    db.collection("Post").document(docID).update(
                            "img", docID
                    );
                    imageRef = storageRef.child("images/"+docID);
                    uploadTask = imageRef.putFile(file);
                    uploadImg();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }
    private void uploadImg() {
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getContext(), "Image Upload Failed.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                //Toast.makeText(getContext(), "Send !", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
