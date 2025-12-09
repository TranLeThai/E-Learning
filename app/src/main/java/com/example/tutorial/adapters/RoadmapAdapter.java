package com.example.tutorial.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial.R;
import com.example.tutorial.models.RoadmapNode;

import java.util.List;

public class RoadmapAdapter extends RecyclerView.Adapter<RoadmapAdapter.ViewHolder> {

    private final List<RoadmapNode> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(RoadmapNode node, int position);
    }

    public RoadmapAdapter(List<RoadmapNode> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roadmap_node, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoadmapNode node = items.get(position);
        holder.tvTitle.setText(node.getTitle());
        holder.tvDesc.setText(node.getDescription());
        holder.tvStatus.setText(node.isUnlocked() ? "Unlocked" : "Locked");
        holder.ivIcon.setAlpha(node.isUnlocked() ? 1.0f : 0.4f);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(node, position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle, tvDesc, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivNodeIcon);
            tvTitle = itemView.findViewById(R.id.tvNodeTitle);
            tvDesc = itemView.findViewById(R.id.tvNodeDesc);
            tvStatus = itemView.findViewById(R.id.tvNodeStatus);
        }
    }
}

