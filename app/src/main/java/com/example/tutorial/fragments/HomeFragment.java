package com.example.tutorial.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tutorial.R;
import com.example.tutorial.adapters.RecommendedAdapter;
import com.example.tutorial.models.Course;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvRecommended;
    private RecommendedAdapter adapter;
    private List<Course> courseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp giao diện fragment_home.xml
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ RecyclerView
        rvRecommended = view.findViewById(R.id.rvRecommended);

        // Tạo dữ liệu giả (Dummy Data)
        courseList = new ArrayList<>();
        courseList.add(new Course("English for IT Support", "Beginner", "4.5", 1200, R.drawable.bg_green_wave));
        courseList.add(new Course("Business Email Writing", "Intermediate", "4.8", 850, R.drawable.bg_green_wave));
        courseList.add(new Course("Scrum Master Vocabulary", "Advanced", "4.9", 500, R.drawable.bg_green_wave));

        // Setup Adapter
        adapter = new RecommendedAdapter(getContext(), courseList);

        // Quan trọng: LayoutManager nằm ngang (Horizontal)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvRecommended.setLayoutManager(layoutManager);
        rvRecommended.setAdapter(adapter);

        return view;
    }
}