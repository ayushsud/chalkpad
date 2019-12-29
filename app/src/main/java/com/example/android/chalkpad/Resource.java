package com.example.android.chalkpad;

/**
 * Created by Ayush on 16-09-2017.
 */

public class Resource {
    String name;
    String teacher;
    String date;
    String url;

    Resource(String name, String teacher, String date,String url) {
        this.name = name;
        this.teacher = teacher;
        this.date = date;
        this.url=url;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getUrl() {
        return url;
    }
}
