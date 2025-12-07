package com.example.tutorial.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial.R;
import com.example.tutorial.adapters.CategoryAdapter;
import com.example.tutorial.models.Category;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private List<Category> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout fragment_explore.xml
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        rvCategories = view.findViewById(R.id.rvCategories);

        // Tạo dữ liệu giả
        categoryList = new ArrayList<>();
        // Lưu ý: Bạn cần có các icon trong drawable (ic_nav_explore, ic_nav_home...) hoặc dùng android.R.drawable...
        categoryList.add(new Category("Business", "15 courses", android.R.drawable.ic_menu_agenda));
        categoryList.add(new Category("Tech / IT", "28 courses", android.R.drawable.ic_menu_manage));
        categoryList.add(new Category("Medical", "8 courses", android.R.drawable.ic_menu_call));
        categoryList.add(new Category("Travel", "12 courses", android.R.drawable.ic_menu_compass));
        categoryList.add(new Category("Daily Life", "40 courses", android.R.drawable.ic_menu_my_calendar));
        categoryList.add(new Category("IELTS Prep", "10 courses", android.R.drawable.ic_menu_edit));

        // Thiết lập Adapter
        adapter = new CategoryAdapter(getContext(), categoryList);

        // Thiết lập GridLayoutManager với 2 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvCategories.setLayoutManager(gridLayoutManager);
        rvCategories.setAdapter(adapter);

        return view;
    }
}