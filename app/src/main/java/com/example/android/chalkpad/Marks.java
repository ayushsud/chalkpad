package com.example.android.chalkpad;

/**
 * Created by Ayush on 29-09-2017.
 */

public class Marks {
    String type;
    String date;
    String teacher;
    String marks;
    String total;
    Marks(String type,String date,String teacher,String marks,String total)
    {
        this.type=type;
        this.date=date;
        this.teacher=teacher;
        this.marks=marks;
        this.total=total;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getMarks() {
        return marks;
    }

    public String getTotal() {
        return total;
    }
}
