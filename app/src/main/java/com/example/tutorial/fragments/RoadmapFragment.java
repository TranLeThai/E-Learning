package com.example.tutorial.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial.R;
import com.example.tutorial.adapters.RoadmapAdapter;
import com.example.tutorial.models.RoadmapNode;

import java.util.ArrayList;
import java.util.List;

public class RoadmapFragment extends Fragment {

    private RecyclerView rvRoadmap;
    private RoadmapAdapter adapter;
    private List<RoadmapNode> nodes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadmap, container, false);

        rvRoadmap = view.findViewById(R.id.rvRoadmap);
        rvRoadmap.setLayoutManager(new LinearLayoutManager(getContext()));

        // Prepare sample data
        prepareSampleNodes();

        adapter = new RoadmapAdapter(nodes, (node, position) -> {
            // Basic click handling: show toast or toggle unlocked for demo
            if (!node.isUnlocked()) {
                // unlock when clicked (demo)
                node.setUnlocked(true);
                adapter.notifyItemChanged(position);
                Toast.makeText(getContext(), "Mở khóa: " + node.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Đã mở: " + node.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        rvRoadmap.setAdapter(adapter);

        return view;
    }

    private void prepareSampleNodes() {
        nodes.clear();
        nodes.add(new RoadmapNode("Basics of Java", "Understand OOP, collections, and syntax.", true));
        nodes.add(new RoadmapNode("Android Fundamentals", "Activity/Fragment lifecycle, layouts, views.", true));
        nodes.add(new RoadmapNode("Networking & REST", "Retrofit, OkHttp, parsing JSON.", false));
        nodes.add(new RoadmapNode("Local Storage", "Room database, SharedPreferences.", false));
        nodes.add(new RoadmapNode("Advanced UI", "Compose, animations, custom views.", false));
        nodes.add(new RoadmapNode("Testing & CI", "Unit tests, instrumentation tests, CI pipelines.", false));
        nodes.add(new RoadmapNode("Performance & Debugging", "Profiling, memory leaks, optimization.", false));
    }
}

