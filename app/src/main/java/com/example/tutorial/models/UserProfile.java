package com.example.tutorial.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private String email;
    private String name;
    private String avatarUrl; // can be local resource name or http url
    private List<String> enrolledCourseIds;

    public UserProfile(String email, String name, String avatarUrl, List<String> enrolledCourseIds) {
        this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl != null ? avatarUrl : "";
        this.enrolledCourseIds = enrolledCourseIds != null ? enrolledCourseIds : new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<String> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }

    public void setEnrolledCourseIds(List<String> enrolledCourseIds) {
        this.enrolledCourseIds = enrolledCourseIds;
    }

    public void addCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }

    public void removeCourse(String courseId) {
        enrolledCourseIds.remove(courseId);
    }

    public String toJson() {
        JSONObject o = new JSONObject();
        try {
            o.put("email", email);
            o.put("name", name);
            o.put("avatarUrl", avatarUrl);
            JSONArray arr = new JSONArray();
            for (String c : enrolledCourseIds) arr.put(c);
            o.put("courses", arr);
            return o.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UserProfile fromJson(String json) {
        if (json == null) return null;
        try {
            JSONObject o = new JSONObject(json);
            String email = o.optString("email", "");
            String name = o.optString("name", "");
            String avatar = o.optString("avatarUrl", "");
            List<String> courses = new ArrayList<>();
            JSONArray arr = o.optJSONArray("courses");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    courses.add(arr.optString(i));
                }
            }
            return new UserProfile(email, name, avatar, courses);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

