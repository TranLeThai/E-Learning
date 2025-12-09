package com.example.tutorial.activities.customer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tutorial.R;
import com.example.tutorial.fragments.ExploreFragment;
import com.example.tutorial.fragments.HomeFragment;
import com.example.tutorial.fragments.ProfileFragment;
import com.example.tutorial.fragments.RoadmapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Layout chứa BottomNav

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Mặc định load HomeFragment khi mở app
        loadFragment(new HomeFragment());

        // Bắt sự kiện click vào menu
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Dùng if-else thay vì switch-case (do chuẩn Java mới trong Android)
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_explore) {
                selectedFragment = new ExploreFragment(); // Bạn cần tạo file này tương tự HomeFragment
            } else if (itemId == R.id.nav_roadmap) {
                selectedFragment = new RoadmapFragment();
            } else if (itemId == R.id.nav_chat) {
                //selectedFragment = new ChatFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // fragment_container là ID của FrameLayout trong activity_main
                .commit();
    }
}