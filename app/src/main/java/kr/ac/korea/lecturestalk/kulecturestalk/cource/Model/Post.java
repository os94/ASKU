package kr.ac.korea.lecturestalk.kulecturestalk.cource.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Post {
    String id;
    String author;

    String course;
    String semester;
    String professor;
    String timeTable;

    String category;
    String title;
    String description;
    List<Integer> comments; // List of the id of comments
    List<String> likes; // List of the id of the users who pressed like
    int numView;
    long time; // TODO: Format?
    int numReports;
    String img;

    public Post(String id, String author, String course, String semester, String professor,
                String timeTable, String category, String title, String description,
                List<Integer> comments, List<String> likes, int numView, long time,
                int numReports, String img) {
        this.id = id;
        this.author = author;
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

    public List<Integer> getComments() {
        return comments;
    }

    public void setComments(List<Integer> comments) {
        this.comments = comments;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
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

    public int getNumReports() {
        return numReports;
    }

    public void setNumReports(int numReports) {
        this.numReports = numReports;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
