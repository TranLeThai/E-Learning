package com.example.tutorial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tutorial.R;
import com.example.tutorial.models.Course;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private OnCourseClickListener listener;

    // Interface để xử lý sự kiện click
    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public CoursesAdapter(List<Course> courseList, OnCourseClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lưu ý: Đảm bảo bạn có file layout item_courses.xml
        // Nếu chưa có, hãy tạo tạm một file layout đơn giản
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        // Giả sử item_courses.xml có các id này. Bạn cần chỉnh sửa cho khớp với file item_courses thực tế
        // holder.tvTitle.setText(course.getTitle());

        holder.itemView.setOnClickListener(v -> listener.onCourseClick(course));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle; // Khai báo các view trong item_courses.xml

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ view: tvTitle = itemView.findViewById(R.id.tvCourseTitle);
        }
    }
}