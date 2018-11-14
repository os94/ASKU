package kr.ac.korea.lecturestalk.kulecturestalk.course.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    String id; // Document id in Firebase Collection
    String author; // author's name
    String authorID; // author's unique id

    String course;
    String semester;
    String professor;
    String timeTable;

    String category;
    String title;
    String description;
    List<String> comments; // List of the id of comments
    ArrayList<String> likes; // List of the id of the users who pressed like
    int numView;
    long time; // TODO: Format?
    List<String> numReports;
    String img;

    public Post(String id, String author, String authorID, String course, String semester, String professor,
                String timeTable, String category, String title, String description,
                List<String> comments, ArrayList<String> likes, int numView, long time,
                List<String> numReports, String img) {
        this.id = id;
        this.author = author;
        this.authorID = authorID;
        this.course = course;
        this.semester = semester;
        this.professor = professor;
        this.timeTable = timeTable;
        this.category = category;
        this.title = title;
        this.description = description;
        this.comments = comments;
        this.likes = likes;
        this.numView = numView;
        this.time = time;
        this.numReports = numReports;
        this.img = img;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(String timeTable) {
        this.timeTable = timeTable;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> Arraylikes) {
        this.likes = likes;
    }

    public int getNumView() {
        return numView;
    }

    public void setNumView(int numView) {
        this.numView = numView;
    }

    public String getFormattedTime() {
        Date date = new Date(this.time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strTime = sdf.format(date);
        return strTime;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<String> getNumReports() {
        return numReports;
    }

    public void setNumReports(List<String> numReports) {
        this.numReports = numReports;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
