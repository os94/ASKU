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

public class PostListAdapter extends RecyclerView.Adapter<PostListHolder> {
    private List<Post> mPostList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "@@@@@ del:";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference storageRef2;


    public PostListAdapter(List<Post> postList) {
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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                //Toast.makeText(view.getContext(), String.valueOf(post), Toast.LENGTH_SHORT).show();

                ReadPostFragment fragment = new ReadPostFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", post.getId()); //send documentID
                //Toast.makeText(view.getContext(), post.getId(), Toast.LENGTH_SHORT).show();
                fragment.setArguments(bundle);

                //((FragmentActivity) view.getContext()).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.post_container, fragment).addToBackStack(null).commit();
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final CharSequence[] items = {view.getContext().getString(R.string.dialog_modification),
                        view.getContext().getString(R.string.dialog_del)};
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setTitle(R.string.manage_boards);
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                if (userid.equals(post.getAuthorID())) {
                                    switch (id) {
                                        case 0: // modify doc
                                            ModifyPostFragment fragment = new ModifyPostFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", post.getId()); //send documentID
                                            fragment.setArguments(bundle);
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.post_container, fragment).addToBackStack(null).commit();
                                            break;
                                        case 1: // delete doc
                                            db.collection("Post").document(post.getId())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                            Toast.makeText(view.getContext(), R.string.deleted_the_post, Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error deleting document", e);
                                                            Toast.makeText(view.getContext(), R.string.failed_to_delete_the_post, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            storageRef2 = storageRef.child("images/" + post.getImg());
                                            storageRef2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // File deleted successfully
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Uh-oh, an error occurred!
                                                }
                                            });
                                            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.post_container, new PostListFragment()).commit();
                                            Fragment fragment2 = activity.getSupportFragmentManager().findFragmentByTag("fragmentTag");
                                            activity.getSupportFragmentManager().beginTransaction().detach(fragment2).attach(fragment2).commit();
                                            break;
                                    }
                                } else {
                                    Toast.makeText(view.getContext(), R.string.do_not_have_permission, Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}

class PostListHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private TextView descTextView;
    private TextView numViewTextView;
    private TextView numLikeTextView;
    private TextView numCommentTextView;
    private TextView dateTextView;

    public final View mView;

    PostListHolder(View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.post_title);
        descTextView = itemView.findViewById(R.id.post_desc);
        numViewTextView = itemView.findViewById(R.id.post_num_view);
        numLikeTextView = itemView.findViewById(R.id.post_num_like);
        numCommentTextView = itemView.findViewById(R.id.post_num_comment);
        dateTextView = itemView.findViewById(R.id.post_date);
        mView = itemView;
    }

    void bindPost(Post post) {
        titleTextView.setText("[" + post.getCategory() + "] " + post.getTitle());
        String desc = post.getDescription();
        if (desc.length() >= 50) {
            desc = desc.substring(0, 50);
            desc += "...";
        }
        descTextView.setText(desc);
        numViewTextView.setText(Integer.toString(post.getNumView()));
        numLikeTextView.setText(Integer.toString(post.getLikes().size()));
        numCommentTextView.setText(String.format("%d", post.getComments().size()));
        dateTextView.setText(post.getFormattedTime());
    }

}
