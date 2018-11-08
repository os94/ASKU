package kr.ac.korea.lecturestalk.kulecturestalk.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
        Post post = mPostList.get(position);
        holder.bindPost(post);
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

    PostListHolder(View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.post_title);
        descTextView = itemView.findViewById(R.id.post_desc);
        numViewTextView = itemView.findViewById(R.id.post_num_view);
        numLikeTextView = itemView.findViewById(R.id.post_num_like);
        numCommentTextView = itemView.findViewById(R.id.post_num_comment);
        dateTextView = itemView.findViewById(R.id.post_date);
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
