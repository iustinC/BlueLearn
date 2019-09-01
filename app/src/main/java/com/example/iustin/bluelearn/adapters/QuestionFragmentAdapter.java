package com.example.iustin.bluelearn.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.iustin.bluelearn.fragments.QuestionFragment;

import java.util.List;

public class QuestionFragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<QuestionFragment> listFragments;

    public QuestionFragmentAdapter(@NonNull FragmentManager fm, Context context, List<QuestionFragment> questionFragments) {
        super(fm);
        this.context = context;
        this.listFragments = questionFragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return new StringBuilder("Question ").append(position + 1).toString();
    }


}
