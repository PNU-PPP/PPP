package com.pnuppp.ppp;

public class SubjectInfo {
    int year;
    int semester;
    String subjectName;
    float credit;
    float grade;
    boolean isMajor;

    SubjectInfo(){
        this.year = 0;
        this.semester = 0;
        this.subjectName = "";
        this.credit = 0;
        this.grade = 4.5f;
        this.isMajor = false;
    }
    SubjectInfo(int year, int semester, String subjectName, float credit, float grade, boolean isMajor){
        this.year = year;
        this.semester = semester;
        this.subjectName = subjectName;
        this.credit = credit;
        this.grade = grade;
        this.isMajor = isMajor;
    }
}
