package com.example.tutorial.models; // Nhớ đổi tên package cho đúng với dự án của bạn

public class Category {
    private String name;
    private String courseCount; // Ví dụ: "12 courses"
    private int iconResId;      // ID của ảnh (R.drawable.ic_...)

    public Category(String name, String courseCount, int iconResId) {
        this.name = name;
        this.courseCount = courseCount;
        this.iconResId = iconResId;
    }

    public String getName() { return name; }
    public String getCourseCount() { return courseCount; }
    public int getIconResId() { return iconResId; }
}