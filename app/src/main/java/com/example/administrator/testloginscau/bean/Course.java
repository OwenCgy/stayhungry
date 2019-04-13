package com.example.administrator.testloginscau.bean;

public class Course {

    /**
     * 课程名
     */
    private String cName;
    /**第几节开始
     *
     */
    private int startNum;
    /**
     * 第几节结束
     */
    private int endNum;
    /**
     * 星期几
     */
    private String day;
    /**
     * 哪间教室
     */
   // private String classroom;

    private String content;

    public Course() {
    }

    public Course(String cName, int startNum, int endNum, String day, String content) {
        this.cName = cName;
        this.startNum = startNum;
        this.endNum = endNum;
        this.day = day;
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getEndNum() {
        return endNum;
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

   /* public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }*/

}
