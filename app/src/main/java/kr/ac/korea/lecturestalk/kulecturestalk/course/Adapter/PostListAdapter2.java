package kr.ac.korea.lecturestalk.kulecturestalk.course.Adapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import kr.ac.korea.lecturestalk.kulecturestalk.ModifyPostFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.ReadPostFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.R;

import static kr.ac.korea.lecturestalk.kulecturestalk.MainActivity.userid;

public class PostListAdapter2 extends RecyclerView.Adapter<PostListHolder> {
    private List<Post> mPostList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "@@@@@ del:";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference storageRef2;


    public PostListAdapter2(List<Post> postList) {
        this.mPostList = postList;
    }

    @Override
    public PostListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_post, parent, false);
        return new PostListHolder(view);
    }

    @Override
    public void onBindViewHolder(PostListHolder holder, int position) {
        final Post post = mPostList.get(position);
        holder.bindPost(post);
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}
