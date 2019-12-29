package com.example.android.chalkpad;

/**
 * Created by Ayush on 14-09-2017.
 */

public class Attendance {
    private String section;
    private String subject;
    private String teacher;
    private String delivered;
    private String attended;
    private float percent;

    public Attendance(String section, String subject, String teacher, String delivered, String attended) {
        this.section = section;
        this.subject = subject;
        this.teacher = teacher;
        this.delivered = delivered;
        this.attended = attended;
        this.percent = Float.parseFloat(attended)*100/Float.parseFloat(delivered);
    }

    public String getPercent() {
        return String.format("%.2f",percent);
    }

    public String getAttended() {
        return attended;
    }

    public String getDelivered() {
        return delivered;
    }

    public String getSection() {
        return section;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }
}
