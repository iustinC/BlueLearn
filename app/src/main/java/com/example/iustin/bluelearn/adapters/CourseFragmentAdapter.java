package com.example.iustin.bluelearn.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.iustin.bluelearn.fragments.CourseFragment;

import java.util.List;

public class CourseFragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    List<CourseFragment> fragments;

    public CourseFragmentAdapter(@NonNull FragmentManager fm, Context context, List<CourseFragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  new StringBuilder("Statement ").append(position + 1).toString();
    }
}
