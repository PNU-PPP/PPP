package com.pnuppp.pplusplus;

public class SubjectInfo {
    String subjectName;
    float credit;
    float grade;
    boolean isMajor;

    SubjectInfo(){
        this.subjectName = "";
        this.credit = 0;
        this.grade = 4.5f;
        this.isMajor = false;
    }
    SubjectInfo(String subjectName, float credit, float grade, boolean isMajor){
        this.subjectName = subjectName;
        this.credit = credit;
        this.grade = grade;
        this.isMajor = isMajor;
    }
}
