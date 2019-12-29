package com.example.android.chalkpad;

/**
 * Created by Ayush on 15-09-2017.
 */

public class Course {
    private String name;
    private String code;
    private String type;
    private String teacher;
    Course(String name, String code, String type, String teacher)
    {
        this.name=name;
        this.code=code;
        this.type=type;
        this.teacher=teacher;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
