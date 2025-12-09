package com.example.tutorial.adapters; // Hãy kiểm tra lại tên package của bạn cho đúng (tutorial hoặc mockup)

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Import Class Course (Hãy chắc chắn bạn đã tạo class Course trong package models)
import com.example.tutorial.models.Course;
import com.example.tutorial.R;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.CourseViewHolder> {

    private Context context;
    private List<Course> listCourse;

    public RecommendedAdapter(Context context, List<Course> listCourse) {
        this.context = context;
        this.listCourse = listCourse;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_card_horizontal, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = listCourse.get(position);
        if (course == null) {
            return;
        }

        holder.tvTitle.setText(course.getTitle());
        holder.tvLevel.setText(course.getLevel());
    }

    @Override
    public int getItemCount() {
        if (listCourse != null) {
            return listCourse.size();
        }
        return 0;
    }
    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvLevel;
        private ImageView imgCourse;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ ID từ file item_course_card_horizontal.xml
            tvTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            imgCourse = itemView.findViewById(R.id.imgCourseThumb);
        }
    }
}