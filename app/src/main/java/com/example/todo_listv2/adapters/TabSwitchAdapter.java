package com.example.todo_listv2.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.todo_listv2.fragments.AllTaskFragment;
import com.example.todo_listv2.fragments.TodayFragment;

public class TabSwitchAdapter extends FragmentStateAdapter {
    public TabSwitchAdapter(@NonNull Fragment fa){
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        if(position == 0) return new TodayFragment();
        else return new AllTaskFragment();
    }

    @Override
    public int getItemCount(){
        return 2;
    }
}