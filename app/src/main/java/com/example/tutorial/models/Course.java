package com.example.tutorial.models;

public class Course {
    private String title;
    private String level;
    private String rating;
    private int studentCount;
    // Trong thực tế bạn sẽ dùng String url ảnh, ở đây demo dùng int resourceId
    private int imageResId;

    public Course(String title, String level, String rating, int studentCount, int imageResId) {
        this.title = title;
        this.level = level;
        this.rating = rating;
        this.studentCount = studentCount;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public String getLevel() { return level; }
    public String getRating() { return rating; }
    public int getStudentCount() { return studentCount; }
    public int getImageResId() { return imageResId; }
}