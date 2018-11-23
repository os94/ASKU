package kr.ac.korea.lecturestalk.kulecturestalk.course.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Comment;
import kr.ac.korea.lecturestalk.kulecturestalk.course.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.message.MsgNewActivity;

import static kr.ac.korea.lecturestalk.kulecturestalk.MainActivity.userEmail;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListHolder> {
    private final Post post;
    private List<Comment> mCommentList;

    public CommentListAdapter(Post post, List<Comment> commentList) {
        this.post = post;
        this.mCommentList = commentList;
    }

    @Override
    public CommentListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentListHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentListHolder holder, int position) {
        final Comment comment = mCommentList.get(position);
        holder.bindComment(comment);

        holder.unpickedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int index = userEmail.indexOf("@");
                String author = userEmail.substring(0, index);
                if (post.getAuthor().equals(author)) {
                    // Pick?
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(R.string.pick_comment_title)
                            .setMessage(R.string.pick_comment_content)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // TODO: 포인트 관리?
                                    Toast.makeText(view.getContext(), R.string.comment_picked, Toast.LENGTH_SHORT).show();
                                    holder.unpickedImageView.setVisibility(View.GONE);
                                    holder.pickedImageView.setVisibility(View.VISIBLE);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.show();
                } else {
                    // No authority
                    Toast.makeText(view.getContext(), "Not allowed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.msgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: implementation
                Toast.makeText(view.getContext(), "Send To Msg!", Toast.LENGTH_SHORT).show();

                //New Message Activity로 이동.
                Intent intent = new Intent(view.getContext(), MsgNewActivity.class);
                intent.putExtra("receiver", holder.authorTextView.getText());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }
}

class CommentListHolder extends RecyclerView.ViewHolder {
    public TextView authorTextView;
    private TextView timeTextView;
    private TextView descTextView;
    ImageView pickedImageView;
    ImageView unpickedImageView;
    ImageView msgImageView;

    CommentListHolder(View itemView) {
        super(itemView);
        authorTextView = itemView.findViewById(R.id.comment_author);
        timeTextView = itemView.findViewById(R.id.comment_time);
        descTextView = itemView.findViewById(R.id.comment_desc);
        pickedImageView = itemView.findViewById(R.id.iv_picked);
        unpickedImageView = itemView.findViewById(R.id.iv_notPicked);
        msgImageView = itemView.findViewById(R.id.iv_send_msg);
    }

    void bindComment(Comment comment) {
        authorTextView.setText(comment.getAuthor());
        timeTextView.setText(comment.getFormattedTime());
        descTextView.setText(comment.getDesc());
        if (comment.isPicked()) {
            pickedImageView.setVisibility(View.VISIBLE);
            unpickedImageView.setVisibility(View.GONE);
        } else {
            pickedImageView.setVisibility(View.GONE);
            unpickedImageView.setVisibility(View.VISIBLE);
        }
    }
}