package com.example.iustin.bluelearn.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.repository.CourseRepository;
import com.google.android.material.tabs.TabLayout;

public class CourseActivity extends AppCompatActivity {

    private ViewPager viewPagerCourse;
    private TabLayout courseTab;
    private CourseRepository courseRepository = new CourseRepository(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        setTitle(Utils.selectedCategory);

        courseRepository.loadCourses();

    }

    public void takeCourse() {
        Utils.courseSlides = courseRepository.getCourseSlides();
    }
}
