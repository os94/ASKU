package kr.ac.korea.lecturestalk.kulecturestalk.schedule;

public class SubjectInfo {
    private String subject;
    private String professor;
    private String room;

    public SubjectInfo (String subject, String professor, String room) {
        this.subject = subject;
        this.professor = professor;
        this.room = room;
    }

    public String getSubject() {
        return subject;
    }

    public String getProfessor() {
        return professor;
    }

    public String getRoom() {
        return room;
    }
}