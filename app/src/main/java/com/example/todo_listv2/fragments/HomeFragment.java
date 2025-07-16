package com.example.todo_listv2.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.todo_listv2.R;
import com.example.todo_listv2.adapters.TabSwitchAdapter;
import com.example.todo_listv2.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ViewPager2 viewPager;
    private TextView tabToday, tabAllTask;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        tabToday = binding.tabToday;
        tabAllTask = binding.tabAllTask;
        viewPager = binding.viewPager;

        viewPager.setAdapter(new TabSwitchAdapter(this));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateTableUI(position);
            }
        });

        tabToday.setOnClickListener(v -> {
            Log.d("TabToday", "CLICKED");
            viewPager.setCurrentItem(0);
        });

        tabAllTask.setOnClickListener(v -> {
            Log.d("TabMonth", "CLICKED");
            viewPager.setCurrentItem(1);
        });

    }

    private void updateTableUI(int position){
        if(position == 0){
            tabToday.setBackgroundResource(R.drawable.tab_selected_bg);
            tabToday.setTypeface(null, Typeface.BOLD);

            tabAllTask.setBackgroundResource(R.drawable.tab_unselected_bg);
            tabAllTask.setTypeface(null, Typeface.NORMAL);
        } else {
            tabToday.setBackgroundResource(R.drawable.tab_unselected_bg);
            tabToday.setTypeface(null, Typeface.NORMAL);

            tabAllTask.setBackgroundResource(R.drawable.tab_selected_bg);
            tabAllTask.setTypeface(null, Typeface.BOLD);
        }
    }

}
