package com.example.iustin.bluelearn.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.adapters.CourseFragmentAdapter;
import com.example.iustin.bluelearn.fragments.CourseFragment;
import com.example.iustin.bluelearn.repository.CourseRepository;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity implements CourseFragment.OnFragmentInteractionListener {

    private ViewPager viewPagerCourse;
    private CourseRepository courseRepository = new CourseRepository(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        setTitle(Utils.selectedCategory);
        Utils.courseFragments = new ArrayList<>();
        courseRepository.loadCourse();

    }

    private void generateFragmentList() {
        if (!Utils.courseFragments.isEmpty()) {
            Utils.courseFragments.clear();
        }
        for (int i  = 0 ; i < Utils.courseSlides.size() ; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            CourseFragment fragment = new CourseFragment();
            fragment.setArguments(bundle);

            Utils.courseFragments.add(fragment);
        }
    }

    public void loadData() {
        generateFragmentList();

        viewPagerCourse = (ViewPager) findViewById(R.id.viewPagerCourse);

        CourseFragmentAdapter courseFragmentAdapter = new CourseFragmentAdapter(getSupportFragmentManager(), this, Utils.courseFragments);
        viewPagerCourse.setAdapter(courseFragmentAdapter);
        viewPagerCourse.setOffscreenPageLimit(Utils.courseFragments.size());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
