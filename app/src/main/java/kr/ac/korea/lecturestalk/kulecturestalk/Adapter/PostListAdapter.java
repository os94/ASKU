package kr.ac.korea.lecturestalk.kulecturestalk.Adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kr.ac.korea.lecturestalk.kulecturestalk.ReadPostFragment;
import kr.ac.korea.lecturestalk.kulecturestalk.cource.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.R;

public class PostListAdapter extends RecyclerView.Adapter<PostListHolder> {
    private List<Post> mPostList;

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
                fragment.setArguments(bundle);

                //((FragmentActivity) view.getContext()).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.post_container, fragment).addToBackStack(null).commit();
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
        titleTextView.setText(post.getTitle());
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
