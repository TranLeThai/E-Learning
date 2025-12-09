package com.example.tutorial.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial.R;
import com.example.tutorial.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gọi file layout item_category_card.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category == null) return;

        holder.tvName.setText(category.getName());
        holder.tvCount.setText(category.getCourseCount());
        holder.imgIcon.setImageResource(category.getIconResId());

        // Bắt sự kiện khi click vào một chủ đề
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked: " + category.getName(), Toast.LENGTH_SHORT).show();
            // Sau này bạn sẽ viết code chuyển màn hình sang danh sách chi tiết ở đây
        });
    }

    @Override
    public int getItemCount() {
        return (categoryList != null) ? categoryList.size() : 0;
    }

    // ViewHolder
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvName, tvCount;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file item_category_card.xml
            imgIcon = itemView.findViewById(R.id.imgCatIcon);
            tvName = itemView.findViewById(R.id.tvCatName);
            // Lưu ý: Kiểm tra file XML của bạn, nếu chưa có ID cho TextView số lượng thì thêm vào nhé
            // Giả sử trong XML bạn đặt là tvCourseCount, nếu chưa có thì thêm android:id="@+id/tvCourseCount"
            tvCount = itemView.findViewById(R.id.tvCatName); // Tạm thời map vào tên nếu chưa có ID count
            // Sửa lại dòng trên thành ID đúng nếu bạn đã thêm ID cho textview số lượng khóa học
        }
    }
}