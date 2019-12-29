package com.example.android.chalkpad;

/**
 * Created by Ayush on 16-09-2017.
 */

public class TimeTable {
    String day;
    String lec1;
    String lec2;
    String lec3;
    String lec4;
    String lec5;
    String lec6;
    String lec7;
    String lec8;

    TimeTable(String day, String lec1, String lec2, String lec3, String lec4, String lec5, String lec6, String lec7, String lec8) {
        this.day = day;
        this.lec1 = lec1;
        this.lec2 = lec2;
        this.lec3 = lec3;
        this.lec4 = lec4;
        this.lec5 = lec5;
        this.lec6 = lec6;
        this.lec7 = lec7;
        this.lec8 = lec8;
    }

    public String getDay() {
        return day;
    }

    public String getLec1() {
        return lec1;
    }

    public String getLec2() {
        return lec2;
    }

    public String getLec3() {
        return lec3;
    }

    public String getLec4() {
        return lec4;
    }

    public String getLec5() {
        return lec5;
    }

    public String getLec6() {
        return lec6;
    }

    public String getLec7() {
        return lec7;
    }

    public String getLec8() {
        return lec8;
    }
}
