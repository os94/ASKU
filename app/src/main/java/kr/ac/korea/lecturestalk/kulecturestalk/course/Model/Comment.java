package kr.ac.korea.lecturestalk.kulecturestalk.course.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    String id; // Comment Id in Firebase Collection
    String postId; // Dependent post Id in Firebase Collection
    String author;
    String desc;
    long time;
    boolean picked;

    public Comment(String id, String postId, String author, String desc, long time, boolean picked) {
        this.id = id;
        this.postId = postId;
        this.author = author;
        this.desc = desc;
        this.time = time;
        this.picked = picked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        picked = picked;
    }

    public String getFormattedTime() {
        Date date = new Date(this.time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strTime = sdf.format(date);
        return strTime;
    }
}
