package com.example.iustin.bluelearn.repository;

import androidx.annotation.NonNull;

import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.activities.CourseActivity;
import com.example.iustin.bluelearn.domain.CourseSlide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    private static final String TAG = CourseRepository.class.getName();

    private DatabaseReference database;

    private CourseActivity courseActivity;

    private List<CourseSlide> courseSlides = new ArrayList<>();

    {
        database = FirebaseDatabase.getInstance().getReference().child("/categories");
    }

    public CourseRepository(CourseActivity courseActivity) {
        this.courseActivity = courseActivity;
    }

    public void loadCourses() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CourseSlide> result = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.child("/" + Utils.selectedCategory).getChildren()) {
                    result.add(data.getValue(CourseSlide.class));
                }

                courseActivity.takeCourse();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<CourseSlide> getCourseSlides() {
        return courseSlides;
    }

    public void setCourseSlides(List<CourseSlide> courseSlides) {
        this.courseSlides = courseSlides;
    }
}
