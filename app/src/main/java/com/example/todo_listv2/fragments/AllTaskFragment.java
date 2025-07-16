package com.example.todo_listv2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.todo_listv2.databinding.FragmentAlltaskBinding;

public class AllTaskFragment extends Fragment {
    private FragmentAlltaskBinding binding;
    public AllTaskFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentAlltaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
